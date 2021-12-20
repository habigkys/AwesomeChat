package com.awesome.configurations.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties(CustomRemoteClientConfig.RemoteClientProperties.class)
public class CustomRemoteClientConfig {
    @Autowired
    private RemoteClientProperties properties;

    @ConditionalOnMissingBean(RestTemplateBuilder.class)
    @Bean
    public RestTemplateBuilder restTemplateBuilder() {
        return new RestTemplateBuilder();
    }

    @Bean(name = "restTemplateClientForSnakeCase")
    public RestTemplate restTemplateClientForSnakeCase(RestTemplateBuilder builder) {
        RestTemplate build = builder.build();
        build.setRequestFactory(getDefaultRequestFactory());
        build.setMessageConverters(getSnakeMessageConverters());
        return build;
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        RestTemplate build = builder.build();
        build.setRequestFactory(getDefaultRequestFactory());
        build.setMessageConverters(getMessageConverters());
        return build;
    }

    private List<HttpMessageConverter<?>> getMessageConverters() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
        List<HttpMessageConverter<?>> httpMessageConverters = getDefaultHttpMessageConverters();
        httpMessageConverters.add(mappingJackson2HttpMessageConverter);
        return httpMessageConverters;
    }

    private List<HttpMessageConverter<?>> getSnakeMessageConverters() {
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter(getObjectMapperForSnakeCase());
        mappingJackson2HttpMessageConverter.setSupportedMediaTypes(Lists.newArrayList(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML));
        List<HttpMessageConverter<?>> httpMessageConverters = getDefaultHttpMessageConverters();
        httpMessageConverters.add(mappingJackson2HttpMessageConverter);

        return httpMessageConverters;
    }

    private List<HttpMessageConverter<?>> getDefaultHttpMessageConverters() {
        List<HttpMessageConverter<?>> httpMessageConverters = new ArrayList<>();
        // form 데이터를  post 로 전송하도록 추가
        httpMessageConverters.add(new FormHttpMessageConverter());
        httpMessageConverters.add(new StringHttpMessageConverter());
        return httpMessageConverters;
    }


    private ClientHttpRequestFactory getDefaultRequestFactory() {
        // make Connection Pool
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(properties.getConnectionMax());
        cm.setDefaultMaxPerRoute(properties.getConnectionMaxPerRoute());

        // Custom HttpClient To Handle Retry, NumberUtils 에 의하여 기본값은 0 이 된다.
        int retryCount = properties.getRetry();

        CloseableHttpClient httpclient = HttpClients.custom()
                .setConnectionManager(cm)
                //Apache Default
                .setRetryHandler(new DefaultHttpRequestRetryHandler(retryCount, false))
                .build();

        // Make Factory
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setBufferRequestBody(properties.isBufferRequestBody());
        factory.setConnectionRequestTimeout(properties.getConnectionRequestTimeout());
        factory.setConnectTimeout(properties.getConnectTimeout());
        factory.setReadTimeout(properties.getReadTimeout());
        factory.setHttpClient(httpclient);
        return factory;
    }


    private ObjectMapper getObjectMapperForSnakeCase() {
        return Jackson2ObjectMapperBuilder.json().propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE).build();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "spring.remote.resttemplate")
    public class RemoteClientProperties {
        private int connectionMax;
        private int connectionMaxPerRoute;
        private int retry;
        private boolean bufferRequestBody;
        private int connectTimeout;
        private int connectionRequestTimeout;
        private int readTimeout;
    }
}
