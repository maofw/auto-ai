package com.ai.generator.enums;
/**
 * @author Administrator
 */
public enum ResultEnum {
    /**
     *
     */
    SUCCESS(200,"成功"),
    /**
     *
     */
    ERROR(500,"系统异常"),
    /**
     *
     */
    FAULT_TOLERANT(501,"服务暂时不可用"),
    /**
     *
     */
    FAIL(201,"操作失败"),
    /**
     *
     */
    NO_LOGIN(401,"未登陆"),
    LOGIN_OUT(402,"登陆过期"),
    /**
     *
     */
    NO_PARAM(202,"参数缺失"),
    /**
     *
     */
    PARAM_CHECK(203,"参数校验失败"),
    /**
     *
     */
    NO_DATA(204,"无数据"),
    /**
     *
     */
    FILE_MAX(206,"文件过大"),
    /**
     *
     */
    NO_FILE(207,"文件为空"),
    FREQUENTLY(209,"频繁请求"),
    ACCOUNT(211,"账号过期"),
    NO_TOURIST(213,"未授权，无法操作"),
    NO_SUPPORT(214,"请求不支持"),

    NO_IDCARD(215,"请先完善用户资料"),
    NO_VIP(216,"尚未订购"),
    /**
     *
     */
    TEST(999999,"TEST"),
    ;

    private Integer code;
    private String message;
    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     *
     * @return
     */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     */
    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
