package com.example.server.util.aspect;

import com.example.server.util.annotation.IsLogin;
import com.example.server.util.http.CookieUtils;
import com.example.server.util.http.HttpUtil;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import com.example.server.util.token.TokenVerifier;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 全鸿润
 */
@Component
@Aspect
public class LoginAspect {

    @Pointcut(value = "@annotation(isLogin)", argNames = "isLogin")
    public void print(IsLogin isLogin) {
    }

    @Around(value = "print(isLogin)", argNames = "point,isLogin")
    public Object authToken(ProceedingJoinPoint point, IsLogin isLogin) {
        try {
            HttpServletRequest request = HttpUtil.getRequest();
            Cookie tokenCookie = CookieUtils.findCookie(request.getCookies(), "token");
            Cookie usernameCookie = CookieUtils.findCookie(request.getCookies(),"username");
            if (tokenCookie == null || usernameCookie == null){
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.UNAUTHORIZED,
                        JsonResultStateCode.UNAUTHORIZED_DESC,
                        null
                );
            }
            String token = tokenCookie.getValue();
            String username = usernameCookie.getValue();
            System.out.println("token: "+token);
            System.out.println("username: "+username);
            if (token == null || username == null) {
                return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
            } else {
                if (TokenVerifier.verifyToken(token) && username.equals(TokenVerifier.getUserNameFromToken(token))) {
                    return point.proceed();
                } else {
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
        }
    }
}
