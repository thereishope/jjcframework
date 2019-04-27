package com.development.dev.service.user;


import com.development.transfer.respconse.DevResponse;

/**
 * @author chenjiajun
 * @title UserTestService
 * @project dev
 * @date 2019-01-04
 */
public interface UserTestService {

    public DevResponse login(String name, String pwd) throws Exception;
}
