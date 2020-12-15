package com.ai.generator.model.request;

import java.io.Serializable;

public class LoginBeanRequest implements Serializable {
    private String account;
    private String password;
    private String captcha;
    private String t ;

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }
}
