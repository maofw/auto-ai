package com.ai.generator.model.request;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class WxLoginRequest implements Serializable {
    /**
     * 登陆凭证
     */
    private String code;
    /**
     * 用户非敏感信息
     */
    private String rawData;
    /**
     * 签名
     */
    private String signature;
    /**
     * 用户敏感信息
     */
    private String encrypteData;
    /**
     * 解密算法的向量
     */
    private String iv;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRawData() {
        return rawData;
    }

    public void setRawData(String rawData) {
        this.rawData = rawData;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getEncrypteData() {
        return encrypteData;
    }

    public void setEncrypteData(String encrypteData) {
        this.encrypteData = encrypteData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }


}
