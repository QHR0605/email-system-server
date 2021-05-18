package com.example.server.util.aspect;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.server.util.annotation.IsSupperAdmin;
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
@Aspect
@Component
public class SupperAdminAspect {
    /**
     * 切点方法
     *
     * @param isSupperAdmin 标识管理员的注解
     */
    @Pointcut("@annotation(isSupperAdmin)")
    public void print(IsSupperAdmin isSupperAdmin) {
    }

    @Around(value = "print(isSupperAdmin)", argNames = "point,isSupperAdmin")
    public Object authorityVerify(ProceedingJoinPoint point, IsSupperAdmin isSupperAdmin) {

        System.err.println("超级管理员的切面");
        try {
            HttpServletRequest request = HttpUtil.getRequest();
            Cookie cookie = CookieUtils.findCookie(request.getCookies(), "token");
            if (cookie == null) {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.UNAUTHORIZED,
                        JsonResultStateCode.UNAUTHORIZED_DESC,
                        null
                );
            }
            String token = cookie.getValue();
            if (token == null) {
                return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
            } else {
                if (TokenVerifier.verifyToken(token)) {
                    int type = TokenVerifier.getUserType(token);
                    if (type == 2) {
                        return point.proceed();
                    }
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
                } else {
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
                }
            }
        } catch (JWTVerificationException j) {
            return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNKNOWN_ERROR, JsonResultStateCode.UNKNOWN_ERROR_DESC, null);
        }
    }
}
