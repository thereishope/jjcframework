package com.development.httpPlugin.metricsFile;

import com.development.httpPlugin.model.MetricsModel;
import com.development.httpPlugin.strategy.Strategy;
import com.development.httpPlugin.wrap.RestTemplateWrap;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.ServiceLoader;

/**
 * @author jiajunchen
 * @title MetricsLocalFileThread
 * @project httpPlugin
 */
public class MetricsLocalThread implements Runnable {

    public static String CACHE_METRICS_KEY = "cache_metrics_key";

    private static org.slf4j.Logger logger = LoggerFactory.getLogger(MetricsLocalThread.class);

    private LinkedList<MetricsModel> linkedList;

    private ApplicationContext context;

    public MetricsLocalThread(LinkedList<MetricsModel> linkedList, ApplicationContext context) {
        this.linkedList = linkedList;
        this.context = context;
    }

    @Override
    public void run() {
        try {
            if (null != linkedList && linkedList.size() > 0) {
                logger.info("server.port:" + context.getEnvironment()
                        .getProperty("server.port")
                        + "metric time:[" +
                        "" + new SimpleDateFormat("yyyy-mm-dd  HH:mm:ss")
                        .format(new Date()) + "]-------------------------------------------------------------------");
                ServiceLoader<Strategy> serviceLoader = ServiceLoader.load(Strategy.class);
                Object o = RestTemplateWrap.strategyMap.get("strategy");
                String strategyName = null != o ? String.valueOf(o) : "detail";
                String msg = null;
                for (Strategy strategy : serviceLoader) {
                    if (strategyName.equals(strategy.getStrategy())) {
                        msg = strategy.getAnalysisMetrics((Object) linkedList);
                        break;
                    }
                }
                logger.info(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            linkedList.clear();
        }
//     public String getMetrics( LinkedList<MetricsTimeModel> linkedDeque){
//            String detaileMetrics = null;
//            try {
//                StringBuffer buffer = new StringBuffer();
//             if (null != linkedDeque && linkedDeque.size() > 0) {
//                    DetailMetrics metrics = new DetailMetrics();
//                if (null != linkedDeque && linkedDeque.size() > 0) {
//                    //计算最大耗时
//                    OptionalDouble max = linkedDeque.stream().mapToDouble(
//                            MetricsTimeModel::getCostTime).max();
//                    //计算最小耗时
//                    OptionalDouble min = linkedDeque.stream().mapToDouble(
//                            MetricsTimeModel::getCostTime).min();
//                    //计算平均耗时
//                    OptionalDouble avg = linkedDeque.stream().mapToDouble(
//                            MetricsTimeModel::getCostTime).average();
//                    metrics.setMax(max.getAsDouble());
//                    metrics.setMin(min.getAsDouble());
//                    metrics.setAvg(avg.getAsDouble());
//                }
//                //获取高于平均耗时的请求，并且进行排序展示
//                List<MetricsTimeModel> metricsTimeModels = new ArrayList<MetricsTimeModel>();
//                if (null != linkedDeque && linkedDeque.size() > 0) {
//                    metricsTimeModels = linkedDeque.stream().filter(
//                            a -> metrics.getAvg() < a.getCostTime())
//                            .sorted(
//                                    Comparator.comparing(
//                                            MetricsTimeModel::getCostTime)).collect(
//                                    Collectors.toList());
//                }
//                //请求耗时统计
//                buffer.append("request time in 1s[" + (int) linkedDeque.stream().filter(
//                        a -> a.getCostTime() <= 1000).count() + "],")
//                        .append("request time in 1~2s[" + (int) linkedDeque.stream().filter(
//                                a -> a.getCostTime() > 1000 && a.getCostTime() <= 2000).count() + "],")
//                        .append("request time in 2~4s[" + (int) linkedDeque.stream().filter(
//                                a -> a.getCostTime() > 2000 && a.getCostTime() <= 4000).count() + "],")
//                        .append("request time over 4s[" + (int) linkedDeque.stream().filter(
//                                a -> a.getCostTime() > 4000).count() + "]");
//                metrics.setTotal(linkedDeque.size());
//                metrics.setDetail(buffer.toString());
//                metrics.setMetricsTimeModels(metricsTimeModels);
//                ObjectMapper mapper = new ObjectMapper();
//                detaileMetrics = mapper.writer()
//                        .writeValueAsString(metrics);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return detaileMetrics;
//    }
//
//
//    /**
//     * 计算最大、最小、及平均耗时
//     *
//     * @author jiajunchen
//     * @date 2019-05-05
//     */
//    public DetailMetrics getDetailMetrics(LinkedList<MetricsTimeModel>
//                                                  linkedDeque) {
//        DetailMetrics metrics = new DetailMetrics();
//        if (null != linkedDeque && linkedDeque.size() > 0) {
//            //计算最大耗时
//            OptionalDouble max = linkedDeque.stream().mapToDouble(
//                    MetricsTimeModel::getCostTime).max();
//            //计算最小耗时
//            OptionalDouble min = linkedDeque.stream().mapToDouble(
//                    MetricsTimeModel::getCostTime).min();
//            //计算平均耗时
//            OptionalDouble avg = linkedDeque.stream().mapToDouble(
//                    MetricsTimeModel::getCostTime).average();
//            metrics.setMax(max.getAsDouble());
//            metrics.setMin(min.getAsDouble());
//            metrics.setAvg(avg.getAsDouble());
//        }
//        return metrics;
//    }
//
//    /**
//     * 获取高于平均耗时的请求，并且进行排序(倒序)展示
//     *
//     * @author jiajunchen
//     * @date 2019-05-05
//     */
//    public List<MetricsTimeModel> getMetricSortList(LinkedList<MetricsTimeModel> linkedDeque,
//                                                    DetailMetrics detailMetrics) {
//        List<MetricsTimeModel> metricsTimeModels = new ArrayList<MetricsTimeModel>();
//        if (null != linkedDeque && linkedDeque.size() > 0) {
//            metricsTimeModels = linkedDeque.stream().filter(
//                    a -> detailMetrics.getAvg() < a.getCostTime())
//                    .sorted(
//                            Comparator.comparing(
//                                    MetricsTimeModel::getCostTime)).collect(
//                            Collectors.toList());
//        }
//        return metricsTimeModels;
//    }

    }
}
