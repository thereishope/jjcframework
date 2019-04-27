package com.development.plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author chenjiajun
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
        MetricsTimeModel metricsTimeModel = (MetricsTimeModel)t;
        logger.info("TestPlugin[excute]获取到的metricsTimeModel," +
                "url["+metricsTimeModel.getUrl()+"],beginTime["+metricsTimeModel.getBeginTime()+"]");

    }

    public String getAnalysisMetrics() throws Exception {
        return "haha";
    }
}
