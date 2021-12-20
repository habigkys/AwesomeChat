package com.awesome.infrastructures.chat;

import com.awesome.infrastructures.shared.chatroom.WebSocketDefaultServiceUser;
import com.wishwingz.platform.core.auth.simple.model.CookieBaseServiceUserFactory;
import com.wishwingz.platform.core.auth.simple.model.ServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class WebSocketHandShakeInterceptor implements HandshakeInterceptor {
    @Autowired
    private WebSocketAuthCookieValueHandler webSocketAuthCookieValueHandler;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletServerRequest = (ServletServerHttpRequest) request;
            ServletServerHttpResponse servletServerResponse = (ServletServerHttpResponse) response;
            HttpServletRequest servletRequest = servletServerRequest.getServletRequest();
            HttpServletResponse servletResponse = servletServerResponse.getServletResponse();

            Cookie token = WebUtils.getCookie(servletRequest, "USER");
            attributes.put("USER", token.getValue());

            ServiceUser serviceUser = webSocketAuthCookieValueHandler.toServiceUser(servletRequest);

            if (CookieBaseServiceUserFactory.DEFAULT_USER.equals(serviceUser)) {
                removeAuthCookie(servletResponse);
            } else {
                attributes.put("webSocketDefaultServiceUser", serviceUser);

                keepAuthCookie(servletResponse, serviceUser);
            }
        }
        return true;
    }

    private void removeAuthCookie(HttpServletResponse response) {
        webSocketAuthCookieValueHandler.expireLoginCookies(response);
    }

    private void keepAuthCookie(HttpServletResponse response, ServiceUser serviceUser) throws Exception {
        webSocketAuthCookieValueHandler.burnLoginCookies(response, serviceUser);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
    }
}
