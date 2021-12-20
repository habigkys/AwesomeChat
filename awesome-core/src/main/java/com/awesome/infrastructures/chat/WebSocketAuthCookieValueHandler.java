package com.awesome.infrastructures.chat;

import com.wishwingz.platform.core.auth.simple.model.CookieBaseServiceUserFactory;
import com.wishwingz.platform.core.auth.simple.model.ServiceUser;
import com.wishwingz.platform.core.auth.simple.utils.AuthUtils;
import com.wishwingz.platform.core.web.utils.CookieBox;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.time.format.DateTimeFormatter;

@Component
public final class WebSocketAuthCookieValueHandler {
    @Value("${auth.cookie.key}")
    private String userCookieKey;
    @Value("${auth.cookie.maxage}")
    private int userCookieMaxAge;
    @Value("${auth.cookie.data.seperator}")
    private String userInfoValueSeperator;
    @Value("${auth.cookie.data.version}")
    private String loginDataFormatVersion;
    @Value("${auth.cookie.data.count}")
    private int loginDataCount;
    @Value("${auth.cookie.data.crypt.key}")
    private String userDataCryptKey;
    @Value("${auth.cookie.data.dateformat}")
    private String userDataDateformat;

    @Value("${auth.domain}")
    private String domain;
    @Value("${env.encoding}")
    private String encoding;

    /**
     * 쿠키 -> ServiceUser
     */
    public ServiceUser toServiceUser(HttpServletRequest request) throws Exception {
        ServiceUser serviceUser = CookieBaseServiceUserFactory.DEFAULT_USER;

        String encryptedUserInfo = new CookieBox(request).getValue(userCookieKey);
        if (StringUtils.isNotBlank(encryptedUserInfo)) {
            try {
                CookieBaseServiceUserFactory userFactory = new CookieBaseServiceUserFactory(userInfoValueSeperator,
                        loginDataCount, loginDataFormatVersion, userDataDateformat);

                return userFactory.createUser(
                        AuthUtils.decryptedUser(encryptedUserInfo, Charset.forName(encoding), userDataCryptKey));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return serviceUser;
    }

    public ServiceUser toServiceUser(String encryptedUserInfo) throws Exception {
        ServiceUser serviceUser = CookieBaseServiceUserFactory.DEFAULT_USER;

        if (StringUtils.isNotBlank(encryptedUserInfo)) {
            try {
                CookieBaseServiceUserFactory userFactory = new CookieBaseServiceUserFactory(userInfoValueSeperator,
                        loginDataCount, loginDataFormatVersion, userDataDateformat);

                return userFactory.createUser(
                        AuthUtils.decryptedUser(encryptedUserInfo, Charset.forName(encoding), userDataCryptKey));
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        return serviceUser;
    }

    /**
     * ServiceUser -> 쿠키
     *
     * @param serviceUser
     * @return
     * @throws Exception
     */
    public String toCookeiValue(ServiceUser serviceUser) throws Exception {
        StringBuffer ret = new StringBuffer();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(userDataDateformat);
        String formattedDateTime = serviceUser.getLoginDttm().format(formatter);

        appendToValue(ret, loginDataFormatVersion);
        appendToValue(ret, serviceUser.getEmail());
        appendToValue(ret, serviceUser.getName());
        appendToValue(ret, serviceUser.getUuid());
        appendToValue(ret, formattedDateTime);

        // 로그인 사용자 정보 암호화
        return AuthUtils.encryptedUser(ret.toString(), Charset.forName(encoding), userDataCryptKey);
    }

    /**
     * 로그인 쿠기를 굽는다.
     *
     * @param response
     * @param serviceUser
     * @throws Exception
     */
    public void burnLoginCookies(HttpServletResponse response, ServiceUser serviceUser) throws Exception {
        // 사용자 정보 쿠키
        CookieBox.CookieCooker cookieCooker = CookieBox.getCooker(userCookieKey, toCookeiValue(serviceUser));

        // cookieCooker.domain("." + domain);
        // tomcat 8 version
        cookieCooker.domain(domain);
        cookieCooker.path("/");
        cookieCooker.maxAge(userCookieMaxAge);
        cookieCooker.cook(response);
    }

    /**
     * 로그인 쿠키 삭제
     *
     * @param response
     */
    public void expireLoginCookies(HttpServletResponse response) {
        CookieBox.CookieCooker cookieCooker = CookieBox.getCooker(userCookieKey, "");

        // cookieCooker.domain("." + domain);
        // tomcat 8 version
        cookieCooker.domain(domain);
        cookieCooker.path("/");
        cookieCooker.maxAge(0);
        cookieCooker.cook(response);
    }

    public void payloadServiceUser(HttpServletRequest request, ServiceUser serviceUser) {
        request.setAttribute(userCookieKey, serviceUser);
    }

    /**
     * 값이 없어도 null 로 채워야 한다. 쿠키 값의 순서로 추출하기 때문이다. 기존 쿠키를 읽어야 하는 이슈도 있어서 쉽게 고칠수
     * 없음
     *
     * @param buffer
     * @param value
     */
    private void appendToValue(StringBuffer buffer, String value) {
        if (buffer.length() != 0) {
            buffer.append(userInfoValueSeperator);
        }

        if (StringUtils.isBlank(value)) {
            value = "null";
        }

        buffer.append(value);
    }
}
