package com.awesome.configurations;

import com.awesome.applications.Applications;
import com.awesome.configurations.remote.CustomRemoteClientConfig;
import com.awesome.domains.Domains;
import com.awesome.infrastructures.Infrastructures;
import com.wishwingz.platform.service.auth.annotations.springboot.EnableAuthService;
import com.wishwingz.platform.service.board.annotations.springboot.EnableBoardService;
import com.wishwingz.platform.service.member.annotations.springboot.EnableMemberService;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

import javax.sql.DataSource;

@Slf4j
@Configuration
//Import 할 컨피그 선행
@Import({
        CustomRemoteClientConfig.class
})
@ComponentScan(basePackageClasses = {Domains.class, Infrastructures.class, Applications.class})
@EntityScan(basePackageClasses = {Domains.class, Infrastructures.class})
@EnableJpaRepositories(basePackageClasses = {Domains.class, Infrastructures.class})
@PropertySource(value = "classpath:config/custom-root-${spring.profiles.active}.properties", encoding = "UTF-8")
//어스 서비스 꽂음
@EnableAuthService
//멤버 서비스 꽂음
@EnableMemberService
//보드 서비스 꽂음
@EnableBoardService
@EnableAsync
@EnableConfigurationProperties(RootConfiguration.DataSourceProperties.class)
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
