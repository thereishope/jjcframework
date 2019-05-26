package com.development.transfer.exception;

/**
 * 异常封装类
 *
 * @author jiajunchen
 * @date 2018-12-29
 */
public class BuisException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String code;

    private String msg;

    public BuisException(String code, String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BuisException() {
        super();
    }


}
