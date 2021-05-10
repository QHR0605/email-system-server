package com.example.server.service.impl;

import com.example.server.entity.SmtpResult;
import com.example.server.service.SmtpService;
import com.example.server.util.annotation.isAuth;
import com.example.server.util.annotation.isHello;
import com.example.server.util.annotation.isMail;
import com.example.server.util.annotation.isRcpt;
import com.example.server.util.command.CommandConstant;
import com.example.server.util.json.SmtpStateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 全鸿润
 */
@Service
public class SmtpServiceImpl implements SmtpService {

    @Autowired
    AuthServiceImpl authService;

    @Override
    public SmtpResult handleHelloCommand(String[] args) {
        SmtpResult result = null;
        if (args.length != 2){
            result = new SmtpResult(SmtpStateCode.COMMAND_ERROR,SmtpStateCode.COMMAND_ERROR_DESC);
        }else{
            result = new SmtpResult(SmtpStateCode.SUCCESS,SmtpStateCode.SUCCESS_DESC);
        }
        return result;
    }

    @Override
    @isHello
    public SmtpResult handleAuthCommand(String[] args) {
        SmtpResult result = null;
        if (args.length != 2){
            result = new SmtpResult(SmtpStateCode.COMMAND_ERROR,SmtpStateCode.COMMAND_ERROR_DESC);
        }else{
            String command = args[0]+args[1];
            if (!CommandConstant.AUTH_LOGIN.replaceAll(" ","").equals(command)){
                result = new SmtpResult(SmtpStateCode.COMMAND_ERROR,SmtpStateCode.COMMAND_ERROR_DESC);
            }else{
                result = new SmtpResult(SmtpStateCode.SUCCESS,SmtpStateCode.SUCCESS_DESC);
            }
        }
        return result;
    }

    @Override
    @isHello
    @isAuth
    public SmtpResult handleMailCommand(String[] args) {

        return null;
    }

    @Override
    @isHello
    @isAuth
    public SmtpResult handleRcptCommand(String[] args) {
        return null;
    }

    @Override
    @isHello
    @isAuth
    @isMail
    @isRcpt
    public SmtpResult handleDataCommand(String[] args) {
        return null;
    }

    @Override
    @isHello
    public SmtpResult handleResetCommand(String[] args) {
        return null;
    }

    @Override
    @isHello
    public SmtpResult handleQuitCommand(String[] args) {
        return null;
    }
}
