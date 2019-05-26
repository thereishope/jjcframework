package com.development.httpPlugin.metricsFile;
import com.development.httpPlugin.queue.FixedQueue;
import com.development.transfer.proxy.CommonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import java.util.LinkedList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author jiajunchen
 * @title MetricsFileHandler
 * @project httpPlugin
 */
public class MetricsLocalHandler implements CommonHandler {

    private static Logger logger = LoggerFactory.getLogger(MetricsLocalHandler.class);

    private static String METRICS_HTTP_QUEUE_TYPE = "local";

    //线程池
    ScheduledExecutorService executor = null;

    private ApplicationContext context;

    public MetricsLocalHandler(ApplicationContext context) {
        this.context = context;
    }



    /**校验开启Http监控分析数据刷入磁盘文件
      *@author jiajunchen
      */
    @Override
    public boolean allowance() throws Exception {
        return null != context.getEnvironment().getProperty("spring.rest.metricsUse", boolean.class) &&
                context.getEnvironment().getProperty("spring.rest.metricsUse", boolean.class)
                && METRICS_HTTP_QUEUE_TYPE.equals(context.getEnvironment().getProperty("spring.rest.strategy"));
    }

    /*定时一分钟提交监控分析数据
    *  分析数据刷入磁盘文件后，会清空队列，高并发场景下存在少量监控分析数据丢失，由于监控分析数据为非业务数据，可以允许丢失
    *  跨节点部署的项目可自行扩展
      *@author jiajunchen      *
      */
    @Override
    public void handle() throws Exception {
        LinkedList queue = FixedQueue.getLinkedDeque();
        getSchedulePool().scheduleAtFixedRate(new MetricsLocalThread(queue,context),1,1,TimeUnit.MINUTES);

    }

    public ScheduledExecutorService getSchedulePool(){
          executor = Executors.newScheduledThreadPool(1);
          return executor;
    }


}
