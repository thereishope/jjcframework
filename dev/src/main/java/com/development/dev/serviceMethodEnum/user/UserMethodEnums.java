package com.development.dev.serviceMethodEnum.user;

import com.development.transfer.serviceMethod.Service;
import com.development.transfer.serviceMethod.ServiceMethod;
/**
 * @author chenjiajun
 * @date 2018-12-04
 */
public enum UserMethodEnums implements ServiceMethod {
    /**
     *
     */
    insertUser(UserServiceEnums.UserService, "用户新增", "insertUser"),
    /**
     *
     */
    login(UserServiceEnums.UserService, "用户登陆", "login");

    @Override
    public Service getServiceEnums() {
        return serviceEnums;
    }

    @Override
    public String getServiceMethodName() {
        return serviceMethodName;
    }

    private UserServiceEnums serviceEnums;


    //具体service
    private String serviceName;

    /**
     * 具体service方法
     */
    private String serviceMethodName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setServiceEnums(UserServiceEnums serviceEnums) {
        this.serviceEnums = serviceEnums;
    }

    public void setServiceMethodName(String serviceMethodName) {
        this.serviceMethodName = serviceMethodName;
    }

    private UserMethodEnums(UserServiceEnums serviceEnums,
                            String serviceName, String serviceMethodName) {
        this.serviceEnums = serviceEnums;
        this.serviceName = serviceName;
        this.serviceMethodName = serviceMethodName;
    }


}
