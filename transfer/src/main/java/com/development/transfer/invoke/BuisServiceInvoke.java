package com.development.transfer.invoke;

import com.development.transfer.container.DevParamContainer;

/**
 * @author jiajunchen
 * @title BuisServiceInvoke
 * @project transfer
 * @date 2018-12-29
 */
public interface BuisServiceInvoke {

    public <T> T excute(DevParamContainer container, Class<T> t) throws Exception;

}
