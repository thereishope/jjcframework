package com.development.plugin.http.strategy.queueBuffer;

import com.development.plugin.http.queue.FixedQueue;

/**
 * @author chenjiajun
 * @title QueueBufferFactory
 * @project plugin
 * @date 2019-04-24
 */
public class QueueBufferFactory {

    public static final String QUEUE_BUFFER_STORE_STRATEGY_MEM = "memory";

    private static final String QUEUE_BUFFER_STORE_STRATEGY_REDIS = "redis";

    public static Object createQueue(String storeType){

        switch (storeType){
            case QUEUE_BUFFER_STORE_STRATEGY_MEM :
                return FixedQueue.getConcurrentLinkedDeque();
            case QUEUE_BUFFER_STORE_STRATEGY_REDIS:
                return null;
                default:
                    return FixedQueue.getConcurrentLinkedDeque();
        }
    }
}
