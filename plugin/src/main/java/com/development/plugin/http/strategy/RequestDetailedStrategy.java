package com.development.plugin.http.strategy;

import com.development.plugin.http.model.DetailMetrics;
import com.development.plugin.http.model.MetricsTimeModel;
import com.development.plugin.http.queue.FixedQueue;
import com.development.plugin.http.strategy.queueBuffer.QueueBufferFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.OptionalDouble;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

/**明细模式策略
 * @author chenjiajun
 * @title RequestDetailedStrategy
 * @project request
 * @date 2019-03-16
 */
public class RequestDetailedStrategy implements Strategy {

    private static Logger logger = LoggerFactory.getLogger(RequestDetailedStrategy.class);

    /**
      *@author chenjiajun
      *@date 2019-03-16
      *
      */
    @Override
    public String getStrategy() {
        return "detail";
    }

    /**执行数据存储策略
      *@author chenjiajun
      *@date 2019-04-24
      *
      */
    @Override
    public <T> void excute(T t) {
        MetricsTimeModel metricsTimeModel = (MetricsTimeModel)t;
        logger.info("RequestDetailedStrategy[excute]获取到的metricsTimeModel," +
                "url["+metricsTimeModel.getUrl()+"],beginTime["+metricsTimeModel.getBeginTime()+"]");
        //后期可提供Redis类型队列
        ConcurrentLinkedDeque queue = (ConcurrentLinkedDeque) QueueBufferFactory.createQueue(metricsTimeModel.getQueueType());
        queue.add(metricsTimeModel);
    }


    /**明细模式，包括max，min以及avg
      *@author chenjiajun
      *@date 2019-03-16
      *
      */
    @Override
    public String getAnalysisMetrics()throws Exception {
        String detaileMetrics = null;
        StringBuffer buffer = new StringBuffer();
        ConcurrentLinkedDeque<MetricsTimeModel> linkedDeque = FixedQueue.
                getConcurrentLinkedDeque();
        logger.info("RequestDetailedStrategy[getAnalysisMetrics]size:"
                +linkedDeque.size()+" ");
        if(null != linkedDeque && linkedDeque.size()>0){
            //计算最大耗时
            OptionalDouble max = linkedDeque.stream().mapToDouble(
                    MetricsTimeModel::getCostTime).max();
            //计算最小耗时
            OptionalDouble min = linkedDeque.stream().mapToDouble(
                    MetricsTimeModel::getCostTime).min();
            //计算平均耗时
            OptionalDouble avg = linkedDeque.stream().mapToDouble(
                    MetricsTimeModel::getCostTime).average();
            DetailMetrics metrics = new DetailMetrics();
            metrics.setMax(max.getAsDouble());
            metrics.setMin(min.getAsDouble());
            metrics.setAvg(avg.getAsDouble());
            //获取高于平均耗时的请求，并且进行排序展示
            List<MetricsTimeModel> metricsTimeModels =  linkedDeque.stream().filter(
                    a -> avg.getAsDouble()<a.getCostTime()).sorted(
                            Comparator.comparing(MetricsTimeModel::getCostTime)).collect(
                                    Collectors.toList());
            //请求耗时统计
            buffer.append("请求耗时1秒内["+(int) linkedDeque.stream().filter(
                    a -> a.getCostTime()<=1000).count()+"],")
                    .append("请求耗时1~2秒["+(int) linkedDeque.stream().filter(
                            a -> a.getCostTime()>1000 && a.getCostTime()<=2000).count()+"],")
                        .append("请求耗时2~4秒["+(int) linkedDeque.stream().filter(
                                a -> a.getCostTime()>2000 && a.getCostTime()<=4000).count()+"],")
                    .append("请求耗时大于4秒["+(int) linkedDeque.stream().filter(
                            a -> a.getCostTime()>4000).count()+"]");
            metrics.setTotal(linkedDeque.size());
            metrics.setDetail(buffer.toString());
            metrics.setMetricsTimeModels(metricsTimeModels);
            ObjectMapper mapper = new ObjectMapper();
            detaileMetrics = mapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(metrics);
        }
        return detaileMetrics;
    }

    public RequestDetailedStrategy() {
    }
}
