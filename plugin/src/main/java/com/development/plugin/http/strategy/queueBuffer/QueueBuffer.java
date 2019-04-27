package com.development.plugin.http.strategy.queueBuffer;

import com.development.plugin.http.model.MetricsTimeModel;

import java.util.Deque;

/**后期通过实现该接口完成不同类型队列的适配
 * @author chenjiajun
 * @title QueueBuffer
 * @project plugin
 * @date 2019-04-24
 */
public interface QueueBuffer {

    public Object getQueue(String bufferType);

}
