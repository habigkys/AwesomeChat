package com.awesome.configurations;

import com.awesome.api.controllers.ApiControllers;
import com.awesome.infrastructures.shared.chatroom.WebSocketDefaultServiceUser;
import com.awesome.web.controllers.WebControllers;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.wishwingz.platform.core.auth.simple.model.AuthenticationRepository;
import com.wishwingz.platform.core.auth.simple.model.DefaultServiceUser;
import com.wishwingz.platform.core.auth.simple.utils.AuthCookieValueHandler;
import com.wishwingz.platform.core.auth.simple.web.interceptors.AuthenticationCookieHandleInterceptor;
import com.wishwingz.platform.core.auth.simple.web.interceptors.AuthenticationInterceptor;
import com.wishwingz.platform.core.auth.simple.web.interceptors.AuthorizationInterceptor;
import com.wishwingz.platform.core.file.io.upload.FileItemArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.*;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.web.context.annotation.RequestScope;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
@ComponentScan(basePackageClasses = {ApiControllers.class, WebControllers.class})
@PropertySource("classpath:config/custom-api-${spring.profiles.active}.properties")
public class WebConfiguration implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //인증 정보 파싱
        registry.addInterceptor(authenticationCookieHandleInterceptor())
                .addPathPatterns("/api/**/*");
        //인증
        registry.addInterceptor(authenticationInterceptor())
                .addPathPatterns("/api/**/*")
                .excludePathPatterns(
                        "/api/v1/auth/login-naver",
                        "/api/v1/auth/login-kakao"
                );
        //인가
        registry.addInterceptor(authorizationInterceptor()).addPathPatterns("/api/**/*");
    }

    @Qualifier("authorityRepositoryImpl")
    @Autowired
    private AuthenticationRepository authenticationRepository;

    @Bean
    @RequestScope
    public DefaultServiceUser user() {
        return new DefaultServiceUser();
    }

    @Bean
    public AuthCookieValueHandler authCookieValueHandler() {
        return new AuthCookieValueHandler();
    }

    @Bean
    public AuthenticationCookieHandleInterceptor authenticationCookieHandleInterceptor() {
        return new AuthenticationCookieHandleInterceptor();
    }

    @Bean
    public AuthenticationInterceptor authenticationInterceptor() {
        AuthenticationInterceptor authenticationInterceptor = new AuthenticationInterceptor();
        return authenticationInterceptor;
    }

    @Bean
    public AuthorizationInterceptor authorizationInterceptor() {
        AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor();
        authorizationInterceptor.setAuthenticationRepository(authenticationRepository);
        return authorizationInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/web/templates/");
    }

    /**
     * StandardServletMultipartResolver 는 서블릿 3.0 에서만 동작하므로
     * CommonsMultipartResolver 를 장착해줌
     * Streaming API 를 사용한다.
     *
     * @return
     * @see "http://wiki.wishwingz.co.kr:8090/display/PLATFORM/CommonsMultipartResolver"
     */
    @Bean
    public MultipartResolver multipartResolver() {
        return new CommonsMultipartResolver();
    }

    /**
     * 아규먼트 리졸버
     *
     * @param argumentResolvers
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        // MAX 사이즈를 지정하지 않으면 기본적으로 2000로 잘라준다.
        PageableHandlerMethodArgumentResolver pageableHandlerMethodArgumentResolver = new PageableHandlerMethodArgumentResolver();
        pageableHandlerMethodArgumentResolver.setMaxPageSize(100);

        argumentResolvers.add(pageableHandlerMethodArgumentResolver);
        argumentResolvers.add(fileItemArgumentResolver());
    }

    /**
     * 아규먼트 리졸버 - File 업로드
     *
     * @return
     */
    @Bean
    public FileItemArgumentResolver fileItemArgumentResolver() {
        return new FileItemArgumentResolver();
    }

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat("yyyy-MM-dd");
            builder.serializers(new LocalDateSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            builder.serializers(new LocalDateTimeSerializer(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        };
    }
}
