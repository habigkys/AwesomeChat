package com.awesome.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Objects;

@EntityScan(basePackages = "com.awesome.domains")
@EnableConfigurationProperties(AwesomeDatasourceConfiguration.DataSourceProperties.class)
@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.awesome.domains")
public class AwesomeDatasourceConfiguration {
    @Autowired
    private DataSourceProperties properties;

    private final JpaProperties jpaProperties;
    private final HibernateProperties hibernateProperties;

    public AwesomeDatasourceConfiguration(JpaProperties jpaProperties, HibernateProperties hibernateProperties){
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = hibernateProperties;
    }

    @Bean
    @Primary
    public DataSource awesomeDataSource() {
        return DataSourceBuilder.create().url(properties.getJdbcUrl()).username(properties.getUsername()).password(properties.getPassword()).driverClassName(properties.getDriverClassName()).build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) {
        var properties = hibernateProperties.determineHibernateProperties(
                jpaProperties.getProperties(), new HibernateSettings());
        return builder.dataSource(awesomeDataSource())
                .properties(properties)
                .packages("com.awesome.domains")
                .build();
    }

    @Bean
    @Primary
    PlatformTransactionManager transactionManager(EntityManagerFactoryBuilder builder) {
        return new JpaTransactionManager(Objects.requireNonNull(entityManagerFactory(builder).getObject()));
    }

    @Setter
    @Getter
    @ConfigurationProperties(prefix = "spring.datasource.awesome")
    public static class DataSourceProperties {
        private String jdbcUrl;
        private String username;
        private String password;
        private String driverClassName;
    }
}
