package com.development.tool.redis;

/**
 * @author chenjiajun
 * @title JedisConnectionConfig
 * @project tools
 * @date 2019-04-17
 */
public class JedisConnectionConfig {

    private String masterName;
    private String sentinels;
    private String host;
    private int port;
    private int timeout;
    private String password;
    private int database;

    public JedisConnectionConfig() {

    }

    public JedisConnectionConfig(String masterName, String sentinels, String host, int port,
                                 int timeout, String password, int database) {
        this.masterName = masterName;
        this.sentinels = sentinels;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.password = password;
        this.database = database;
    }

    public String getMasterName() {
        return masterName;
    }

    public void setMasterName(String masterName) {
        this.masterName = masterName;
    }

    public String getSentinels() {
        return sentinels;
    }

    public void setSentinels(String sentinels) {
        this.sentinels = sentinels;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getDatabase() {
        return database;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}
