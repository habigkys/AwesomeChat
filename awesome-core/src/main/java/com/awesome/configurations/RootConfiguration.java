package com.awesome.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@EnableConfigurationProperties(RootConfiguration.DataSourceProperties.class)
@Configuration
public class RootConfiguration {

    @Autowired
    private DataSourceProperties properties;

    @Bean
    @Primary
    public DataSource dataSource() {
        return DataSourceBuilder.create().url(properties.getJdbcUrl()).username(properties.getUsername()).password(properties.getPassword()).driverClassName(properties.getDriverClassName()).build();
    }


    @Setter
    @Getter
    @ConfigurationProperties(prefix = "spring.datasource")
    public static class DataSourceProperties {
        private String jdbcUrl;
        private String username;
        private String password;
        private String driverClassName;
    }
}
