package com.example.server.controller;

import com.example.server.entity.SmtpResult;
import com.example.server.service.SmtpService;
import com.example.server.util.command.CommandConstant;
import com.example.server.util.commandParser.CommandParse;
import com.example.server.util.json.JsonResult;
import com.example.server.util.json.JsonResultFactory;
import com.example.server.util.json.SmtpStateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 全鸿润
 * 可删除
 */
@RestController
public class SmtpController {

    @Autowired
    private SmtpService smtpService;

    @PostMapping("/")
    public JsonResult handleSmtpCommand(@RequestBody String command) {
        String[] args = CommandParse.parseCommand(command);
        JsonResult result = null;
        Integer stateCode;
        SmtpResult smtpResult;
        if (args == null) {
            return JsonResultFactory.buildSmtpCommandErrorResult();
        }
        if (CommandConstant.HELO.equals(args[0])) {
            smtpResult = smtpService.handleHelloCommand(args);
            if (smtpResult != null) {
                if (smtpResult.getState() == (SmtpStateCode.SUCCESS)) {
                    result = JsonResultFactory.buildSuccessResult(smtpResult);
                } else if (smtpResult.getState() == (SmtpStateCode.COMMAND_ERROR)) {
                    result = JsonResultFactory.buildSmtpCommandErrorResult();
                }
            }
        } else if (CommandConstant.AUTH_LOGIN_PREFIX.equals(args[0])) {
            smtpResult = smtpService.handleAuthCommand(args);
            if (smtpResult != null) {
                if (smtpResult.getState() == (SmtpStateCode.SUCCESS)) {
                    result = JsonResultFactory.buildSuccessResult(smtpResult);
                } else if (smtpResult.getState() == (SmtpStateCode.COMMAND_ERROR)) {
                    result = JsonResultFactory.buildSmtpCommandErrorResult();
                } else if (smtpResult.getState() == (SmtpStateCode.LOGICAL_ERROR)) {
                    result = JsonResultFactory.buildSuccessResult(smtpResult);
                }
            }
        } else if (CommandConstant.MAIL_FROM_PREFIX.equals(args[0])) {

        } else if (CommandConstant.RCPT_TO_PREFIX.equals(args[0])) {

        } else if (CommandConstant.DATA.equals(args[0])) {

        } else if (CommandConstant.REST.equals(args[0])) {

        } else if (CommandConstant.QUIT.equals(args[0])) {

        } else {
            result = JsonResultFactory.buildSmtpCommandErrorResult();
        }

        return result;
    }
}
