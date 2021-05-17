package com.example.server.controller;

import com.example.server.config.SpringContextConfig;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.dto.UserNameAndType;
import com.example.server.entity.Filter;
import com.example.server.entity.User;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.SupperAdminService;
import com.example.server.service.impl.SupperAdminImpl;
import com.example.server.util.annotation.IsAdmin;
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
@CrossOrigin
@RestController
@RequestMapping(value = "/supper")
public class SupperAdminController {

    private final SupperAdminService supperAdminService = SpringContextConfig.getBean(SupperAdminImpl.class);


    @GetMapping("/get-users")
    @IsAdmin
    public JsonResult handleGetUsers() {
        List<User> userList = supperAdminService.getAllUsers();
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
    @IsAdmin
    public JsonResult handleAuthorize(@RequestBody List<UserNameAndType> userNameAndTypes) {

        if (userNameAndTypes != null) {
            List<String> usernames = new LinkedList<>();
            for (UserNameAndType user : userNameAndTypes
            ) {
                usernames.add(user.getUsername());
            }
            Integer type = userNameAndTypes.get(0).getAccountType();
            Integer rows = supperAdminService.auth(usernames, type);
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
    @IsAdmin
    public JsonResult handleCreate(@RequestBody NewUserMessage userMessage) {

        Integer rows;
        rows = supperAdminService.createUser(userMessage);
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
    @IsAdmin
    public JsonResult handleChangeServerState(@RequestBody ServerStateMsg serverState) {
        Integer state = supperAdminService.changeServerState(serverState);
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
                supperAdminService.restartServer(msg);
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
                supperAdminService.stopServer(msg);
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
        return res;
    }

    @PostMapping("/change-server-port")
    @IsAdmin
    public JsonResult handleChangeServerPort(@RequestBody ServerPortMsg msg) {

        //修改端口号
        Integer row = supperAdminService.changeServerPort(msg);
        JsonResult res = JsonResultFactory.buildSuccessResult();
        JsonResult res1 = JsonResultFactory.buildFailureResult();
        if (row != null && row == 1) {
            //暂停当前端口
            Integer r1 = supperAdminService.stopServer(msg);
            if (r1 != null && r1.equals(JsonResultStateCode.SUCCESS)) {
                Integer r2 = JsonResultStateCode.SUCCESS;
                if (msg.getServerType() == 0) {
                    if (!SmtpServer.isShutDown()) {
                        r2 = supperAdminService.restartServer(msg);
                    }
                } else if (msg.getServerType() == 1) {
                    if (!Pop3Server.isShutDown()) {
                        r2 = supperAdminService.restartServer(msg);
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

    @GetMapping("/get-filters")
    @IsAdmin
    public JsonResult handleGetFilters() {
        List<Filter> res = supperAdminService.getFilters();
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
    @IsAdmin
    public JsonResult handleAddBlacklist(@RequestBody Filter filter) {

        Integer row = supperAdminService.addFilter(filter);
        if (row != null && row == 1) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }

    @PostMapping("del-blacklist")
    @IsAdmin
    public JsonResult handleDelBlacklist(@RequestBody Filter filter) {

        Integer row = supperAdminService.deleteFilter(filter);
        if (row != null && row == 1) {
            return JsonResultFactory.buildSuccessResult();
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }
}
