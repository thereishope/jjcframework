package com.development.dev.controller;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.invoke.DevServiceInvoke;
import com.development.transfer.respconse.DevResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * @author chenjiajun
 * @title AbstractController
 * @project dev
 * @date 2018-12-04
 */
public abstract class AbstractController {

    public Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private DevServiceInvoke serviceInvoke;


    /**获取container对象并添加公用信息
      *@author chenjiajun
      *@date 2019-01-15
      *
      */
    public DevParamContainer getBaseContainer(){
        //通过request添加基础信息，例如放置在header里的token,用户ip，操作时间等
        DevParamContainer container = new DevParamContainer(){
            {
                put("operateTime",new Date());
                put("token",request.getHeader("x-token"));
            }
        };
        return container;
    }


    /**
     * @author chenjiajun
     * @date 2018-12-29
     */
    public DevResponse invoke(DevParamContainer container)throws Exception {
        DevResponse res = null;
        res = serviceInvoke.doInvoke(container, DevResponse.class);
        return res;
    }


    public void flushLogger(Exception e) {
        logger.error("controller[" + this.getClass().
                getSimpleName() + "],方法[" + e.getStackTrace()[0].
                getMethodName() + "]发生异常", e);
    }

}
