package com.development.httpPlugin.wrap;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author jiajunchen
 * @title HttpProperties
 * @project httpPlugin
 */
@ConfigurationProperties(prefix = "spring.rest"
)
public class HttpProperties {

    private int maxTotal;

    private int maxPreRoute;

    private int socketTimeout;

    private int connectionTimeout;

    private int requestTimeout;

    private String strategy;

    private String queueType;

    private String httpType;

    private boolean metricsUse;

    public boolean isMetricsUse() {
        return metricsUse;
    }

    public void setMetricsUse(boolean metricsUse) {
        this.metricsUse = metricsUse;
    }

    public String getHttpType() {
        return httpType;
    }

    public void setHttpType(String httpType) {
        this.httpType = httpType;
    }

    public String getQueueType() {
        return queueType;
    }

    public void setQueueType(String queueType) {
        this.queueType = queueType;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getMaxPreRoute() {
        return maxPreRoute;
    }

    public void setMaxPreRoute(int maxPreRoute) {
        this.maxPreRoute = maxPreRoute;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }

    public void setSocketTimeout(int socketTimeout) {
        this.socketTimeout = socketTimeout;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }
}
