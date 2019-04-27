package com.development.transfer.invoke;



import com.development.transfer.configuration.ApplicationWrapBuilder;
import com.development.transfer.constant.SysConstant;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.exception.BuisException;
import com.development.transfer.respconse.DevResponse;

import java.lang.reflect.InvocationTargetException;


/**
 * 根据业务名称适配具体实现类并封装返回异常
 * 结合具体项目实现getResponse接口完成返回值适配
 *
 * @author jiajun_chen
 */
public class DevServiceInvoke{




    private static DevServiceInvoke devServiceInvoke = new DevServiceInvoke();





    /**
     * 统一调用业务层方法，并实现异常统一处理
     * 并实现适配调用
     * @author chenjiajun
     * @date 2019-01-13
     */
    public <T> T doInvoke(DevParamContainer param, Class<T> t) throws Exception {
        try {
            BuisServiceInvoke service = ApplicationWrapBuilder.getBean(param
                        .getServiceMethod()
                        .getServiceEnums()
                        .getServiceName(),
                BuisServiceInvoke.class);
            return (T) service.excute(param, t);
        } catch (Exception e) {
            return getExResponse(e, t);
        }
    }



    /**业务异常组装并返回至控制层
     * 业务层异常抛出后，识别业务异常及其他异常
      *@author chenjiajun
      *@date 2019-01-21
      *
      */
    public <T> T getExResponse(Exception e, Class<T> t) {
        return (T) getResponse(e);
    }

    public <T> T getExResponse(Class<? extends Throwable> e, Class<T> t) {
        return null;
    }

    /**
     * 返回值处理样例
     *
     * @author chenjiajun
     * @date 2019-01-13
     */
    public DevResponse getResponse(Exception e) {
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

    private DevServiceInvoke(){};

    public static DevServiceInvoke getInstance(){
        return devServiceInvoke;
    }


}
