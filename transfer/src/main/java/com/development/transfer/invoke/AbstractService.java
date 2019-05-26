package com.development.transfer.invoke;

import com.development.transfer.constant.SysConstant;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.respconse.DevResponse;
import com.development.transfer.serviceMethod.MethodCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Method;

/**
 * @author jiajunchen
 * @title AbstractService
 * @project transfer
 * @date 2019-04-24
 */

public abstract class AbstractService implements BuisServiceInvoke {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private RwSeparationExtend rwSeparationExtend;

    /**发起service层请求
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
   @Transactional(rollbackFor = Exception.class)
    public <T> T excute(DevParamContainer paraMap, Class<T> t) throws Exception {
        T res = null;
        Method method = null;
        method = getMethodCache(paraMap);
            if (null != method) {
                setDataSourceExt(method);
                res = (T) method.invoke(this, paraMap);
            }
        return res;
    }

    /**加载读写分离规则，并根据规则设置数据源
     *在dev工程中进行扩展
      *@author jiajunchen
      *@date 2019-05-26
      */
    private void setDataSourceExt(Method method){
        if(null != rwSeparationExtend){
            rwSeparationExtend.setDataSource(method);
        }
    }

    /**
     * 内存缓存方法反射对象
     * 提高调用效率
     * @author jiajunchen
     * @date 2019-04-24
     */
    private Method getMethodCache(DevParamContainer paraMap) {
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
     * @author jiajunchen
     * @date 2019-04-24
     */
    public void throwEx(Exception e) throws Exception {
        logger.error("Service[" + this.getClass().
                getSimpleName() + "],方法[" + e.getStackTrace()[0].
                getMethodName() + "]发生异常", e);
        throw e;
    }
    
    /**
      *@author jiajunchen
      *@date 2019-04-24
      *
      */
    protected DevResponse commonResponse(){
        return commonResponse(true,null,SysConstant.SUCCESS.getCode(),SysConstant.SUCCESS.getMsg());
    }

    protected DevResponse commonResponse(boolean flag, String code, String msg, Object data){
        return new DevResponse(flag,code,msg,data);
    }


}
