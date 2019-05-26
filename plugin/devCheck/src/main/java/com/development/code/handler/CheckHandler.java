package com.development.code.handler;
import com.development.code.extension.AbstractCodeCheckExcutor;
import com.development.code.extension.CheckExcutor;
import com.development.code.extension.defaultExcutor.DefaultCodeCheckExcutor;
import com.development.transfer.proxy.CommonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.TreeMap;

/**
 * @author palading_cr@163.com
 * @title CodeCheckHandler
 * @project devCheck
 * @date 2019-04-23
 */
public class CheckHandler implements CommonHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static Map<String, TreeMap> ctrMap = new HashMap<>();

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private ApplicationContext context;

    public CheckHandler(ApplicationContext context,
                        RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.context = context;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    /**
     *@author  palading_cr@163.com
     *@date 2019-05-05
     *
     */
    public void handle() throws Exception {
        ServiceLoader<CheckExcutor> serviceLoader = ServiceLoader.load(CheckExcutor.class);
        if(!serviceLoader.iterator().hasNext() ){
            defaultExcute();
            return;
        }
        spiExcute();
    }

    /**
     * 校验是是否需要进行检查
     * @author palading_cr@163.com
     * @date 2019-04-23
     */
    public boolean allowance() throws Exception {
        Environment env = context.getEnvironment();
        return null != env.getProperty("dev.controller.method.check", boolean.class) &&
                env.getProperty("dev.controller.method.check", boolean.class) && !"product".equals(env.getProperty("spring.profiles.active"));
    }



    /**支持编写不同扩展处理器执行代码检查逻辑
     * 在开发层需添加spi
      *@author  palading_cr@163.com
      *@date 2019-05-05
      *
      */
    private void spiExcute()throws Exception{
        ServiceLoader<CheckExcutor> serviceLoader = ServiceLoader.load(CheckExcutor.class);
        logger.info("spiExcute扩展处理器执行了");
        for (CheckExcutor extender : serviceLoader) {
            AbstractCodeCheckExcutor abstractCodeCheckExtender = (AbstractCodeCheckExcutor) extender;
            abstractCodeCheckExtender.setContext(context);
            abstractCodeCheckExtender.setRequestMappingHandlerMapping(requestMappingHandlerMapping);
            abstractCodeCheckExtender.extendExcute();
        }
    }

    /**如果开发层未使用spi注入接口实现，则使用默认内置的扩展处理器
      *@author  palading_cr@163.com
      *
      */
    private void defaultExcute()throws Exception{
        logger.info("默认内置的扩展处理器执行了");
        DefaultCodeCheckExcutor defaultCodeCheckExtender = new DefaultCodeCheckExcutor();
        defaultCodeCheckExtender.setContext(context);
        defaultCodeCheckExtender.setRequestMappingHandlerMapping(requestMappingHandlerMapping);
        defaultCodeCheckExtender.extendExcute();
    }




}

