package com.development.separtion.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author jiajunchen
 * @title ApplicationUtils
 * @project rwSeparation
 */
@Component
public class ApplicationUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public ApplicationContext getContext() {
        return context;
    }
}
