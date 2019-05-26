package com.development.httpPlugin.strategy;
import com.development.httpPlugin.model.MetricsModel;
import com.development.httpPlugin.wrap.RestTemplateWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.ServiceLoader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 策略模式设置http监控数据
 * 并异步执行监控数据进入队列
 *
 * @author jiajunchen
 * @title MetricsTimeStrategy
 * @project httpPlugin
 */
public class MetricsStrategy implements ClientHttpRequestInterceptor {


    private static Logger logger = LoggerFactory.getLogger(MetricsStrategy.class);

    static ExecutorService pool = Executors.newFixedThreadPool(2);



    /**
     * http拦截器
     * 设置http metrics数据
     *
     * @author jiajunchen
     * @date 2019-04-24
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        MetricsModel metricsTimeModel = new MetricsModel();
        metricsTimeModel.setBeginTime(new Date());
        ClientHttpResponse response;
        try {
            response = execution.execute(request, body);
        } finally {

        }
        metricsTimeModel.setEndTime(new Date());
        metricsTimeModel.setStatus(response.getStatusCode().value());
        metricsTimeModel.setUrl(request.getURI().getPath());
        metricsTimeModel.setCostTime(metricsTimeModel.getEndTime().
                getTime()-metricsTimeModel.getBeginTime().getTime());
        metricsTimeModel.setQueueType("");
        pool.execute(new AsyncMetricsExcute(metricsTimeModel));
        return response;
    }

//    /**
//     * 处理MetricsTimeModel数据
//     *
//     * @author jiajunchen
//     */
//    public void handleMetrics(MetricsTimeModel metricsTimeModel) {
//        try {
//            pool.execute(new AsyncMetricsExcute(metricsTimeModel));
//        } catch (Exception e) {
//            logger.error("MetricsStrategy[handleMetrics]发生异常", e);
//        }
//
//    }

    /**
     * 异步定长线程处理短任务
     *
     * @author jiajunchen
     */
    class AsyncMetricsExcute implements Runnable {

        private MetricsModel metricsTimeModel;

        public AsyncMetricsExcute(MetricsModel metricsTimeModel) {
            this.metricsTimeModel = metricsTimeModel;
        }

        @Override
        public void run() {
          String strategyName  = RestTemplateWrap.strategyMap.get("strategy");
            if(!StringUtils.isEmpty(strategyName)){
                ServiceLoader<Strategy> serviceLoader = ServiceLoader.load(Strategy.class);
                for (Strategy strategy : serviceLoader) {
                    if (strategyName.equals(strategy.getStrategy())) {
                        strategy.excute(metricsTimeModel);
                    }
                }
            }

        }
    }

}
