package com.development.dev.listener;


import com.development.dev.configuration.ApplicationBuilder;
import com.development.dev.listener.handler.propertiesLoad.PropertiesLoadHandler;
import com.development.transfer.proxy.CommonHandlerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 解决教育redis工具类配置无法在多环境配置下使用
 * 后续将适配基于配置中心的配置读取
 *
 * @author chenjiajun
 * @title PropertiesLoadListener
 * @project dev
 * @date 2019-04-17
 */
@Component
public class PropertiesLoadListener implements CommandLineRunner {


    @Autowired
    private ApplicationBuilder builder;


    /**
     * @author chenjiajun
     * @date 2019-04-17
     */
    @Async
    public void run(String... args) throws Exception {
        new CommonHandlerProxy(new PropertiesLoadHandler(
                builder.getContext())).excute();

    }
}
