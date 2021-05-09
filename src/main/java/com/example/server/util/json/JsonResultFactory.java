package com.example.server.util.json;

/**
 * @author 全鸿润
 */
public class JsonResultFactory {

    static JsonResult buildJsonResult(Integer stateCode , String message , Object body){
        return new JsonResult(stateCode,message,body);
    }
    static JsonResult buildSmtpReadyResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.READY);
    }
    static JsonResult buildSmtpSuccessResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.SUCCESS);
    }
    static JsonResult buildSmtpAuthSuccessResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.AUTH_SUCCESS);
    }
    static JsonResult buildSmtpProcessingResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.PROCESSING);
    }
    static JsonResult buildSmtpSendingResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.SENDING);
    }
    static JsonResult buildSmtpCommandErrorResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.COMMAND_ERROR);
    }
    static JsonResult buildSmtpInterruptedResult(){
        return new JsonResult(JsonResultStateCode.SUCCESS,null,SmtpStateCode.INTERRUPTED);
    }
}
