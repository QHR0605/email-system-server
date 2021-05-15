package com.example.server.controller;

import com.example.server.ServerApplication;
import com.example.server.config.SpringContextConfig;
import com.example.server.dto.NewUserMessage;
import com.example.server.dto.ServerState;
import com.example.server.dto.UserNameAndType;
import com.example.server.server.Pop3Server;
import com.example.server.server.SmtpServer;
import com.example.server.service.SupperAdminService;
import com.example.server.service.impl.SupperAdminImpl;
import com.example.server.util.annotation.IsSupperAdmin;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.JsonResultStateCode;
import org.springframework.web.bind.annotation.*;

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
    @IsSupperAdmin
    public JsonResult handleStopSmtp(@RequestBody ServerState serverState) {

        if ("POP3".equals(serverState.getServerName())) {
            if (serverState.getState()) {
                if (ServerApplication.pop3Server.isShutDown()) {
                    ServerApplication.pop3Server = new Pop3Server();
                    ServerApplication.pop3Server.start();
                    return JsonResultFactory.buildSuccessResult();
                } else {
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.OPERATION_IS_NOT_COMPLETED, "POP3服务器已开启", null);
                }
            } else {
                ServerApplication.pop3Server.stopPop3Server();
                return JsonResultFactory.buildSuccessResult();
            }
        } else if ("SMTP".equals(serverState.getServerName())) {

            if (serverState.getState()) {
                if (ServerApplication.smtpServer.isShutDown()) {
                    ServerApplication.smtpServer = new SmtpServer();
                    ServerApplication.smtpServer.start();
                    return JsonResultFactory.buildSuccessResult();
                } else {
                    return JsonResultFactory.buildJsonResult(JsonResultStateCode.OPERATION_IS_NOT_COMPLETED, "SMTP服务器已开启", null);
                }
            } else {
                ServerApplication.smtpServer.stopSmtpServer();
                return JsonResultFactory.buildSuccessResult();
            }
        } else {
            return JsonResultFactory.buildFailureResult();
        }
    }
}
