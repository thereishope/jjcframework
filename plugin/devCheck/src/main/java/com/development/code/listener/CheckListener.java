package com.development.code.listener;
import com.development.code.configuration.ApplicationBuilders;
import com.development.code.handler.CheckHandler;
import com.development.transfer.proxy.CommonHandlerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**spring容器启动后异步进行控制层方法检查输出
 * springmvc下实现contextLoaderListener
 * @author palading_cr@163.com
 * @title CheckListener
 * @project devCheck
 */
@Component
public class CheckListener implements CommandLineRunner {

    @Autowired
    private ApplicationBuilders builder;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @Async
    public void run(String... args) throws Exception {
       new CommonHandlerProxy(new CheckHandler(
                builder.getContext(),requestMappingHandlerMapping)).excute();

    }
}
