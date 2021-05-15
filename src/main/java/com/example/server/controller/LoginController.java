package com.example.server.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.UserMessage;
import com.example.server.entity.User;
import com.example.server.service.AuthService;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author 全鸿润
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class LoginController {

    private AuthService authService = SpringContextConfig.getBean("LoginServiceImpl");

    @PostMapping("/login")
    public JsonResult handleLogin(@RequestBody JSONObject userMessage, HttpServletResponse response) {

        String username = userMessage.getString("username");
        String password = userMessage.getString("password");
        String msg = authService.handleLogin(username, password);
        JsonResult res = null;
        if (JsonResultStateCode.USERNAME_WRONG_DESC.equals(msg)) {
            res = JsonResultFactory.buildJsonResult(JsonResultStateCode.USERNAME_WRONG, msg, null);
        } else if (JsonResultStateCode.PASSWORD_WRONG_DESC.equals(msg)) {
            res = JsonResultFactory.buildJsonResult(JsonResultStateCode.PASSWORD_WRONG, msg, null);
        } else if (JsonResultStateCode.SUCCESS_DESC.equals(msg)) {
            res = JsonResultFactory.buildSuccessResult();
        } else {
            res = JsonResultFactory.buildJsonResult(JsonResultStateCode.UNKNOWN_ERROR, msg, null);
        }
        return res;
    }

    @PostMapping("/register")
    public JsonResult handleRegister(@RequestBody UserMessage userMessage) {

        Integer rows = authService.registerUser(userMessage);
        if (rows != null && rows == 1) {
            return JsonResultFactory.buildSuccessResult();
        } else if (rows != null && rows.equals(JsonResultStateCode.USERNAME_IS_EXITED)) {
            return JsonResultFactory.buildJsonResult(JsonResultStateCode.USERNAME_IS_EXITED, JsonResultStateCode.USERNAME_IS_EXITED_DESC, null);
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/update-password")
    public JsonResult handleUpdatePassword(@RequestBody JSONObject jsonObject) {
        String userName = jsonObject.getString("username");
        String password = jsonObject.getString("password");
        String newPassword = jsonObject.getString("newPassword");

        User user = authService.findUserByUsername(userName);
        if (user == null || !user.getPassword().equals(password)) {
            return JsonResultFactory.buildFailureResult();
        }
        Integer rows = authService.updatePassword(userName, newPassword);
        if (rows != null && rows == 1) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }
}