package com.development.httpPlugin.restBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**http工具
 * @author jiajunchen
 * @project httpPlugin
 */
@Component
public class HttpBuilder {

    @Autowired
    private RestTemplate restTemplate;


    public <T> T postForObject(String url,Object data,Class<T> clazz)throws Exception{
        return restTemplate.postForObject(url,data,clazz);
    }

    public <T> ResponseEntity<T> postForEntity(String url, HttpEntity<String> entity, Class<T> clazz)throws Exception {
        return restTemplate.postForEntity(url, entity, clazz);
    }

    public <T> ResponseEntity<T> postForEntity(String url, MultiValueMap<String, Object> params, Class<T> clazz)throws Exception {
        return restTemplate.postForEntity(url, params, clazz);
    }


    public <T> ResponseEntity<T> getForEntity(String url, Class<T> responseType, Map<String, ?> uriVariables) throws Exception {
        return restTemplate.getForEntity(url, responseType, uriVariables);
    }

    public <T> T getForObject(String url,Class<T> clazz)throws Exception{
        return restTemplate.getForObject(url,clazz);
    }


    /**若以上方法无法满足需求，可以直接调用此方法获取restTemplate后调用
     * 它封装好的请求方法
      *@author jiajunchen
      *
      */
    public RestTemplate getRestTemplate(){
        return restTemplate;
    }

}
