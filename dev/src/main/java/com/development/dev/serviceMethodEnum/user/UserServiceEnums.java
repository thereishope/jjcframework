package com.development.dev.serviceMethodEnum.user;


import com.development.transfer.serviceMethod.Service;

/**
 * @author chenjiajun
 * @date 2018-12-04
 */
public enum UserServiceEnums implements Service {
    /**
     *
     */
    UserService("UserService")//用户服务
    ;

    private String serviceName;

    @Override
    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    private UserServiceEnums(String serviceName) {
        this.serviceName = serviceName;
    }


}
