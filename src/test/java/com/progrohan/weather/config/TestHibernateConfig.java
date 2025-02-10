package com.progrohan.weather.config;

import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class TestHibernateConfig {

    DataSource dataSource;

    @Bean
    @Primary
    public LocalSessionFactoryBean testSessionFactory(DataSource testDataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(testDataSource);
        sessionFactory.setPackagesToScan("com.progrohan.weather.model");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    private Properties hibernateProperties() {
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect");
        return props;
    }

    @Bean
    @Primary
    public Flyway flyway() {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:db/test_migration")
                .baselineOnMigrate(true)
                .load();

        flyway.migrate();

        return flyway;
    }

    @Bean
    @Primary
    public PlatformTransactionManager testTransactionManager(SessionFactory testSessionFactory) {
        return new HibernateTransactionManager(testSessionFactory);
    }

}
