package com.development.plugin.http.sched;
import com.development.plugin.http.queue.FixedQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * @author chenjiajun
 * @title ClearQueueSched
 * @project plugins
 * @date 2019-04-25
 */
@Component
public class ClearQueueSched {

    private static Logger logger = LoggerFactory.getLogger(ClearQueueSched.class);

    private static final int QUEUE_SIZE = 30000;

    @Scheduled(fixedDelay = 90000)
    public void clearQueue(){
        ConcurrentLinkedDeque queue  = FixedQueue.getConcurrentLinkedDeque();
        if(queue.size()>0 && queue.size()>=QUEUE_SIZE){
            queue.clear();
        }
    }

}
