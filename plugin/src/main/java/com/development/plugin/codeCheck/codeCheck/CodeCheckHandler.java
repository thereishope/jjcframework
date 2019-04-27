package com.development.plugin.codeCheck.codeCheck;
import com.development.transfer.proxy.CommonHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.util.*;

/**
 * chenjiajun
 * @title CodeCheckHandler
 * @project plugin
 * @date 2019-03-16
 */
public class CodeCheckHandler implements CommonHandler {

    private  Logger logger = LoggerFactory.getLogger(getClass());

    private ApplicationContext context;

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    public static Map<String,TreeMap> ctrMap = new HashMap<>();


    public CodeCheckHandler(ApplicationContext context,
                            RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.context = context;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }


    /**校验是是否需要进行检查
     * standar.method.check为true，进行检查
      *chenjiajun
      *@date 2019-03-16
      *
      */
    public boolean allowance() throws Exception {
        Environment env = context.getEnvironment();
        return null != env.getProperty("standar.method.check", boolean.class) &&
                env.getProperty("standar.method.check", boolean.class)&& !"product".equals(env.getProperty("spring.profiles.active"));
    }



    /**获取controller方法上的注解信息
     * 排出spring自带controller
     * 如果设置standar.method.ignore.list，则不对设置的方法进行检查
      *chenjiajun
      *@date 2019-03-16
      *
      */
    public void handle() throws Exception {
        logger.info("CodeCheckHandler[handle]执行控制层代码检查开始");
        Environment env = this.context.getEnvironment();
        Map<RequestMappingInfo, HandlerMethod> mappings =
                requestMappingHandlerMapping.getHandlerMethods();
        TreeMap<String, List<MethodInfo>> treeMap = new TreeMap<String, List<MethodInfo>>();
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
        }
        ctrMap.put("ctrCode",treeMap);
        logger.info("CodeCheckHandler[handle]执行控制层检查完毕");
    }


    /**向list容器添加方法信息
      *chenjiajun
      *@date 2019-03-16
      *
      */
    private void addMethod(TreeMap<String, List<MethodInfo>> treeMap,
                           final HandlerMethod method, final RequestMappingInfo info) {
        if (treeMap.containsKey(method.getMethod().getDeclaringClass()
                .getSimpleName())) {
            treeMap.get(method.getMethod().getDeclaringClass()
                    .getSimpleName()).add(
                    new MethodInfo(info.getPatternsCondition().toString(),
                            method.getMethod().getName(),
                            info.getMethodsCondition().toString()));
        } else {
            treeMap.put(method.getMethod().getDeclaringClass()
                    .getSimpleName(), new ArrayList<MethodInfo>() {
                {
                    add(new MethodInfo(info.getPatternsCondition().toString(),
                            method.getMethod().getName(),
                            info.getMethodsCondition().toString()));
                }
            });
        }
    }


    /**获取排出方法集合并将中文逗号转换成英文逗号
      *chenjiajun
      *@date 2019-03-16
      *
      */
    private List<String> getIgnoreList(Environment env) {
        List<String> ignoreList = null;
        String ignore = env.getProperty("standar.method.ignore.list");
        if (!StringUtils.isEmpty(ignore)) {
            ignoreList = Arrays.asList(ignore.replaceAll("，", ",").split(","));
        }
        return ignoreList;
    }

    private void addCheckFile(TreeMap<String, List<MethodInfo>> treeMap, String path) {
        if(null != treeMap && treeMap.size()>0){
//            FileTool.createJsonFile(treeMap, StringUtils.isEmpty(path)
//                    ? "/usr/" : path, "method_check");
        }

    }
}

class MethodInfo {

    private String mapping;

    private String methodName;

    private String methodType;

    public MethodInfo(String mapping, String methodName, String methodType) {
        this.mapping = mapping;
        this.methodName = methodName;
        this.methodType = methodType;
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
}
