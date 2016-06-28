package com.thxy.mobileguard.utils;

/**
 * Created by dongwei on 2016/6/27.
 */
public class EncryptTools {
    public static String encrypt(int seed, String str){
        byte[] bytes = str.getBytes();
        for (int i =0; i < bytes.length; i++){
            bytes[i] ^= seed;
        }
        return new String(bytes);
    }

    public static String decryption(int seed, String str){
        byte[] bytes = str.getBytes();
        for (int i =0; i < bytes.length; i++){
            bytes[i] ^= seed;
        }
        return new String(bytes);
    }
}
