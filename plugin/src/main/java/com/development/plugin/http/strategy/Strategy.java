package com.development.plugin.http.strategy;


/**
 * @author chenjiajun
 * @title StrategyHandlerFactory
 * @project request
 * @date 2019-03-16
 */
public interface Strategy {

    public String getStrategy();

    public <T> void excute(T t);

    public String getAnalysisMetrics()throws Exception;

}
