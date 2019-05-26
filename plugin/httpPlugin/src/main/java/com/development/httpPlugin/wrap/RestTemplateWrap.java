package com.development.httpPlugin.wrap;
import com.development.httpPlugin.strategy.MetricsStrategy;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http工具包装配置类
 *
 * @author jiajunchen
 */
@Configuration
@EnableConfigurationProperties(HttpProperties.class)
public class RestTemplateWrap {

    //Apache HttpClient
    private static final String APACHE_HTTP = "httpClient";

    //okHttp3
    private static final String OK_HTTP_3 = "okHttp3";

    public static Map<String, String> strategyMap = null;

    private static Logger logger = LoggerFactory.getLogger(RestTemplateWrap.class);


    @Autowired
    private HttpProperties properties;


    /**
     * 根据配置动态构造ClientHttpRequestFactory
     * @author jiajunchen
     */
    @Bean
    public ClientHttpRequestFactory httpRequestFactory() {
        switch (properties.getHttpType()) {
            case APACHE_HTTP:
                logger.info("RestTemplateWrap[httpRequestFactory] httpClient");
                return new HttpComponentsClientHttpRequestFactory(httpClient());
            case OK_HTTP_3:
                logger.info("RestTemplateWrap[httpRequestFactory] okhttp3");
                return new OkHttp3ClientHttpRequestFactory(okHttpClient());
            default:
                return new HttpComponentsClientHttpRequestFactory(httpClient());
        }
    }


    /**
     * 以httpClient或okHttp3为基础底层，构造RestTemplate
     * 加入监控策略
     * @author jiajunchen
     */
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(httpRequestFactory());
        logger.info("RestTemplateWrap[restTemplate]:" + properties.getStrategy() + "");
        if (properties.isMetricsUse()) {
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<ClientHttpRequestInterceptor>();
            MetricsStrategy metricsStrategy = new MetricsStrategy();
            interceptors.add(metricsStrategy);
            restTemplate.setInterceptors(interceptors);
            strategyMap = new HashMap<String, String>() {
                {
                    //策略
                    put("strategy", properties.getStrategy());
//                    //缓冲队列类型
//                    put("queueType", StringUtils.isEmpty(properties.getQueueType()) ?
//                            QueueBufferFactory.QUEUE_BUFFER_STORE_STRATEGY_MEM : properties.getQueueType());
                }
            };
        }
        return restTemplate;
    }


    /**
     * 设置http连接池，并支持http/https连接
     * @author jiajunchen
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

    /**
     * okHttp3配置
     * 超时设置，https证书认证，连接池
     * @author jiajunchen
     */
    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient.Builder()
                .retryOnConnectionFailure(false)
                .connectionPool(new ConnectionPool(properties.getMaxTotal(), 5, TimeUnit.MINUTES))
                .connectTimeout(properties.getSocketTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(properties.getRequestTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(properties.getRequestTimeout(), TimeUnit.MILLISECONDS)
                .sslSocketFactory(sslSocketFactory(), x509TrustManager())
                .build();
    }


    @Bean
    public X509TrustManager x509TrustManager() {
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
    }

    @Bean
    public SSLSocketFactory sslSocketFactory() {
        try {
            //信任任何链接
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{x509TrustManager()}, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("okhttp sslSocketFactory发生异常", e);
        }
        return null;
    }


}

