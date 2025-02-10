package com.progrohan.weather.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:application-test.properties")
public class TestDataSourseConfig {

    private final Environment env;

    public TestDataSourseConfig(Environment env) {
        this.env = env;
    }

    @Bean
    @Primary
    public DataSource testDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(env.getRequiredProperty("test.database.driver"));
        dataSource.setUrl(env.getRequiredProperty("test.database.url"));
        dataSource.setUsername("");
        dataSource.setPassword("");
        return dataSource;
    }

}
