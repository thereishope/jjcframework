package com.development.code.extension.defaultExcutor;
import com.development.code.extension.AbstractCodeCheckExcutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * @author  palading_cr@163.com
 * @title DefaultCodeCheckExtender
 * @devCheck
 */
public class DefaultCodeCheckExcutor extends AbstractCodeCheckExcutor {

    public static Map<String, TreeMap> ctrMap = new HashMap<>();

    private static Logger logger = LoggerFactory.getLogger(DefaultCodeCheckExcutor.class);



    public DefaultCodeCheckExcutor() {

    }


    @Override
    public Map<String, TreeMap> getExtendCheckDetail() {
        return ctrMap;
    }

    /**获取controller方法上的注解信息
     * 排出spring自带controller
     *@author palading_cr@163.com
     *
     */
    @Override
    public void extendExcute() throws Exception {
        logger.info("DefaultCodeCheckExtender[extendExcute]执行控制层代码检查开始");
        TreeMap<String, List<MethodInfo>> treeMap = new TreeMap<String, List<MethodInfo>>();
        Environment env = super.context.getEnvironment();
        logger.info("DefaultCodeCheckExtender[extendExcute]:"+env.getProperty("dev.method.ignore.list"));
        Map<RequestMappingInfo, HandlerMethod> mappings =
                super.requestMappingHandlerMapping.getHandlerMethods();
        logger.info("DefaultCodeCheckExtender[extendExcute]:"+mappings.size());
        List<String> ignoreList = getIgnoreList(env);
        for (Map.Entry<RequestMappingInfo, HandlerMethod> mapping
                : mappings.entrySet()) {
            final RequestMappingInfo info = mapping.getKey();
            HandlerMethod method = mapping.getValue();
            String className = method.getMethod()
                    .getDeclaringClass()
                    .getName();
            if (!StringUtils.isEmpty(className) &&
                    className.startsWith("org.springframework")) {
                continue;
            }
            if (null != ignoreList && ignoreList.size() > 0) {
                if (ignoreList.contains(method.getMethod().getDeclaringClass()
                        .getSimpleName().concat("/*"))) {
                    continue;
                }
                if (ignoreList.contains(method.getMethod().getDeclaringClass()
                        .getSimpleName()
                        .concat(info.getPatternsCondition()
                                .getPatterns().iterator().next()))) {
                    continue;
                }
            }
            addMethod(treeMap, method, info);
            addFile(treeMap,checkFilePath(env));
        }
        logger.info("DefaultCodeCheckExtender[extendExcute]执行控制层检查完毕");
    }

    /**
     * 向list容器添加方法信息
     *
     * @author palading_cr@163.com
     */
    private void addMethod(TreeMap<String, List<MethodInfo>> treeMap,
                           final HandlerMethod method, final RequestMappingInfo info) {

        if (treeMap.containsKey(method.getMethod().getDeclaringClass()
                .getSimpleName())) {
            treeMap.get(method.getMethod().getDeclaringClass()
                    .getSimpleName()).add(
                    new MethodInfo(info.getPatternsCondition().toString(),
                            method.getMethod().getName(),
                            info.getMethodsCondition().toString(),getParams(method)));
        } else {
            treeMap.put(method.getMethod().getDeclaringClass()
                    .getSimpleName(), new ArrayList<MethodInfo>() {
                {
                    add(new MethodInfo(info.getPatternsCondition().toString(),
                            method.getMethod().getName(),
                            info.getMethodsCondition().toString(),getParams(method)));
                }
            });
        }
    }


    private String getParams(HandlerMethod method){
        StringBuffer buffer  = new StringBuffer();
        Method method1 = method.getMethod();
        Parameter[] params = method1.getParameters();
        for(Parameter parameter : params){
            buffer.append("<!#");
            buffer.append(parameter.getType().getSimpleName()+":"+"["+parameter.getName()+"]");
            buffer.append("/>");
        }
        return buffer.toString();

    }


    /**
     * 获取排出方法集合并将中文逗号转换成英文逗号
     *
     * @author palading_cr@163.com
     */
    private List<String> getIgnoreList(Environment env) {
        List<String> ignoreList = null;
        String ignore = env.getProperty("dev.controller.ignore.list");
        if (!StringUtils.isEmpty(ignore)) {
            ignoreList = Arrays.asList(ignore.replaceAll("，", ",").split(","));
        }
        return ignoreList;
    }

    public String checkFilePath(Environment env){
        return null != env.getProperty(
                "dev.controller.checkViewPath")?
                env.getProperty("dev.controller.checkViewPath"):"/usr/check/controller.check.json";
    }
}
