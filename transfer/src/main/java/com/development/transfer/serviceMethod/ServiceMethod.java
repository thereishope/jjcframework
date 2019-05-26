package com.development.transfer.serviceMethod;

/**
 * 获取业务方法抽象
 * 便于独立项中不同业务枚举
 *
 * @author jiajunchen
 * @date 2018-12-03
 */
public interface ServiceMethod {


    Service getServiceEnums();

    String getServiceMethodName();


}
