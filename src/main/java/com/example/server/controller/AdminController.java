package com.example.server.controller;

import com.example.server.config.SpringContextConfig;
import com.example.server.entity.User;
import com.example.server.service.AdminService;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author 全鸿润
 */
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService = SpringContextConfig.getBean("AdminServiceImpl");

    @PostMapping("/delete-users")
    public JsonResult handleDeleteUser(@RequestBody List<String> usernames) {

        Integer rows = adminService.deleteUsersByUsername(usernames);
        if (rows != null) {
            if (rows.equals(usernames.size())) {
                return JsonResultFactory.buildSuccessResult();
            } else {
                return JsonResultFactory.buildJsonResult(JsonResultStateCode.OPERATION_IS_NOT_COMPLETED, JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC, null);
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/logout-users")
    public JsonResult handleLogout(@RequestBody List<String> usernames) {
        Integer rows = adminService.updateUsersLogState(usernames, false);
        if (rows != null) {
            if (rows.equals(usernames.size())) {
                return JsonResultFactory.buildSuccessResult();
            } else {
                return JsonResultFactory.buildJsonResult(JsonResultStateCode.OPERATION_IS_NOT_COMPLETED, JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC, null);
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/login-users")
    public JsonResult handleLogin(@RequestBody List<String> usernames) {
        Integer rows = adminService.updateUsersLogState(usernames, true);
        if (rows != null) {
            if (rows.equals(usernames.size())) {
                return JsonResultFactory.buildSuccessResult();
            } else {
                return JsonResultFactory.buildJsonResult(JsonResultStateCode.OPERATION_IS_NOT_COMPLETED, JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC, null);
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @GetMapping("/get-users")
    public JsonResult handleGetUsers() {
        List<User> result;
        result = adminService.getUsers();
        if (result == null) {
            return JsonResultFactory.buildJsonResult(JsonResultStateCode.FAILED, JsonResultStateCode.FAILED_DESC, null);
        } else {
            return JsonResultFactory.buildJsonResult(JsonResultStateCode.SUCCESS, JsonResultStateCode.SUCCESS_DESC, result);
        }
    }
}
