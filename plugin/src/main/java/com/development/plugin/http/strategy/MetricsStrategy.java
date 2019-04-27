package com.development.plugin.http.strategy;

import com.development.plugin.http.model.MetricsTimeModel;
import com.development.plugin.http.wrap.RestTemplateWrap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 策略模式设置http监控数据
 * 并异步执行监控数据进入队列
 *
 * @author chenjiajun
 * @title MetricsTimeStrategy
 * @project request
 * @date 2019-03-16
 */
public class MetricsStrategy implements ClientHttpRequestInterceptor {

    private static final ConcurrentHashMap<String, ServiceLoader> serviceLoaderMap = new ConcurrentHashMap<String, ServiceLoader>();

    private static Logger logger = LoggerFactory.getLogger(MetricsStrategy.class);

    private static String SERVICE_LOADER_KEY = "service_load_key";

    /**
     * http拦截器
     * 设置http metrics数据
     *
     * @author chenjiajun
     * @date 2019-04-24
     */
    @Override
    public ClientHttpResponse intercept(HttpRequest request,
                                        byte[] body,
                                        ClientHttpRequestExecution execution) throws IOException {
        MetricsTimeModel metricsTimeModel = new MetricsTimeModel();
        String urlPath = request.getURI().getPath();
        metricsTimeModel.setUrl(urlPath);
        logger.info("MetricsStrategy[intercept]获取到的请求地址:" + urlPath + "");
        metricsTimeModel.setBeginTime(new Date());
        ClientHttpResponse response = execution.execute(request, body);
        logger.info("MetricsStrategy[intercept]获取到的请求地址:" + urlPath + "," +
                "返回status[" + response.getStatusCode().toString() + "]");
        long end = System.currentTimeMillis();
        metricsTimeModel.setEndTime(new Date());
        metricsTimeModel.setStatus(response.getStatusCode().value());
        metricsTimeModel.setCostTime(metricsTimeModel.getEndTime().
                getTime()-metricsTimeModel.getBeginTime().getTime());
        metricsTimeModel.setQueueType(String.valueOf(RestTemplateWrap.strategyMap.get("queueType")));
        handleMetrics(metricsTimeModel);
        return response;
    }

    /**
     * 处理MetricsTimeModel数据
     *
     * @author chenjiajun
     * @date 2019-03-16
     */
    public void handleMetrics(MetricsTimeModel metricsTimeModel) {
        try {
            ServiceLoader<Strategy> serviceLoader = ServiceLoader.load(Strategy.class);
            if (null != serviceLoader && !serviceLoaderMap.containsKey(SERVICE_LOADER_KEY)) {
                serviceLoaderMap.put(SERVICE_LOADER_KEY, serviceLoader);
            }
            ExecutorService pool = Executors.newFixedThreadPool(2);
            pool.execute(new AsyncMetricsExcute(metricsTimeModel));
        } catch (Exception e) {
            logger.error("MetricsStrategy[handleMetrics]发生异常", e);
        }

    }

    /**
     * 异步定长线程处理短任务
     *
     * @author chenjiajun
     * @date 2019-04-24
     */
    class AsyncMetricsExcute implements Runnable {

        private MetricsTimeModel metricsTimeModel;

        public AsyncMetricsExcute(MetricsTimeModel metricsTimeModel) {
            this.metricsTimeModel = metricsTimeModel;
        }

        @Override
        public void run() {
            logger.info("MetricsStrategy[handleMetrics]获取到的strategy[" + RestTemplateWrap.
                    strategyMap.get("strategy") + "]");
            Object o = RestTemplateWrap.strategyMap.get("strategy");
            String strategyName = null != o ? String.valueOf(o) : "detail";
            ServiceLoader<Strategy> serviceLoader = serviceLoaderMap.get(SERVICE_LOADER_KEY);
            for (Strategy strategy : serviceLoader) {
                logger.info("MetricsStrategy[handleMetrics]==" + strategy.getStrategy());
                if (strategyName.equals(strategy.getStrategy())) {
                    strategy.excute(metricsTimeModel);
                }
            }
        }
    }

}
