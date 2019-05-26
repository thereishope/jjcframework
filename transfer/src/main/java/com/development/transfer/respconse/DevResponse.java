package com.development.transfer.respconse;

import java.io.Serializable;

/**
 * 响应封装
 *
 * @author jiajunchen
 * @title DevResponse
 * @project transfer
 * @date 2018-12-03
 */
public class DevResponse implements Serializable {

    //返回标志，true|false
    private boolean flag;

    //返回码
    private String resCode;

    //返回信息
    private String resMsg;

    //返回内容
    private Object data;

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResMsg() {
        return resMsg;
    }

    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public DevResponse(boolean flag, String resCode, String resMsg, Object data) {
        this.flag = flag;
        this.resCode = resCode;
        this.resMsg = resMsg;
        this.data = data;
    }

    public DevResponse() {
    }
}
