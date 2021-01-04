package com.ai.generator.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;

/**
 * @author Administrator
 */
public class Sha256Util {

    public static byte[] encrypt(String content, String password) {
        try {
            //"AES"：请求的密钥算法的标准名称
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            //256：密钥生成参数；securerandom：密钥生成器的随机源
            SecureRandom securerandom = new SecureRandom(tohash256Deal(password));
            kgen.init(256, securerandom);
            //生成秘密（对称）密钥
            SecretKey secretKey = kgen.generateKey();
            //返回基本编码格式的密钥
            byte[] enCodeFormat = secretKey.getEncoded();
            //根据给定的字节数组构造一个密钥。enCodeFormat：密钥内容；"AES"：与给定的密钥内容相关联的密钥算法的名称
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            //将提供程序添加到下一个可用位置
            Security.addProvider(new BouncyCastleProvider());
            //创建一个实现指定转换的 Cipher对象，该转换由指定的提供程序提供。
            //"AES/ECB/PKCS7Padding"：转换的名称；"BC"：提供程序的名称
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] byteContent = content.getBytes("utf-8");
            System.err.println("base64:"+new String(Base64.getEncoder().encode(byteContent)));
            byte[] cryptograph = cipher.doFinal(byteContent);
            return Base64.getEncoder().encode(cryptograph);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return null;
    }

    public static String decrypt(byte[] cryptograph, String password) {
        try {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom securerandom = new SecureRandom(tohash256Deal(password));
            kgen.init(256, securerandom);
            SecretKey secretKey = kgen.generateKey();
            byte[] enCodeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
            Security.addProvider(new BouncyCastleProvider());
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] content = cipher.doFinal(Base64.getDecoder().decode(cryptograph));
            return new String(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static byte[] tohash256Deal(String datastr) {
        try {
            MessageDigest digester=MessageDigest.getInstance("SHA-256");
            digester.update(datastr.getBytes());
            byte[] hex=digester.digest();
            return hex;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    /**
     * base64 解密
     * @param content
     * @return
     */
    public static String base64ToString(String content){
        return new String(Base64.getDecoder().decode(content));
    }

    /**
     * Base64加密
     * @return
     */
    public static String stringToBase64(String content){
        return new String(Base64.getEncoder().encode(content.getBytes()));
    }


}
