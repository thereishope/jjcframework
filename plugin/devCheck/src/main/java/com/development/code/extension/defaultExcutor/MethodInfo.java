package com.development.code.extension.defaultExcutor;

/**
 * @author  palading_cr@163.com
 * @title MethodInfo
 * @devCheck
 */
class MethodInfo {

    private String mapping;

    private String methodName;

    private String methodType;

    private String parameter;

    public MethodInfo(String mapping, String methodName, String methodType,String parameter) {
        this.mapping = mapping;
        this.methodName = methodName;
        this.methodType = methodType;
        this.parameter = parameter;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
