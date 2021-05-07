package com.example.server.util;

import java.util.Base64;
import java.util.Base64.Encoder;

/**
 * @author 全鸿润
 */
public class Base64Util {

    private static final Encoder ENCODER = Base64.getEncoder();

    /**
     * 编码
     * @param data 加密的内容
     * @return 加密后的字符串
     */
    public static String encodeByBase64(byte[] data){

        return ENCODER.encodeToString(data);
    }

    /**
     * 解码
     * @param data 解码的内容
     * @return 解码后的内容
     */
    public static String decodeByBase64(byte[] data){
        return ENCODER.encodeToString(data);
    }
}
