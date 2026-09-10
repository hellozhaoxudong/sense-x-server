package com.sense.app.core.utils;


import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

public class PasswordUtils {
    // 前后端传输用户名密码时的RSA加密 公私钥对，前端使用公钥加密，后端使用私钥解密
    public static String LOGIN_RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDaIfQewHOtybaYLeZ1cvWyI67o6qCtxQR4GW2u6eS3VAxOPrGCAn4JD5VPAIU+bGFLxJecBt05aIu8HXpuokEBb8OvaLm0cOULKr3zWYSwT6QloSwyv/03QGLXF6prKeXHCCsSNHxcxGJrinRswMTG6rmSTM9E90nZfyR4Eqe8GQIDAQAB";
    public static String LOGIN_RSA_PRIVATE = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANoh9B7Ac63Jtpgt5nVy9bIjrujqoK3FBHgZba7p5LdUDE4+sYICfgkPlU8AhT5sYUvEl5wG3Tloi7wdem6iQQFvw69oubRw5QsqvfNZhLBPpCWhLDK//TdAYtcXqmsp5ccIKxI0fFzEYmuKdGzAxMbquZJMz0T3Sdl/JHgSp7wZAgMBAAECgYAZzu6dPNPy+ey6sKOVvdZID2TAdhIwvSC8PPX3PgErl6wlDdHeugDDfa6CvGMHcbdJSadOdJl0E2SFC0/TCyt5khF5qxC+OT8vLw/kacJ54FApw39BPRrgXgHmacWlrP4re59MWOuJK5S+mfH72lXd7NgJJnvwz2VhYuEStO7fAQJBAPacdBM1NmuL/eb4J46/x5YJifxxIjUMSCeIiMzgYijP+3HLXGqnW4YkCBC6tCHMJa5u7mzp8BsujqVPkGS1lykCQQDib/CiwnPJWIJlBDZPhuskX7TP8nCa6q2yoPo/CKZ3N/u6/rZgHwWhW7M3rPvlPKHwOc104/fYRUMM/XYbX0txAkEAjodL2tIVWsD1BKmFi04x1vg6ZMkqIQixJxAVKHvCn53c0B7dkdKZ5gQrGSge9a2cVZ1NdWsbV0poLQi4BkyhSQJBALeGXLW9/tRZDU5MbKxmQJz5iPXlnIii3mdh+5EtNKZ51GhJ24zqd8jBJu9gH8U7MFEguDoeLpm2AVM89wXWZbECQQDTkFQM3pDq0e5yAx/PBltxZx84CkmPLK7RVv7jV9xB7s5xb1+lO5LYmI/Y6M1mPBUoRmJYm1BDIo2Op4l0fs2p";

    // 用户密码盐值, 最终存储的用户密码为: md5(md5(明文密码+盐1)+盐2)
    public static String PASSWORD_SALT1 = "eh94DudLSZUbHc89RNjw";
    public static String PASSWORD_SALT2 = "PQuVMeH6Z75m5CxM8yJm";


    /**
     * 获取加密后的用户密码
     * @param password  用户明文密码
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public static String encryptPassword(String password){
        return SecureUtil.md5(SecureUtil.md5(password+PASSWORD_SALT1)+PASSWORD_SALT2);
    }


    /**
     * 使用私钥解密字符串
     * @param encryptStr    加密后的字符串
     * @author sense-x
     * @date 2026-01-01 23:00:00
     */
    public static String decryptRsa(String encryptStr){
        RSA rsa = SecureUtil.rsa(LOGIN_RSA_PRIVATE, null);
        String de = rsa.decryptStr(encryptStr, KeyType.PrivateKey);
        return de;
    }

}
