package com.development.plugin.codeCheck.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring bean工具
 *
 * chenjiajun
 * @title ApplicationBuilder
 * @project plugin
 * @date 2019-04-24
 */
@Component
public class ApplicationBuilders implements ApplicationContextAware {

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



    /**
     * chenjiajun
     * @date 2019-03-16
     */
    public static <T> T getBean(String beanId, Class<T> T) {
        try {
            if (beanMap.containsKey(beanId)) {
                return (T) beanMap.get(beanId);
            }
            Object bean = context.getBean(beanId);
            if (bean == null) {
                throw new Exception("通过spring容器查找不到这个bean {" +
                        "" + beanId + "} ");
            }
            beanMap.put(beanId, bean);
            return (T) bean;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * chenjiajun
     * @date 2019-03-16
     */
    public Object getBean(String beanId) {
        try {
            if (beanMap.containsKey(beanId)) {
                return beanMap.get(beanId);
            }
            Object bean = context.getBean(beanId);
            if (bean == null) {
                throw new Exception("通过spring容器查找不到" +
                        "这个bean {" + beanId + "} ");
            }
            beanMap.put(beanId, bean);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
