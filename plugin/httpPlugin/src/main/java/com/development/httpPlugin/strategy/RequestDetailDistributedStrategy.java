package com.development.httpPlugin.strategy;

/**利用redis进行存储请求
 * 并进行分析
 * @author jiajunchen
 * @title RequestDetailDistributedStrategy
 * @project httpPlugin
 */
public class RequestDetailDistributedStrategy implements Strategy {


    //分布式请求下的统计模式
    @Override
    public String getStrategy() {
        return "redis";
    }

    /**在redis队列中存储请求
      *@author jiajunchen
      *
      */
    @Override
    public <T> void excute(T t) {

    }

    /**统计分析
      *@author jiajunchen
      *
      */
    @Override
    public String getAnalysisMetrics(Object o) throws Exception {
        return null;
    }

    public RequestDetailDistributedStrategy() {
    }
}
