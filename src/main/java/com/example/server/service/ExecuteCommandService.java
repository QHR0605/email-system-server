package com.example.server.service;

/**
 * @author 全鸿润
 */
public interface ExecuteCommandService {

    /**
     * 执行指令
     * @param args 指令参数
     * @return 执行状态码
     */
    Integer execute(String args);
}
