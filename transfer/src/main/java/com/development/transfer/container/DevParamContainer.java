package com.development.transfer.container;

import com.development.transfer.serviceMethod.ServiceMethod;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 统一传参容器
 * 并可设置调用得业务方法
 *
 * @author chenjiajun
 * @title DevParamContainer
 * @project transfer
 * @date 2019-01-13
 */
public class DevParamContainer implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Map<String, Object> param;

    //业务方法接口
    private ServiceMethod serviceMethod;


    /**
     * 获取参数
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public <T> T get(String paramKey, Class<T> clazz) {
        return (T) this.param.get(paramKey);
    }

    /**
     * 获取字符类型的值
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public String getStringValue(String paramKey) {
        return get(paramKey, String.class);
    }

    /**
     * 获取Integer类型的值
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public Integer getIntegerValue(String paramKey) {
        return get(paramKey, Integer.class);
    }

    /**
     * 设置参数
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public void put(String paramKey, Object o) {
        this.param.put(paramKey, o);
    }


    public ServiceMethod getServiceMethod() {
        return serviceMethod;
    }

    /**
     * 设置业务方法
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public void setMethodEnum(ServiceMethod serviceMethod) {
        this.serviceMethod = serviceMethod;
    }

    public DevParamContainer(Map<String, Object> param) {
        this.param = param;
    }

    /**
     * @author chenjiajun
     * @date 2019-01-13
     */
    public DevParamContainer() {
        this.param = new HashMap<String, Object>();
    }


    /**
     * 清空传参容器内某个值
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public void removeAttr(String key) {
        this.param.remove(key);
    }

    /**
     * 清空参数容器
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public void clear() {
        this.param.clear();
    }
}
