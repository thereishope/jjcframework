package com.development.httpPlugin.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author jiajunchen
 * @title DetailMetrics
 * @project httpPlugin
 */
public class DetailMetrics implements Serializable {

    //最大请求时间
    private double max;

    //最小请求时间
    private double min;

    //平均请求时间
    private double avg;

    //明细
    private List<MetricsModel> metricsTimeModels;


    //队列中请求个数
    private int total;

    private String detail;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public double getMax() {
        return max;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public double getAvg() {
        return avg;
    }

    public void setAvg(double avg) {
        this.avg = avg;
    }

    public List<MetricsModel> getMetricsTimeModels() {
        return metricsTimeModels;
    }

    public void setMetricsTimeModels(List<MetricsModel> metricsTimeModels) {
        this.metricsTimeModels = metricsTimeModels;
    }
}
