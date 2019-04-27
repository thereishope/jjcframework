package com.development.plugin.http.queue;

import com.development.plugin.http.strategy.queueBuffer.QueueBuffer;

import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * 自定义队列
 *
 * @author chenjiajun
 * @title FixedQueue
 * @project request
 * @date 2019-03-16
 */
public class FixedQueue implements QueueBuffer{


    @Override
    public Object getQueue(String bufferType) {
        return FixedQueue.getConcurrentLinkedDeque();
    }

    private FixedQueue() {}

    ;



//    public int getSize(){
//        return fixedQueue.size();
//    }
//
//    public T get(int position) {
//        return fixedQueue.get(position);
//    }
//
//    public T getLast() {
//        return fixedQueue.getLast();
//    }
//
//    public T getFirst() {
//        return fixedQueue.getFirst();
//    }

    public static ConcurrentLinkedDeque getConcurrentLinkedDeque() {
        ConcurrentLinkedDeque linkedQueque = FixedConcurrentLinkedDeque.fixedQueue;
        return linkedQueque;
    }

    /**
     * 队列长度如果大于或等于限制长度，移除末位元素
     * 可以不用考虑线程安全方面的问题
     *
     * @author chenjiajun
     * @date 2019-03-16
     */
    public void add(Object t) {
        int fixed = 300;
        if (FixedConcurrentLinkedDeque.fixedQueue.size() >= fixed) {
            FixedConcurrentLinkedDeque.fixedQueue.poll();
        }
        FixedConcurrentLinkedDeque.fixedQueue.offer(t);
    }

    public static class FixedConcurrentLinkedDeque {

        public static ConcurrentLinkedDeque fixedQueue = new ConcurrentLinkedDeque();

    }
}
