package com.development.transfer.invoke;

import com.development.transfer.container.DevParamContainer;

/**请求拦截及异常转换操作
 * @author jiajunchen
 * @title BuisInterceptoHander
 * @project adapter
 * @date 2019-05-13
 */
public interface BuisInterceptoHander<T> {


    /**前置拦截
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
    public BuisInterceptoHander   preHandle(DevParamContainer container) throws Exception;

    /**请求后置处理
     * res 返回值对象
     * container请求参数容器
     * method 方法对象
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
    public <T> void afterHandle(Class<T> res, DevParamContainer container) throws Exception;

    /**异常转换处理
     * t 为自定义返回值
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
    public <T> T getExResponse(Exception e, Class<T> t) throws Exception;
}
