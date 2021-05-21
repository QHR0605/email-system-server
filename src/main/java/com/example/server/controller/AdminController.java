package com.example.server.controller;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.*;
import com.example.server.entity.Filter;
import com.example.server.entity.Log;
import com.example.server.entity.ServerMessage;
import com.example.server.entity.User;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.AdminService;
import com.example.server.service.impl.AdminServiceImpl;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

/**
 * @author 全鸿润
 */
@Api
@RestController
@CrossOrigin
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService = SpringContextConfig.getBean(AdminServiceImpl.class);

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

    @PostMapping("/login-users")
    public JsonResult handleLogin(@RequestBody List<String> usernames) {
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

    @GetMapping("/get-users")
    public JsonResult handleGetUsers() {
        List<User> userList = adminService.getAllUsers();
        if (userList != null) {
            if (userList.size() > 0) {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.SUCCESS,
                        JsonResultStateCode.SUCCESS_DESC,
                        userList
                );
            } else {
                return JsonResultFactory.buildJsonResult(
                        JsonResultStateCode.NOT_FOUND,
                        JsonResultStateCode.NOT_FOUND_DESC,
                        null
                );
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/auth")
    public JsonResult handleAuthorize(@RequestBody List<UserNameAndType> userNameAndTypes) {

        if (userNameAndTypes != null) {
            List<String> usernames = new LinkedList<>();
            for (UserNameAndType user : userNameAndTypes
            ) {
                usernames.add(user.getUsername());
            }
            Integer type = userNameAndTypes.get(0).getAccountType();
            Integer rows = adminService.auth(usernames, type);
            if (rows != null) {
                if (rows.equals(userNameAndTypes.size())) {
                    return JsonResultFactory.buildSuccessResult();
                } else {
                    return JsonResultFactory.
                            buildJsonResult(
                                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED,
                                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC, null);
                }
            } else {
                return JsonResultFactory.buildFailureResult();
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }

    }

    @PostMapping("/create-user")
    public JsonResult handleCreate(@RequestBody NewUserMessage userMessage) {

        Integer rows;
        rows = adminService.createUser(userMessage);
        if (rows != null) {
            if (rows == 1) {
                return JsonResultFactory.buildSuccessResult();
            } else {
                return JsonResultFactory.buildFailureResult();
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/change-server-state")
    public JsonResult handleChangeServerState(@RequestBody ServerStateMsg serverState) {

        System.out.println(serverState);
        Integer state = adminService.changeServerState(serverState);
        JsonResult res = JsonResultFactory.buildSuccessResult();
        ServerPortMsg msg = new ServerPortMsg();
        if (state != null && state == 1) {
            if (serverState.getServerState()) {
                if (serverState.getServerType() == 0) {
                    msg.setServerPort(SmtpServer.getPort());
                    msg.setServerType(serverState.getServerType());
                    msg.setSid(serverState.getSid());
                } else if (serverState.getServerType() == 1) {
                    msg.setServerPort(Pop3Server.getPort());
                    msg.setServerType(serverState.getServerType());
                    msg.setSid(serverState.getSid());
                } else {
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.FAILED, "没有该服务", null);
                }
                adminService.restartServer(msg);
            } else {
                if (serverState.getServerType() == 0) {
                    msg.setServerPort(SmtpServer.getPort());
                    msg.setServerType(serverState.getServerType());
                    msg.setSid(serverState.getSid());

                } else if (serverState.getServerType() == 1) {
                    msg.setServerPort(Pop3Server.getPort());
                    msg.setServerType(serverState.getServerType());
                    msg.setSid(serverState.getSid());
                } else {
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.FAILED, "没有该服务", null);
                }
                adminService.stopServer(msg);
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
        return res;
    }

    @PostMapping("/change-server-port")
    public JsonResult handleChangeServerPort(@RequestBody ServerPortMsg msg) {

        //修改端口号
        Integer row = adminService.changeServerPort(msg);
        JsonResult res = JsonResultFactory.buildSuccessResult();
        JsonResult res1 = JsonResultFactory.buildFailureResult();
        if (row != null && row == 1) {
            //暂停当前端口
            Integer r1 = adminService.stopServer(msg);
            if (r1 != null && r1.equals(JsonResultStateCode.SUCCESS)) {
                Integer r2 = JsonResultStateCode.SUCCESS;
                if (msg.getServerType() == 0) {
                    if (!SmtpServer.isShutDown()) {
                        r2 = adminService.restartServer(msg);
                    }
                } else if (msg.getServerType() == 1) {
                    if (!Pop3Server.isShutDown()) {
                        r2 = adminService.restartServer(msg);
                    }
                }
                if (r2 != null && r2.equals(JsonResultStateCode.SUCCESS)) {
                    return res;
                } else {
                    return res1;
                }
            } else {
                return res1;
            }
        } else {
            return res1;
        }
    }

    @GetMapping("/get-server-msg")
    public JsonResult handleGetServerMsg(){
        List<ServerMessage> serverMsg = adminService.getServersMsg();
        if (serverMsg != null){
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.SUCCESS,
                    JsonResultStateCode.SUCCESS_DESC,
                    serverMsg
            );
        }else{
            return JsonResultFactory.buildFailureResult();
        }
    }

    @GetMapping("/get-filters")
    public JsonResult handleGetFilters() {
        List<Filter> res = adminService.getFilters();
        if (res != null && res.size() > 0) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.SUCCESS,
                    JsonResultStateCode.SUCCESS_DESC,
                    res
            );
        } else if (res != null) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.NOT_FOUND,
                    JsonResultStateCode.NOT_FOUND_DESC,
                    null
            );
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/add-blacklist")
    public JsonResult handleAddBlacklist(@RequestBody List<String> ipList) {

        Integer row = adminService.addFilter(ipList);
        if (row != null && row == ipList.size()) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("del-blacklist")
    public JsonResult handleDelBlacklist(@RequestBody List<String> ipList) {

        Integer row = adminService.deleteFilter(ipList);
        if (row != null && row == ipList.size()) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("update-mailbox-size")
    public JsonResult handleUpdateMailboxSize(@RequestBody List<MailBoxSize> mailBoxSizeList) {

        Integer row = adminService.updateMailBoxSize(mailBoxSizeList);
        if (row != null && row == mailBoxSizeList.size()) {
            return JsonResultFactory.buildSuccessResult();
        } else if (row != null) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED,
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC,
                    null
            );
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @GetMapping("/get-logs")
    public JsonResult getLogs() {

        List<Log> logs = adminService.getLogs();
        if (logs != null) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.SUCCESS,
                    JsonResultStateCode.SUCCESS_DESC,
                    logs
            );
        } else {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.FAILED,
                    JsonResultStateCode.FAILED_DESC,
                    null
            );
        }
    }

    @PostMapping("/del-logs")
    public JsonResult handleDelLog(@RequestBody List<Integer> logIdList) {

        Integer row = adminService.deleteLog(logIdList);
        if (row != null && row == logIdList.size()) {
            return JsonResultFactory.buildSuccessResult();
        } else if (row != null) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED,
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC,
                    null
            );
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("/send-group-mail")
    public JsonResult handleSendGroupMail(@RequestBody MassEmail massEmail) {

        Integer row = adminService.sendMails(massEmail);
        if (row != null && row == massEmail.getReceiverEmails().size()) {
            return JsonResultFactory.buildSuccessResult();
        } else if (row != null) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED,
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC,
                    null
            );
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("forbid-users")
    public JsonResult handleForbideUsers(@RequestBody ForbiddenUser forbiddenUser) {

        Integer row = adminService.filterUsers(forbiddenUser.getUsername(), forbiddenUser.getForbidden());

        if (row != null && row == forbiddenUser.getUsername().size()) {
            return JsonResultFactory.buildSuccessResult();
        } else if (row != null) {
            return JsonResultFactory.buildJsonResult(
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED,
                    JsonResultStateCode.OPERATION_IS_NOT_COMPLETED_DESC,
                    null
            );
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }
}
