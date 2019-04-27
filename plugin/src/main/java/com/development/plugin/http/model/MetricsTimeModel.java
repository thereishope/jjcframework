package com.development.plugin.http.model;

import java.util.Date;

/**
 * @author chenjiajun
 * @title MetricsTimeModel
 * @project request
 * @date 2019-03-16
 */
public class MetricsTimeModel {

    //请求地址
    private String url;

    //耗时
    private long costTime;
    //开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    //状态


    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    private int status;

    //队列类型
    private String queueType;

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public long getCostTime() {
        return costTime;
    }

    public void setCostTime(long costTime) {
        this.costTime = costTime;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
