package com.spm.viettel.msm.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @author luan
 */
@Configuration
@ConfigurationProperties(prefix = "spring.anypay.datasource")
public class DatabaseAnypayConfiguration extends BaseDatabaseConfiguration {
    @Value("${spring.anypay.datasource.encode_file}")
    private String encodeFilePath;

    @Bean(name = "anypayDataSource")
    public DataSource anypayDataSource() {
        return readDatabase(encodeFilePath);

    }

    @Bean(name = "anypaySessionFactory")
    public LocalSessionFactoryBean anypaySessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(anypayDataSource());
        sessionFactory.setPackagesToScan(
                new String[]{"com.bitel.repository.anypay.entity"});
        sessionFactory.setHibernateProperties(hibernateProperties());

        return sessionFactory;
    }

    private final Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(
                "hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
        return hibernateProperties;
    }
}
