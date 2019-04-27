package com.development.transfer.proxy.contextWarp;


import com.development.transfer.configuration.ApplicationWrapBuilder;
import com.development.transfer.invoke.BuisServiceInvoke;
import com.development.transfer.proxy.CommonHandler;
import org.springframework.context.ApplicationContext;

import java.util.Map;

/**
 * @author chenjiajun
 * @title ContextTransferHandle
 * @project dev
 * @date 2019-01-13
 */

public class ContextTransferHandler implements CommonHandler {

    private ApplicationContext context;

    public ContextTransferHandler(ApplicationContext context) {
        this.context = context;
    }

    public boolean allowance() throws Exception {
        return true;
    }

    public void handle() throws Exception {
        Map<String,BuisServiceInvoke> beanMap = context.getBeansOfType(BuisServiceInvoke.class);
        ApplicationWrapBuilder.setBeanMap(beanMap);
//        Map<String,Object> m = ApplicationWrapBuilder.beanMap;
    }
}
