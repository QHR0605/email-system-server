package com.example.server.util.command;

import org.springframework.util.StringUtils;

/**
 * @author 全鸿润
 */
public class CommandParse {

    public static String[] parseCommand(String command){
        String[] args;
        if (StringUtils.isEmpty(command) || command.startsWith(" ")){
            return null;
        }
        args = command.split("\\s+");
        return args;
    }
}
