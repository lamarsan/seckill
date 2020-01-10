package com.lamarsan.seckill.utils;

import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * className: MD5Util
 * description: TODO
 *
 * @author lamar
 * @version 1.0
 * @date 2020/1/4 16:29
 */
public class MD5Util {
    public static String EncodeByMd5(String string) {
        // 确定计算方法
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            BASE64Encoder base64Encoder = new BASE64Encoder();
            // 加密字符串
            String newString = base64Encoder.encode(md5.digest(string.getBytes("utf-8")));
            return newString;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
