package com.development.tool.redis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.Properties;

/**
 * @author chenjiajun
 * @title JedisUtils
 * @project tools
 * @date 2019-04-17
 */
public class JedisUtils {

    private static Properties properties;

    public static Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties properties) {
        JedisUtils.properties = properties;
    }

    private static final String OK_CODE = "OK";
    private static final String OK_MULTI_CODE = "+OK";

    private DevJedisPool pool = null;

    /**
     * 判断返回值是否ok.
     */
    public static boolean isStatusOk(String status) {
        return (status != null) && (OK_CODE.equals(status) || OK_MULTI_CODE.equals(status));
    }

    private static class RedisUtilHolder{
        private static JedisUtils instance = new JedisUtils();
    }

    public static JedisUtils getInstance() {
        return RedisUtilHolder.instance;
    }

    private static String getParameter(String key, String defaultValue) {
        String value = properties.getProperty(key, "");
        return StringUtils.isEmpty(value)?defaultValue:value;
    }

    public JedisTemplate getJedisTemplate() {
        try {
            if (pool == null) {
                String host = getParameter("redis.host", "localhost");
                int port = Integer.parseInt(getParameter("redis.port", "6379"));
                String password = getParameter("redis.password", "");
                int db = Integer.parseInt(getParameter("redis.database", "0"));
                int timeout = Integer.parseInt(getParameter("redis.timeout", "5000"));
                int poolSize = Integer.parseInt(getParameter("redis.pool.maxActive", "100"));
                int maxIdle = Integer.parseInt(getParameter("redis.pool.maxIdle", "20"));
                int minIdle = Integer.parseInt(getParameter("redis.pool.minIdle", "10"));
                String redisType = getParameter("redis.type", "redis");
                String sentinels = getParameter("redis.sentinels", "");
                String sentinelName = getParameter("redis.sentinelName", "def_master");
                GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
                poolConfig.setMaxTotal(poolSize);
                poolConfig.setMaxIdle(maxIdle);
                poolConfig.setMinIdle(minIdle);
                JedisConnectionConfig connectionConfig = new JedisConnectionConfig();
                if ("redis-sentinel".equals(redisType)) {
                    connectionConfig.setDatabase(db);
                    connectionConfig.setMasterName(sentinelName);
                    connectionConfig.setSentinels(sentinels);
                    if (StringUtils.isNotBlank(password)) {
                        connectionConfig.setPassword(password.trim());
                    }
                    connectionConfig.setTimeout(timeout);
                }
                else {
                    connectionConfig.setDatabase(db);
                    connectionConfig.setHost(host);
                    if (StringUtils.isNotBlank(password)) {
                        connectionConfig.setPassword(password.trim());
                    }
                    connectionConfig.setPort(port);
                    connectionConfig.setTimeout(timeout);
                }
                this.pool = new DevJedisPool(poolConfig, connectionConfig);
            }
            return new JedisTemplate(this.pool);
        }
        catch (Exception ex) {

        }

        return null;
    }

    /**
     * 在Pool以外强行销毁Jedis.
     */
    public static void destroyJedis(Jedis jedis) {
        if ((jedis != null) && jedis.isConnected()) {
            try {
                try {
                    jedis.quit();
                } catch (Exception e) {
                }
                jedis.disconnect();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Ping the jedis instance, return true is the result is PONG.
     */
    public static boolean ping(JedisPool pool) {
        JedisTemplate template = new JedisTemplate(pool);
        try {
            String result = template.execute(new JedisTemplate.JedisAction<String>() {
                @Override
                public String action(Jedis jedis) {
                    return jedis.ping();
                }
            });
            return (result != null) && result.equals("PONG");
        } catch (JedisException e) {
            return false;
        }
    }
}
