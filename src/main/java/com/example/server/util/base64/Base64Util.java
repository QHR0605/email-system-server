package com.example.server.util.base64;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

/**
 * @author 全鸿润
 */
public class Base64Util {

    private static final Encoder ENCODER = Base64.getEncoder();
    private static final Decoder DECODER = Base64.getDecoder();

    /**
     * 编码
     *
     * @param data 加密的内容
     * @return 加密后的字符串
     */
    public static String encodeByBase64(byte[] data) {

        return ENCODER.encodeToString(data);
    }

    /**
     * 解码
     *
     * @param data 解码的内容
     * @return 解码后的内容
     */
    public static String decodeByBase64(byte[] data) {
        return new String(DECODER.decode(data), StandardCharsets.UTF_8);
    }
}
