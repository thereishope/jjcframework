package com.development.httpPlugin.strategy;


/**
 * @author jiajunchen
 * @title Strategy
 * @project httpPlugin
 * @date 2019-04-22
 */
public interface Strategy {

    //监控策略
    public String getStrategy();

    //监控执行
    public <T> void excute(T t);

    //监控分析
    public String getAnalysisMetrics(Object o)throws Exception;

}
