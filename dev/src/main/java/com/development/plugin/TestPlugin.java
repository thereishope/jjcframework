package com.development.plugin;
import com.development.httpPlugin.model.MetricsModel;
import com.development.httpPlugin.strategy.Strategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author jiajunchen
 * @title TestPlugin
 * @project dev
 * @date 2019-04-24
 */
public class TestPlugin implements Strategy {

    private static Logger logger = LoggerFactory.getLogger(TestPlugin.class);

    public TestPlugin() {
        super();
    }

    public String getStrategy() {
        return "test";
    }

    public <T> void excute(T t) {
        MetricsModel metricsTimeModel = (MetricsModel)t;
        logger.info("TestPlugin[excute]获取到的metricsTimeModel," +
                "url["+metricsTimeModel.getUrl()+"],beginTime["+metricsTimeModel.getBeginTime()+"]");

    }

    public String getAnalysisMetrics(Object o) throws Exception {
        return null;
    }
}
