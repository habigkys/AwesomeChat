package com.awesome.infrastructures.shared.chatroom;

import com.wishwingz.platform.core.auth.simple.enums.MemberType;
import com.wishwingz.platform.core.auth.simple.model.ServiceUser;
import lombok.Data;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Component
@Scope(scopeName = "websocket", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class WebSocketDefaultServiceUser implements ServiceUser {
    private String uuid;
    private String email;
    private String name;
    private MemberType memberType;
    private boolean isRemember;
    private LocalDateTime loginDttm;
    private List<String> roles;

    /**
     * @return
     */
    @Override
    public boolean isLogin() {
        return !StringUtils.isBlank(uuid);
    }
}
