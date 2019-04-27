package com.development.plugin.http.wrap;

import com.development.plugin.http.strategy.MetricsStrategy;
import com.development.plugin.http.strategy.Strategy;
import com.development.plugin.http.strategy.queueBuffer.QueueBufferFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**http工具包装配置类
  *@author chenjiajun
  *@date 2019-04-17
  *
  */
@EnableScheduling
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class RestTemplateWrap {

    @Autowired
    private HttpProperties properties;

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory(httpClient());
    }

    public static Map<String,Object> strategyMap = null;


    /**以httpClient为基础底层，构造RestTemplate
     * 加入监控策略
      *@author chenjiajun
      *@date 2019-04-17
      *
      */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        if(!StringUtils.isEmpty(properties.getStrategy())){
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
            MetricsStrategy metricsStrategy = new MetricsStrategy();
            interceptors.add(metricsStrategy);
            restTemplate.setInterceptors(interceptors);
            strategyMap = new HashMap<String,Object>(){
                {
                    //策略
                    put("strategy",StringUtils.isEmpty(properties.getStrategy())?"detail":properties.getStrategy());
                    //缓冲队列类型
                    put("queueType",StringUtils.isEmpty(properties.getQueueType())?
                            QueueBufferFactory.QUEUE_BUFFER_STORE_STRATEGY_MEM : properties.getQueueType());
                }
            };
        }
        return restTemplate;
    }


    /**设置http连接池，并支持http/https连接
      *@author chenjiajun
      *@date 2019-04-17
      *
      */
    @Bean
    public HttpClient httpClient() {
        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(registry);
        connectionManager.setMaxTotal(properties.getMaxTotal());
        connectionManager.setDefaultMaxPerRoute(properties.getMaxPreRoute());
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(properties.getSocketTimeout())
                .setConnectTimeout(properties.getConnectionTimeout())
                .setConnectionRequestTimeout(properties.getConnectionTimeout())
                .build();
        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .build();
    }
}
