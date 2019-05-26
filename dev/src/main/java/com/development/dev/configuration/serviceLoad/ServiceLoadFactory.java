package com.development.dev.configuration.serviceLoad;

import com.development.dev.common.async.CommonAsyncHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**不同类型接口加载工厂
 * @author jiajunchen
 * @title ServiceLoadFactory
 * @project fastdev
 * @date 2019-05-06
 */
@Configuration
public class ServiceLoadFactory {

    private static Logger logger = LoggerFactory
            .getLogger(ServiceLoadFactory.class);

    /**异步处理接口集合
      *@author jiajunchen
      *@date 2019-05-06
      *
      */
    public static final Map<String, CommonAsyncHandler> BUFFER_STATIC_ASYNC =
            new ConcurrentHashMap<String, CommonAsyncHandler>();

    /**加载CommonAsyncHandler类型实现
     * spring3 使用applicationContext.getBeansOfType进行注入
      *@author jiajunchen
      *@date 2019-05-06
      *
      */
    @Autowired
    public void setAsyncService(Map<String, CommonAsyncHandler> asyncHandler) {
        for (Map.Entry<String, CommonAsyncHandler> item : asyncHandler.entrySet()) {
            CommonAsyncHandler async = item.getValue();
            if (BUFFER_STATIC_ASYNC.containsKey(async.getAsyncCode())) {
                logger.error(async.getAsyncCode() + "----重复");
                System.exit(0);
            }
            BUFFER_STATIC_ASYNC.put(async.getAsyncCode(), async);
        }
    }




}


