package com.development.transfer.serviceMethod;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * method缓存
 *
 * @author chenjiajun
 * @date 2019-01-13
 */
public class MethodCache {

    /**
     * 方法缓存容器
     */
    public static final Map<String, Method> cache = new ConcurrentHashMap<String, Method>();

    private MethodCache() {

    }

    ;

    /**
     * 获取内存中缓存的方法信息
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public static Method getMehod(String key) {
        return cache.get(key);
    }

    /**
     * 设置方法缓存
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public static void setMethod(String key, Method method) {
        cache.put(key, method);
    }
}
