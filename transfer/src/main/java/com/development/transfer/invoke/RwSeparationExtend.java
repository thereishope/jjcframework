package com.development.transfer.invoke;

import java.lang.reflect.Method;

/**
 * @author jiajunchen
 * @title RwSeparationExtend
 * @project adapter
 * @date 2019-05-22
 */
public interface RwSeparationExtend<T extends Method> {

    /**
      *@author jiajunchen
      *@date 2019-05-22
      *
      */
    public <T extends Method> void setDataSource(T t);
}
