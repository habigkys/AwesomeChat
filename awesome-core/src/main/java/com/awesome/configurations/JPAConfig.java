package com.awesome.configurations;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = {"com.awesome.domains"})
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.awesome.domains"})
public class JPAConfig {

}