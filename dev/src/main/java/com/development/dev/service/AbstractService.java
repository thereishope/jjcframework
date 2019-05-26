package com.development.dev.service;

import com.development.transfer.constant.SysConstant;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.invoke.BuisServiceInvoke;
import com.development.transfer.invoke.DevServiceInvoke;
import com.development.transfer.respconse.DevResponse;
import com.development.transfer.serviceMethod.MethodCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author chenjiajun
 * @title AbstractService
 * @project dev
 * @date 2018-12-16
 */
public abstract class AbstractService implements BuisServiceInvoke {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //@Transactional(rollbackFor = Exception.class)
    public <T> T excute(DevParamContainer paraMap, Class<T> t) throws Exception {
        T res = null;
        Method method = getMethodCache(paraMap);
        if (null != method) {
            res = (T) method.invoke(this, paraMap);
            /**
             * 1.根据res对象返回码，进行操作日志异步记录
             * 2.新老系统数据同步
             * 3.
             */
        }
        return res;
    }
    /**
     * 内存缓存方法反射对象
     * 提高调用效率
     *
     * @author chenjiajun
     * @date 2018-12-16
     */
    public Method getMethodCache(DevParamContainer paraMap) {
        String methodKey = paraMap.getServiceMethod().getServiceEnums()
                .getServiceName()
                + "_"
                + paraMap.getServiceMethod().getServiceMethodName()
                .concat("key");
        Method methodCache = MethodCache.getMehod(methodKey);
        if (null == methodCache) {
            try {
                methodCache = this.getClass().getMethod(
                        paraMap.getServiceMethod().getServiceMethodName(),
                        DevParamContainer.class);
                methodCache.setAccessible(true);
                MethodCache.setMethod(methodKey, methodCache);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return methodCache;
    }


    /**
     * 业务异常抛出
     *
     * @author chenjiajun
     * @date 2018-12-16
     */
    public void throwEx(Exception e) throws Exception {
        logger.error("Service[" + this.getClass().
                getSimpleName() + "],方法[" + e.getStackTrace()[0].
                getMethodName() + "]发生异常", e);
        throw e;
    }
    
    /**
      *@author chenjiajun
      *@date 2019-01-07
      *
      */
    protected DevResponse commonResponse(){
        return commonResponse(true,null,SysConstant.SUCCESS.getCode(),SysConstant.SUCCESS.getMsg());
    }

    protected  DevResponse commonResponse(boolean flag,Object data,String code,String msg){
        return new DevResponse(flag,code,msg,data);
    }

}
