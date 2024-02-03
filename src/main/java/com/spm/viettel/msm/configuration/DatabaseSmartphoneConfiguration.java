package com.spm.viettel.msm.configuration;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author luan
 */
@Configuration
@ConfigurationProperties(prefix = "spring.smartphone.datasource")
@EnableTransactionManagement
@EnableJpaRepositories(
        entityManagerFactoryRef = "smartphoneEntityManager",
        transactionManagerRef = "smartphoneTransactionManager",
        basePackages = "com.spm.viettel.msm.repository.smartphone"
)
public class DatabaseSmartphoneConfiguration extends BaseDatabaseConfiguration {
    @Value("${spring.smartphone.datasource.encode_file}")
    private String encodeFilePath;

    @Bean(name = "smartphoneDataSource")
    public DataSource smartphoneDataSource() {
        return readDatabase(encodeFilePath);

    }

    @Bean(name = "smartphoneEntityManager")
    public LocalContainerEntityManagerFactoryBean smartphoneEntityManager(EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(smartphoneDataSource())
                .packages("com.spm.viettel.msm.repository.smartphone.entity")
                .persistenceUnit("smartphonePU")
                .build();
    }

    @Bean(name = "smartphoneTransactionManager")
    public PlatformTransactionManager smartphoneTransactionManager(@Qualifier("smartphoneEntityManager") EntityManagerFactory smartphoneEntityManager) {
        return new JpaTransactionManager(smartphoneEntityManager);
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        hibernateProperties.setProperty(
                "hibernate.show_sql", "true");
        return hibernateProperties;
    }

    @Bean(name = "smartphoneSessionFactory")
    public LocalSessionFactoryBean smartphoneSessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(smartphoneDataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"com.spm.viettel.msm.repository.smartphone.entity"});
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

}
