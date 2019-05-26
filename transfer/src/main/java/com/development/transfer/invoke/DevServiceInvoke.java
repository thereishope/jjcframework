package com.development.transfer.invoke;



import com.development.transfer.configuration.ApplicationWrapBuilder;
import com.development.transfer.constant.SysConstant;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.exception.BuisException;
import com.development.transfer.respconse.DevResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;


/**
 * 根据业务名称适配具体实现类并封装返回异常
 * 结合具体项目实现getResponse接口完成返回值适配
 *
 * @author jiajun_chen
 */
@Component
public class DevServiceInvoke{

    private static Logger logger = LoggerFactory.getLogger(DevServiceInvoke.class);

    @Autowired
    Environment environment;


    @Autowired(required = false)
    private BuisInterceptoHander buisInterceptoHander;

    /**
     * 统一调用业务层方法，并实现异常统一处理
     * 并实现适配调用
     * @author jiajunchen
     * @date 2019-4-18
     */
    public <T> T doInvoke(DevParamContainer param, Class<T> t) throws Exception {
        T res = null;
        try {
            //前置处理
            preHandle(param);
            BuisServiceInvoke service = ApplicationWrapBuilder.getBean(
                    param.getServiceMethod()
                            .getServiceEnums()
                            .getServiceName(),
                    BuisServiceInvoke.class);
            //处理业务请求
            res = (T)service.excute(param, t);
            //请求后置处理
            afterHandle(t, param);
        } catch (Exception e) {
            //定位/打印错误日志
            logerExport(param, e);
            //默认异常转换
            if (defaultResponse()) {
                return (T) getExResponse(e, t);
            }
            //自定义异常逻辑
            return (T) getExtendResponse(e, t);
        }
        return res;
    }

    /**自定义异常转换
     *@author jiajunchen
     *@date 2019-05-13
     *
     */
    private <T> T getExtendResponse(Exception e, Class<T> t) throws Exception {
        return null != buisInterceptoHander ? (T) buisInterceptoHander.getExResponse(e, t) : null;
    }

    /**
     * 请求前置处理
     *
     * @author jiajunchen
     * @date 2019-05-13
     */
    private BuisInterceptoHander preHandle(DevParamContainer container) throws Exception {
        return null != buisInterceptoHander ? buisInterceptoHander.preHandle(container) : null;
    }

    /**
     * 请求后置处理
     *
     * @author jiajunchen
     * @date 2019-05-13
     */
    private void afterHandle(Class t, DevParamContainer container) throws Exception {
        if (null != buisInterceptoHander) {
            buisInterceptoHander.afterHandle(t, container);
        }
    }

    /**
     * 判断是否使用默认内置的返回值样例
     *
     * @author jiajunchen
     * @date 2019-05-13
     */
    private boolean defaultResponse() {
        return null == environment.getProperty("dev.res.default", Boolean.class)
                || environment.getProperty("dev.res.default", Boolean.class);
    }



    private <T> T getExResponse(Exception e, Class<T> t) {
        return (T) getResponse(e);
    }

    private <T> T getExResponse(Class<? extends Throwable> e, Class<T> t) {
        return null;
    }

    /**
     * 返回值处理样例
     *
     * @author jiajunchen
     * @date 2019-4-18
     */
    private DevResponse getResponse(Exception e) {
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


    /**日志输出打印
     *@author jiajunchen
     *@date 2019-05-13
     *
     */
    private void logerExport(DevParamContainer container, Exception e) {
        String methodName = container.getServiceMethod().getServiceMethodName();
        logger.error("service[" + container.getServiceMethod()
                .getServiceEnums().
                        getServiceName() + "]," +
                "method[" + methodName + "]发生异常", e);
    }


}
