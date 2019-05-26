package com.development.httpPlugin.queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 自定义队列
 *
 * @author jiajunchen
 * @title FixedQueue
 * @project httpPlugin
 */
public class FixedQueue {

    private static Logger logger = LoggerFactory.getLogger(FixedQueue.class);

    private static Lock lock = new ReentrantLock();


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

    public static LinkedList getLinkedDeque() {
        LinkedList linkedQueque = FixedLinkedDeque.fixedQueue;
        return linkedQueque;
    }

    /**
     * 队列长度如果大于或等于限制长度，移除末位元素
     * 可以不用考虑线程安全方面的问题
     *
     * @author jiajunchen
     */
//    public static void add(Object t) {
//        try {
////            lock.lock();
//            int fixed = 300;
//            int queueSize = FixedConcurrentLinkedDeque.fixedQueue.size();
//            logger.info("FixedQueue 长度["+queueSize+"]");
//            if (queueSize >= fixed) {
//                FixedConcurrentLinkedDeque.fixedQueue.poll();
//            }
//            FixedConcurrentLinkedDeque.fixedQueue.offer(t);
//        } finally {
////            lock.unlock();
//        }
//
//    }

    public static class FixedLinkedDeque {

        public static LinkedList fixedQueue = new LinkedList();

    }

    }

