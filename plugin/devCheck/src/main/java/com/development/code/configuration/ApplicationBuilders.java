package com.development.code.configuration;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * spring bean工具
 *
 * @author palading_cr@163.com
 * @title ApplicationBuilders
 * @project devCheck
 */
@Component
public class ApplicationBuilders implements ApplicationContextAware {

    private static ApplicationContext context;


    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        context = applicationContext;
    }

    public ApplicationContext getContext() {
        return context;
    }

}
