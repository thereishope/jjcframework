package com.development.dev.common.async;

/**
 * @author jiajunchen
 * @title CommonAsyncHandler
 * @project fastdev
 * @date 2019-05-06
 */
public interface CommonAsyncHandler<T> {

    public String getAsyncCode();

    public <T> T handle(T t) throws Exception;

}
