package com.ai.generator.entity;


import java.io.Serializable;
import java.util.Date;

/**
 * 登陆缓存信息
 * @author Administrator
 */

public class TokenEntity implements Serializable {
    /**
     * 登陆id
     */
    private Long id;
    /**
     * 登陆时间
     */
    private Date logintime;
    /**
     * 登陆token
     */
    private String token;

    /**
     * 微信登陆sessionKey
     */
    private String sessionKey;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLogintime() {
        return logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

}

