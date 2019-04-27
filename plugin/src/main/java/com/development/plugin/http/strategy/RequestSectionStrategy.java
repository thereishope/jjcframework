package com.development.plugin.http.strategy;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/**分段统计模式
 * @author chenjiajun
 * @title RequestSectionStrategy
 * @project request
 * @date 2019-03-16
 */
public class RequestSectionStrategy implements Strategy {


    //分段统计
    @Override
    public String getStrategy() {
        return "section";
    }

    @Override
    public <T> void excute(T t) {

    }
    @Override
    public String getAnalysisMetrics() throws Exception {
        return null;
    }

    public RequestSectionStrategy() {
    }
}
