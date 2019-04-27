package com.development.dev.controller.user;

import com.development.dev.controller.AbstractController;
import com.development.dev.domain.User.User;
import com.development.dev.model.Test;
import com.development.dev.service.user.UserTestService;
import com.development.dev.serviceMethodEnum.user.UserMethodEnums;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Properties;

/**
 * @author chenjiajun
 * @title UserController
 * @project dev
 * @date 2018-12-04
 */
@RestController
public class UserController extends AbstractController {


    @Autowired
    private UserTestService userTestService;

    /**
     * 登录
     *
     * @author chenjiajun
     * @date 2018-12-04
     */
    @HystrixCommand(
            commandKey = "testCommondKey",
            fallbackMethod = "failback",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE"),
                    @HystrixProperty(name = "execution.isolation.semaphore.maxConcurrentRequests", value = "1")
//                    @HystrixProperty(name = "circuitBreaker.enabled", value = "true"),
//                    @HystrixProperty(name = "circuitBreaker.requestVolumeThreshold", value = "2"),//超过100个
//                    @HystrixProperty(name = "circuitBreaker.errorThresholdPercentage", value = "50"),
//                    @HystrixProperty(name = "circuitBreaker.sleepWindowInMilliseconds", value = "5000"),
//                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "2000"),
//                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "10000")//命令执行超时时间
            })
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public DevResponse login(@RequestParam("name") String userName,
                             @RequestParam("pwd") String userPwd) {
//        Properties properties = JedisUtils.getProperties();
//        JedisUtils.getInstance().getJedisTemplate().set("a","b");
        DevResponse response = null;
        DevParamContainer container = getBaseContainer();
        container.setMethodEnum(UserMethodEnums.login);
        container.put("name", userName);
        container.put("pwd", userPwd);
        try {
            response = invoke(container);
        } catch (Exception e) {
            flushLogger(e);
        }
        return response;
    }

    public DevResponse failback(String userName,String userPwd){
        return new DevResponse(false,"0000","出错了",null);
    }







    /**
     * 用户添加
     *
     * @author chenjiajun
     * @date 2019-01-04
     */
    @RequestMapping(value = "/insertUser", method = RequestMethod.POST)
    public DevResponse insertUser(@RequestBody User user) {
        DevResponse response = null;
        DevParamContainer container = getBaseContainer();
        container.setMethodEnum(UserMethodEnums.insertUser);
        container.put("user", user);
        try {
            response = invoke(container);
        } catch (Exception e) {
            flushLogger(e);
        }
        return response;
    }


    /**
     * 登录
     *
     * @author chenjiajun
     * @date 2018-12-04
     */
    @RequestMapping(value = "/login2", method = RequestMethod.POST)
    public DevResponse login2(@RequestParam("name") String userName,
                                  @RequestParam("pwd") String userPwd) {
        DevResponse response = null;
        try {
            response = userTestService.login(userName, userPwd);
        } catch (Exception e) {
            flushLogger(e);
        }
        return response;
    }




    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public DevResponse test(String name,
                                int age) {
        Test test = new Test();
        test.setAge(age);
        test.setName(name);
        DevResponse response = new DevResponse(true,"0000","操作成功",test);
        return response;
    }

    @RequestMapping(value = "/test2", method = RequestMethod.POST)
    public DevResponse test2(Test test) {
        DevResponse response = new DevResponse(true,"0000","操作成功",test);
        return response;
    }

    @RequestMapping(value = "/test3", method = RequestMethod.POST)
    public DevResponse test3(@RequestBody Test test) {
        DevResponse response = new DevResponse(true,"0000","操作成功",test);
        return response;
    }



}
