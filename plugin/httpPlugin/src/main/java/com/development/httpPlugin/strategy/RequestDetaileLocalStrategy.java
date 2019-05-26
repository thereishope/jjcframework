package com.development.httpPlugin.strategy;

import com.development.httpPlugin.model.DetailMetrics;
import com.development.httpPlugin.model.MetricsModel;
import com.development.httpPlugin.queue.FixedQueue;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 明细模式策略
 *
 * @author jiajunchen
 * @title RequestDetaileLocalStrategy
 * @project httpPlugin
 */
public class RequestDetaileLocalStrategy implements Strategy {

    private static Logger logger = LoggerFactory.getLogger(RequestDetaileLocalStrategy.class);

    public RequestDetaileLocalStrategy() {
    }

    /**
     * @author jiajunchen
     * @date 2019-04-22
     */
    @Override
    public String getStrategy() {
        return "local";
    }

    /**
     * 执行数据存储策略
     *
     * @author jiajunchen
     */
    @Override
    public <T> void excute(T t) {
        MetricsModel metricsTimeModel = (MetricsModel) t;
        LinkedList queue = FixedQueue.getLinkedDeque();
        queue.add(metricsTimeModel);
    }

    /**
     * 明细模式，展示最近请求耗时情况
     * 以及高于平均请求时间的列表和耗时区间个数
     * @author jiajunchen
     */
    @Override
    public String getAnalysisMetrics(Object o) throws Exception {

//        try {
//            return MetricsFileThread.CACHE_METRICS.get(MetricsFileThread.CACHE_METRICS_KEY);
//        } finally {
//            MetricsFileThread.CACHE_METRICS.clear();
//        }

        return getMetrics((LinkedList<MetricsModel>)o);

    }

    private String getMetrics( LinkedList<MetricsModel> linkedDeque){
        String detaileMetrics = null;
        try {
            StringBuffer buffer = new StringBuffer();
            if (null != linkedDeque && linkedDeque.size() > 0) {
                DetailMetrics metrics = new DetailMetrics();
                if (null != linkedDeque && linkedDeque.size() > 0) {
                    //计算最大耗时
                    OptionalDouble max = linkedDeque.stream().mapToDouble(
                            MetricsModel::getCostTime).max();
                    //计算最小耗时
                    OptionalDouble min = linkedDeque.stream().mapToDouble(
                            MetricsModel::getCostTime).min();
                    //计算平均耗时
                    OptionalDouble avg = linkedDeque.stream().mapToDouble(
                            MetricsModel::getCostTime).average();
                    metrics.setMax(max.getAsDouble());
                    metrics.setMin(min.getAsDouble());
                    metrics.setAvg(avg.getAsDouble());
                }
                //获取高于平均耗时的请求，并且进行排序展示
                List<MetricsModel> metricsTimeModels = new ArrayList<MetricsModel>();
                if (null != linkedDeque && linkedDeque.size() > 0) {
                    metricsTimeModels = linkedDeque.stream().filter(
                            a -> metrics.getAvg() < a.getCostTime())
                            .sorted(
                                    Comparator.comparing(
                                            MetricsModel::getCostTime)).collect(
                                    Collectors.toList());
                }
                //请求耗时统计
                buffer.append("request time in 1s[" + (int) linkedDeque.stream().filter(
                        a -> a.getCostTime() <= 1000).count() + "],")
                        .append("request time in 1~2s[" + (int) linkedDeque.stream().filter(
                                a -> a.getCostTime() > 1000 && a.getCostTime() <= 2000).count() + "],")
                        .append("request time in 2~4s[" + (int) linkedDeque.stream().filter(
                                a -> a.getCostTime() > 2000 && a.getCostTime() <= 4000).count() + "],")
                        .append("request time over 4s[" + (int) linkedDeque.stream().filter(
                                a -> a.getCostTime() > 4000).count() + "]");
                metrics.setTotal(linkedDeque.size());
                metrics.setDetail(buffer.toString());
                metrics.setMetricsTimeModels(metricsTimeModels);
                ObjectMapper mapper = new ObjectMapper();
                detaileMetrics = mapper.writer()
                        .writeValueAsString(metrics);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return detaileMetrics;
    }



}
