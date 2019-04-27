package com.development.transfer.configuration;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring bean工具
 *
 * @author chenjiajun
 * @title ApplicationBuilder
 * @project transfer
 * @date 2019-01-13
 */
@Component
public class ApplicationAdaptBuilder implements ApplicationContextAware {

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
     * @author chenjiajun
     * @date 2019-01-13
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
     * @author chenjiajun
     * @date 2019-01-13
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
