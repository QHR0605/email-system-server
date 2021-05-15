package com.example.server.util.json;

/**
 * 状态码及其状态信息
 *
 * @author 全鸿润
 */
public class JsonResultStateCode {

    static public Integer SUCCESS = 200;
    static public String SUCCESS_DESC = "success";

    static public Integer FAILED = 400;
    static public String FAILED_DESC = "failed";

    public static Integer USERNAME_WRONG = 410;
    public static String USERNAME_WRONG_DESC = "用户名错误";

    public static Integer USERNAME_IS_EXITED = 413;
    public static String USERNAME_IS_EXITED_DESC = "用户名已存在";

    static public Integer UNKNOWN_ERROR = 404;
    static public String UNKNOWN_ERROR_DESC = "unknown error";

    public static Integer PASSWORD_WRONG = 414;
    public static String PASSWORD_WRONG_DESC = "密码错误";

    public static Integer OPERATION_IS_NOT_COMPLETED = 490;
    public static String OPERATION_IS_NOT_COMPLETED_DESC = "操作做未全部完成";

    public static Integer NOT_FOUND = 403;
    public static String NOT_FOUND_DESC = "not found";
}
