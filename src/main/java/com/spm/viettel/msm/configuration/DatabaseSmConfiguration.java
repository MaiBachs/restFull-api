package com.spm.viettel.msm.configuration;

import com.slyak.spring.jpa.GenericJpaRepositoryFactoryBean;
import com.slyak.spring.jpa.GenericJpaRepositoryImpl;
import com.viettel.common.util.EncryptDecryptUtils;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author luan
 */
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.spm.viettel.msm.repository.sm",
        repositoryBaseClass = GenericJpaRepositoryImpl.class,
        repositoryFactoryBeanClass = GenericJpaRepositoryFactoryBean.class,
        entityManagerFactoryRef = "entityManagerFactory",
        transactionManagerRef = "transactionManager")
@ConfigurationProperties(prefix = "spring.datasource")
public class DatabaseSmConfiguration extends HikariConfig {
    @Value("${spring.datasource.encode:false}")
    private Boolean encode;
    @Value("${spring.datasource.encode_file}")
    private String encodeFilePath;

    @Bean(name = "dataSource")
    @Primary
    public DataSource defaultDataSource() {
        HikariConfig hikariConfig = new HikariConfig();

        if (encode) {
            try {
                Map<String, String> config = new HashMap<>();
                String decryptString = EncryptDecryptUtils.decryptFile(encodeFilePath);
                String[] properties = decryptString.split("\r\n");
                for (String property : properties) {
                    String[] temp = property.split("=", 2);
                    if (temp.length == 2) {
                        String key = temp[0];
                        String value = temp[1];
                        config.put(key, value);
                    }
                }
                hikariConfig.setJdbcUrl(config.get("hibernate.connection.url"));
                hikariConfig.setDriverClassName(this.getDriverClassName());
                hikariConfig.setUsername(config.get("hibernate.connection.username"));
                hikariConfig.setPassword(config.get("hibernate.connection.password"));
                hikariConfig.setConnectionTimeout(this.getConnectionTimeout());
                hikariConfig.setIdleTimeout(this.getIdleTimeout());
                hikariConfig.setMaxLifetime(this.getMaxLifetime());
                hikariConfig.setMinimumIdle(this.getMinimumIdle());
                hikariConfig.setMaximumPoolSize(100);
            } catch (Exception e) {
                hikariConfig = this;
            }
        }else {
            hikariConfig = this;
        }
        return new HikariDataSource(hikariConfig);

    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory(
            final EntityManagerFactoryBuilder builder, @Qualifier("dataSource") final DataSource dataSource,
            final JpaProperties jpaProperties) {
        return builder
                .dataSource(dataSource)
                .packages("com.spm.viettel.msm.repository.sm.entity")
                .mappingResources(jpaProperties.getMappingResources().toArray(new String[0]))
                .persistenceUnit("default")
                .build();
    }

    @Bean(name = "transactionManager")
    @Primary
    public JpaTransactionManager defaultTransactionManager(
            @Qualifier("entityManagerFactory") final EntityManagerFactory emf) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(emf);
        return transactionManager;
    }

    @Bean(name = "SMSessionFactory")
    public LocalSessionFactoryBean anypaySessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(defaultDataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"com.spm.viettel.msm.repository.sm.entity"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }
    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        hibernateProperties.setProperty(
                "hibernate.show_sql", "true");
        return hibernateProperties;
    }
}
