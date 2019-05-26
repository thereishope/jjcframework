package com.development.httpPlugin.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring bean工具
 *
 * @author jiajunchen
 * @title ApplicationContextUtil
 * @project httpPlugin
 */
@Component
public class ApplicationContextUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    public static final Map<String, Object> beanMap =
            new ConcurrentHashMap<String, Object>();

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public ApplicationContext getContext() {
        return context;
    }

}
