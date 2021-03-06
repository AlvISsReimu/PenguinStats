package io.penguinstats.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.penguinstats.constant.Constant.DefaultValue;
import io.penguinstats.service.UserService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Component("cookieUtil")
public class CookieUtil {

    private static CookieUtil cookieUtil;

    @Autowired
    private UserService userService;

    @PostConstruct
    public void init() {
        cookieUtil = this;
        cookieUtil.userService = this.userService;
    }

    public static void setUserIDCookie(HttpServletRequest request, HttpServletResponse response, String userID)
            throws UnsupportedEncodingException {
        String host = request.getHeader("Host");
        if (host == null) {
            host = "";
        }

        ZonedDateTime expireTime = ZonedDateTime.now(ZoneId.of("Z")).plusSeconds(DefaultValue.USER_ID_COOKIE_EXPIRY);
        String expires = expireTime.format(DateTimeFormatter.RFC_1123_DATE_TIME);

        StringBuilder sb = new StringBuilder();
        sb.append("userID=").append(URLEncoder.encode(userID, "UTF-8")).append("; Max-Age=")
                .append(DefaultValue.USER_ID_COOKIE_EXPIRY).append("; Expires=").append(expires).append("; Path=")
                .append("/").append("; Domain=").append(".").append(host).append("; SameSite=").append("None")
                .append("; Secure");

        response.addHeader("Set-Cookie", sb.toString());
    }

    /** 
     * @Title: readUserIDFromCookie 
     * @Description: Read userID from cookies.
     * @param request
     * @return String
     */
    public String readUserIDFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        String userID = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userID")) {
                    userID = cookie.getValue();
                    if (userID != null) {
                        try {
                            userID = URLDecoder.decode(userID, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            log.error("Error in getUserIDFromCookies: ", e);
                        }
                    } else if (userID == null) {
                        log.warn("userID's value in the cookie map is null.");
                    }
                    break;
                }
            }
        }
        return userID;
    }

}
