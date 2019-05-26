package com.development.dev.listener.handler.propertiesLoad;
import com.development.tool.redis.JedisUtils;
import com.development.transfer.proxy.CommonHandler;
import org.springframework.context.ApplicationContext;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author chenjiajun
 * @title PropertiesHandler
 * @project dev
 * @date 2019-04-17
 */
public class PropertiesLoadHandler implements CommonHandler {

    private ApplicationContext context;


    public PropertiesLoadHandler(ApplicationContext context) {
        this.context = context;
    }

    public boolean allowance() throws Exception {
        return null != context.getEnvironment().getProperty("standar.redis.check", boolean.class) &&
                context.getEnvironment().getProperty("standar.redis.check", boolean.class);
    }

    /**加载当前激活配置
      *@author chenjiajun
      *@date 2019-04-18
      *
      */
    public void handle() throws Exception {
        String profie = context.getEnvironment().getProperty("spring.profiles.active");
        ClassLoader ex = PropertiesLoadHandler.class.getClassLoader();
        InputStream is = ex.getResourceAsStream("application-"+profie+".properties");
        Properties properties = PropertiesLoader.getInstance();
        if (is != null) {
            properties.load(is);
        }
        setProperties(properties);
    }

    /**com.dev.tool工程中持有的静态Properties对象
     * 设置为静态资源后实现共享
      *@author chenjiajun
      *@date 2019-04-18
      *
      */
    public void setProperties(Properties properties){
        JedisUtils.setProperties(properties);
    }
}

/**单例静态加载Properties对象
  *@author chenjiajun
  *@date 2019-04-18
  *
  */
class PropertiesLoader{
    private PropertiesLoader(){

    };

    private static class PropertiesSingleHoder{
        private static Properties properties = new Properties();
    }

    public static Properties getInstance(){
        return PropertiesSingleHoder.properties;
    }
}
