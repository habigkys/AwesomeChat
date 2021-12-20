package com.awesome.configurations.chat;

import com.awesome.infrastructures.chat.WebSocketHandShakeInterceptor;
import com.awesome.infrastructures.shared.chatroom.WebSocketDefaultServiceUser;
import com.wishwingz.platform.core.auth.simple.model.CookieBaseServiceUserFactory;
import com.wishwingz.platform.core.auth.simple.model.DefaultServiceUser;
import com.wishwingz.platform.core.auth.simple.model.ServiceUser;
import com.wishwingz.platform.core.auth.simple.utils.AuthCookieValueHandler;
import com.wishwingz.platform.core.auth.simple.web.interceptors.AuthenticationCookieHandleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/api/v1/chat/stomp")
                .setAllowedOrigins("*")
                .withSockJS()
                .setInterceptors(webSocketHandShakeInterceptor());
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub");
        registry.enableSimpleBroker("/sub");
    }

    @Bean
    public WebSocketHandShakeInterceptor webSocketHandShakeInterceptor() {
        return new WebSocketHandShakeInterceptor();
    }
}
