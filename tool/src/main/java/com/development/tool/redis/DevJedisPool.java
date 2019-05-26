package com.development.tool.redis;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.util.HashSet;
import java.util.Set;

/**
 * @author chenjiajun
 * @title DevJedisPool
 * @project tools
 * @date 2019-04-17
 */
public class DevJedisPool extends Pool<Jedis> {

    // redis部署方式，单节点的话该值为"redis" 哨兵模式的话该值为"redis-sentinel"
    private String redisType;
    private GenericObjectPoolConfig poolConfig;
    private JedisConnectionConfig connectionPool;

    private JedisPool jedisPool;
    private JedisSentinelPool jedisSentinelPool;

    // 暂时不用，为了防止配置的混乱，需要在配管系统中配置redis.type选项
    public DevJedisPool(final GenericObjectPoolConfig poolConfig, final JedisConnectionConfig connectionPool) {
        // 根据connectionPool的参数判断redisType的值
        this(getRedisType(connectionPool), poolConfig, connectionPool);
    }

    // 推荐的构造函数
    public DevJedisPool(final String redisType, final GenericObjectPoolConfig poolConfig, final JedisConnectionConfig connectionPool) {
        this.redisType = redisType;
        this.poolConfig = poolConfig;
        this.connectionPool = connectionPool;
        if ("redis".equalsIgnoreCase(redisType)) {
            this.jedisPool = new JedisPool(poolConfig, connectionPool.getHost(), connectionPool.getPort(),
                    connectionPool.getTimeout(), connectionPool.getPassword(), connectionPool.getDatabase());
            this.jedisSentinelPool = null;
        } else if ("redis-sentinel".equalsIgnoreCase(redisType)) {
            this.jedisSentinelPool = new JedisSentinelPool(connectionPool.getMasterName(), getNodes(connectionPool.getSentinels()),
                    poolConfig, connectionPool.getTimeout(), connectionPool.getPassword(), connectionPool.getDatabase());
            this.jedisPool = null;
        }
    }


    @Override
    public Jedis getResource() {
        return (this.jedisPool != null) ? this.jedisPool.getResource() : this.jedisSentinelPool.getResource();
    }

    @Override
    @Deprecated
    public void returnBrokenResource(final Jedis resource) {
        if (this.jedisPool != null) {
            this.jedisPool.returnBrokenResource(resource);
        } else {
            this.jedisSentinelPool.returnBrokenResource(resource);
        }
    }

    @Override
    @Deprecated
    public void returnResource(final Jedis resource) {
        if (this.jedisPool != null) {
            this.jedisPool.returnResource(resource);
        } else {
            this.jedisSentinelPool.returnResource(resource);
        }
    }

    private static Set<String> getNodes(String sentinels) {
        Set<String> nodes = new HashSet<String>();
        String[] sentinelArray = StringUtils.split(sentinels, ",");
        for (String hostAndPort : sentinelArray) {
            nodes.add(hostAndPort.trim());
        }
        return nodes;
    }

    private static String getRedisType(final JedisConnectionConfig connectionPool) {
        String redisType = "redis-sentinel";
        if (StringUtils.isBlank(connectionPool.getMasterName()) || StringUtils.isBlank(connectionPool.getSentinels()) ||
                StringUtils.equalsIgnoreCase(connectionPool.getMasterName(), "/") ||
                StringUtils.equalsIgnoreCase(connectionPool.getSentinels(), "/")) {
            redisType = "redis";
        }
        return redisType;
    }

}
