package com.example.server.util.json;

/**
 * 封装返回的Json数据的类
 * @author 全鸿润
 */
public class JsonResult {

    private final Integer state;
    private final String message;
    private final Object body;

    public JsonResult(Integer state, String message, Object body) {
        this.state = state;
        this.message = message;
        this.body = body;
    }

    public Integer getState() {
        return state;
    }

    public String getMessage() {
        return message;
    }

    public Object getBody() {
        return body;
    }
}
