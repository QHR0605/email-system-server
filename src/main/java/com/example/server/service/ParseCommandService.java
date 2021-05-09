package com.example.server.service;

/**
 * @author 全鸿润
 */
public interface ParseCommandService {

    /**
     * 获取指令名
     * @param command 指令
     * @return 指令名
     */
    String getName(String command);

    /**
     * 获取指令参数
     * @param command 指令
     * @return 指令参数数组
     */
    String[] getArgs(String command);
}
