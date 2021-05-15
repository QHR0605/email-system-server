package com.example.server.util.http;

import javax.servlet.http.Cookie;

/**
 * @author 全鸿润
 */
public class CookieUtil {
    public static Cookie getCookie(Cookie[] cookies, String name) throws Exception {
        if (cookies != null) {
            //获取Cookie中的token
            for (Cookie cookie : cookies
            ) {
                if (name.equals(cookie.getName())) {
                    return cookie;
                }
            }
        }
        return null;
    }
}
