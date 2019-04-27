package com.development.transfer.invoke;

import com.development.transfer.container.DevParamContainer;

/**
 * @author chenjiajun
 * @title BuisServiceInvoke
 * @project transfer
 * @date 2019-01-13
 */
public interface BuisServiceInvoke {

    public <T> T excute(DevParamContainer container, Class<T> t) throws Exception;

}
