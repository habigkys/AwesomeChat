package com.awesome.configurations;

import com.awesome.domains.entities.Entities;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackageClasses = {Entities.class})
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = {Entities.class})
public class JPAConfig {

}