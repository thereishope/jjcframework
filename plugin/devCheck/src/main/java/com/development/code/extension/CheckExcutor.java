package com.development.code.extension;

import java.util.Map;
import java.util.TreeMap;

/**
 * @author  palading_cr@163.com
 * @title CodeCheckExtender
 * @devCheck
 * @date 2019-05-05
 */
public interface CheckExcutor {

    public Map<String, TreeMap> getExtendCheckDetail();

    public void extendExcute()throws Exception;

}
