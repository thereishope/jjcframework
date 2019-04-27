package com.development.plugin.codeCheck.listener;
import com.development.plugin.codeCheck.codeCheck.CodeCheckHandler;
import com.development.plugin.codeCheck.configuration.ApplicationBuilders;
import com.development.transfer.proxy.CommonHandlerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**spring容器启动后异步进行控制层方法检查输出
 * springmvc下实现contextLoaderListener
 * chenjiajun
 * @title CodeCheckListener
 * @project plugin
 * @date 2019-03-16
 */
@Component
public class CodeCheckListener implements CommandLineRunner {

    @Autowired
    private ApplicationBuilders builder;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;


    @Async
    public void run(String... args) throws Exception {
       new CommonHandlerProxy(new CodeCheckHandler(
                builder.getContext(),requestMappingHandlerMapping)).excute();

    }
}
