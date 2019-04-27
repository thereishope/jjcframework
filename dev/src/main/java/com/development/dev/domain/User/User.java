package com.development.dev.domain.User;

import java.io.Serializable;

/**
 * @author chenjiajun
 * @title User
 * @project dev
 * @date 2019-01-04
 */
public class User implements Serializable {

    private String name;

    private String pwd;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
