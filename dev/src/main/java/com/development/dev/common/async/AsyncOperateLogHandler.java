package com.development.dev.common.async;

import com.development.transfer.container.DevParamContainer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**异步记录操作日志
 * @author jiajunchen
 * @title AsyncOperateLogHandler
 * @project fastdev
 * @date 2019-05-06
 */
@Component
public class AsyncOperateLogHandler implements CommonAsyncHandler<DevParamContainer> {

    public String getAsyncCode() {
        return AsyncEnum.AYSNC_001.getAsyncCode();
    }

    /**异步操作日志记录
      *@author jiajunchen
      *@date 2019-05-06
      *
      */
    @Async
    public <T> T handle(T t) throws Exception {
        DevParamContainer container = (DevParamContainer)t;
        return null;
    }

}
