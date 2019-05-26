package com.development.dev.common.async;

/**
 * @author jiajunchen
 * @title AsyncEnum
 * @project fastdev
 * @date 2019-05-06
 */
public enum AsyncEnum {

    AYSNC_001("async001","操作日志记录")
    ;

    private String asyncCode;

    private String asyncName;

    AsyncEnum(String asyncCode, String asyncName) {
        this.asyncCode = asyncCode;
        this.asyncName = asyncName;
    }

    public String getAsyncCode() {
        return asyncCode;
    }

    public void setAsyncCode(String asyncCode) {
        this.asyncCode = asyncCode;
    }

    public String getAsyncName() {
        return asyncName;
    }

    public void setAsyncName(String asyncName) {
        this.asyncName = asyncName;
    }
}
