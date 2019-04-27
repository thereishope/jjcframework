package com.development.transfer.configuration;



import java.util.HashMap;
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
public class ApplicationWrapBuilder {


    public static  Map<String, Object> beanMap ;


    public static void setBeanMap(Map beans){
        beanMap = beans;
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
        } catch (Exception e) {
            return null;
        }
        return null;
    }

    /**
     * @author chenjiajun
     * @date 2019-01-13
     */
    public static Object getBean(String beanId) {
        try {
            if (beanMap.containsKey(beanId)) {
                return beanMap.get(beanId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
