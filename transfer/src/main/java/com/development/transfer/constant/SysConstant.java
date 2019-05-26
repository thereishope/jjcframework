package com.development.transfer.constant;

/**
 * @author jiajunchen
 * @title SysConstant
 * @project transfer
 * @date 2019-01-08
 */
public enum SysConstant {
    SUCCESS("0000","操作成功"),
    FAIL("7777","操作失败"),
    ERROR("9999","系统异常");

    private String code;
    private String msg;

    SysConstant(String code, String msg) {
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
}
