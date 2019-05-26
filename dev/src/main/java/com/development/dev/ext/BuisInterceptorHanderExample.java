package com.development.dev.ext;
import com.development.dev.common.async.AsyncEnum;
import com.development.dev.configuration.serviceLoad.ServiceLoadFactory;
import com.development.transfer.constant.SysConstant;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.exception.BuisException;
import com.development.transfer.invoke.BuisInterceptoHander;
import com.development.transfer.respconse.DevResponse;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;

/**业务拦截处理以及异常转换处理
 * 如果不实现则采用默认机制(包括异常转换)
 *(可选)
 * @author jiajunchen
 * @title BuisEx
 * @project fastdev
 * @date 2019-05-13
 */
@Component
public class BuisInterceptorHanderExample implements BuisInterceptoHander {


    /**前置拦截
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
    @Override
    public BuisInterceptoHander preHandle(DevParamContainer iflytekParamContainer) throws Exception {
        return null;
    }

    /**后置拦截处理
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
    @Override
    public void afterHandle(Class aClass, DevParamContainer iflytekParamContainer) {
        try {
            ServiceLoadFactory
                    .BUFFER_STATIC_ASYNC
                    .get(AsyncEnum.AYSNC_001
                            .getAsyncCode()).handle(iflytekParamContainer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**异常转换处理
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
    @Override
    public Object getExResponse(Exception e, Class aClass) {
        DevResponse res = new DevResponse();
        if (e instanceof InvocationTargetException) {
            InvocationTargetException exe = (InvocationTargetException) e;
            Throwable te = exe.getTargetException();
            if (te instanceof BuisException) {
                BuisException se = (BuisException) te;
                res.setResCode(se.getCode());
                res.setResMsg(se.getMsg());
                res.setFlag(false);
                res.setData(null);
                return res;
            }
        }
        res.setResCode(SysConstant.ERROR.getCode());
        res.setResMsg(SysConstant.ERROR.getMsg());
        res.setFlag(false);
        res.setData(null);
        return res;
    }
}
