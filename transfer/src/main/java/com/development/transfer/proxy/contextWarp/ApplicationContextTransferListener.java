package com.development.transfer.proxy.contextWarp;
import com.development.transfer.configuration.ApplicationAdaptBuilder;
import com.development.transfer.proxy.CommonHandlerProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**不允许删除该类，否则无法调用
 * @author chenjiajun
 * @title ApplicationContextTransferListener
 * @project transfer
 * @date 2019-01-13
 */
@Component
public class ApplicationContextTransferListener implements CommandLineRunner {

    @Autowired
    private ApplicationAdaptBuilder builder;


    /**
     * @author chenjiajun
     * @date 2019-01-13
     */
    @Async
    public void run(String... args) throws Exception {
        new CommonHandlerProxy(new ContextTransferHandler(
                builder.getContext())).excute();

    }
}
