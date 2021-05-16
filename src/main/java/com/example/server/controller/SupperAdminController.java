package com.example.server.controller;

import com.example.server.ServerApplication;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerPortMsg;
import com.example.server.dto.ServerStateMsg;
import com.example.server.dto.UserNameAndType;
import com.example.server.service.SupperAdminService;
import com.example.server.service.impl.SupperAdminImpl;
import com.example.server.util.annotation.IsSupperAdmin;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * @author 全鸿润
 */
@CrossOrigin
@RestController
@RequestMapping(value = "/supper")
public class SupperAdminController {

    private final SupperAdminService supperAdminService = SpringContextConfig.getBean(SupperAdminImpl.class);

    @PostMapping("/auth")
    @IsSupperAdmin
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
    @IsSupperAdmin
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
    public JsonResult handleChangeServerState(@RequestBody ServerStateMsg serverState) {

        System.out.println(serverState.getServerType());
        Integer state = supperAdminService.changeServerState(serverState);
        JsonResult res = JsonResultFactory.buildSuccessResult();
        if (state != null && state == 1) {
            try {
                if (serverState.getServerState()) {
                    if (serverState.getServerType() == 0) {
                        if (ServerApplication.smtpServer.isInterrupted()) {
                            ServerApplication.smtpServer.start();
                        }
                    } else if (serverState.getServerType() == 1) {
                        if (ServerApplication.pop3Server.isInterrupted()) {
                            ServerApplication.pop3Server.start();
                        }
                    } else {
                        return JsonResultFactory.buildJsonResult(JsonResultStateCode.FAILED, "没有该服务", null);
                    }
                    return res;
                } else {
                    if (serverState.getServerType() == 0) {
                        if (!ServerApplication.smtpServer.isInterrupted()) {
                            ServerApplication.smtpServer.stopSmtpServer();
                        }
                    } else if (serverState.getServerType() == 1) {
                        if (!ServerApplication.pop3Server.isInterrupted()) {
                            ServerApplication.pop3Server.stopPop3Server();
                        }
                    } else {
                        return JsonResultFactory.buildJsonResult(JsonResultStateCode.FAILED, "没有该服务", null);
                    }
                    return res;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return JsonResultFactory.buildFailureResult();
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }

    }

    @PostMapping("/change-server-port")
    public JsonResult handleChangeServerPort(@RequestBody ServerPortMsg msg) {

        //修改端口号
        Integer row = supperAdminService.changeServerPort(msg);
        JsonResult res = JsonResultFactory.buildSuccessResult();
        JsonResult res1 = JsonResultFactory.buildFailureResult();
        if (row != null && row == 1) {
            //暂停当前端口
            Integer r1 = supperAdminService.stopServer(msg);
            if (r1 != null && r1.equals(JsonResultStateCode.SUCCESS)) {
                //重启新的端口
                Integer r2 = supperAdminService.restartServer(msg);
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
}
