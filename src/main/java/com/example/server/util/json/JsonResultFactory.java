package com.example.server.util.json;

import com.example.server.entity.SmtpResult;

/**
 * @author 全鸿润
 */
public class JsonResultFactory {

    public static JsonResult buildJsonResult(Integer stateCode , String message , Object body){
        return new JsonResult(stateCode,message,body);
    }
    public static JsonResult buildSuccessResult(SmtpResult smtpResult){
        return buildJsonResult(JsonResultStateCode.SUCCESS,"",smtpResult);
    }
    public static JsonResult buildSuccessResult(){
        return buildJsonResult(JsonResultStateCode.SUCCESS,"",null);
    }
    public static JsonResult buildSmtpCommandErrorResult(){
        return buildSuccessResult(new SmtpResult(SmtpStateCode.COMMAND_ERROR,""));
    }
}
