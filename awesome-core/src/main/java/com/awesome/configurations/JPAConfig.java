package com.awesome.configurations;

import com.awesome.domains.Project.entities.ProjectEntities;
import com.awesome.domains.ProjectTask.entities.ProjectTaskEntities;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackageClasses = {ProjectEntities.class, ProjectTaskEntities.class})
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = {ProjectEntities.class, ProjectTaskEntities.class})
public class JPAConfig {

}