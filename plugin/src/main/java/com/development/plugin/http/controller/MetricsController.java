package com.development.plugin.http.controller;

import com.development.plugin.http.strategy.Strategy;
import com.development.plugin.http.wrap.HttpProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ServiceLoader;

/**
 * @author chenjiajun
 * @title MetricsController
 * @project request
 * @date 2019-03-16
 */
@RestController
public class MetricsController {

    private Logger logger = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpProperties properties;


    /**根据不同策略模式获取http监控数据
     * 默认为明细模式
      *@author chenjiajun
      *@date 2019-03-16
      *
      */
    @RequestMapping("/metrics/http")
    public String getHttpMetrics()throws Exception{
        String model = StringUtils.isEmpty(properties.getStrategy())?"detail":properties.getStrategy();
        logger.info("MetricsController[getHttpMetrics]策略模式["+model+"]");
        ServiceLoader<Strategy> serviceLoader = ServiceLoader.load(Strategy.class);
        for(Strategy strategy : serviceLoader){
            if(model.equals(strategy.getStrategy())){
                return strategy.getAnalysisMetrics();
            }
        }
        return null;

    }
}
