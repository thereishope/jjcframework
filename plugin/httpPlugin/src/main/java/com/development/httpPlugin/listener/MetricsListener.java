package com.development.httpPlugin.listener;
import com.development.httpPlugin.context.ApplicationContextUtil;
import com.development.httpPlugin.metricsFile.MetricsLocalHandler;
import com.development.transfer.proxy.CommonHandlerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author jiajunchen
 * @title MetricsListener
 * @project httpPlugin
 */
@Component
public class MetricsListener implements CommandLineRunner {

    @Autowired
    private ApplicationContextUtil builder;



    @Async
    public void run(String... args) throws Exception {
        new CommonHandlerProxy(new MetricsLocalHandler(
                builder.getContext())).excute();
    }

}
