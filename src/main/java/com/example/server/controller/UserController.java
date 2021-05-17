package com.example.server.controller;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.User;
import com.example.server.service.UserService;
import com.example.server.service.impl.UserServiceImpl;
import com.example.server.util.annotation.IsLogin;
import com.example.server.util.http.CookieUtils;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 全鸿润
 */
@Api
@CrossOrigin
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService = SpringContextConfig.getBean(UserServiceImpl.class);

    @GetMapping("/get-contact")
    @IsLogin
    public JsonResult handleGetContact(HttpServletRequest request) {

        String username = null;
        try {
            Cookie cookie = CookieUtils.findCookie(request.getCookies(), "username");
            if (cookie == null) {
                return JsonResultFactory.buildJsonResult(JsonResultStateCode.UNAUTHORIZED, JsonResultStateCode.UNAUTHORIZED_DESC, null);
            } else {
                username = cookie.getValue();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.UNAUTHORIZED,
                    JsonResultStateCode.UNAUTHORIZED_DESC,
                    null
            );
        }
        List<User> contactList = userService.getContactList(username);
        if (contactList != null) {
            if (contactList.size() > 0) {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.SUCCESS,
                        JsonResultStateCode.SUCCESS_DESC,
                        contactList
                );
            } else {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.NOT_FOUND,
                        JsonResultStateCode.NOT_FOUND_DESC,
                        null
                );
            }
        }
        return JsonResultFactory.buildFailureResult();
    }
}
