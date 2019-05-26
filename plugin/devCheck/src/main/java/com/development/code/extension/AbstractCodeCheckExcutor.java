package com.development.code.extension;

import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author palading_cr@163.com
 * @title AbstractCodeCheckExtender
 * @devCheck
 */
public abstract class AbstractCodeCheckExcutor implements CheckExcutor {

    protected ApplicationContext context;

    protected RequestMappingHandlerMapping requestMappingHandlerMapping;

    public ApplicationContext getContext() {
        return context;
    }

    public void setContext(ApplicationContext context) {
        this.context = context;
    }

    public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
        return requestMappingHandlerMapping;
    }

    public void setRequestMappingHandlerMapping(RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    protected void addFile(TreeMap treeMap,String filePath){
        File file = new File(filePath);
        FileWriter writer = null;
        try {
            writer = new FileWriter(filePath);
            writer.write("");//清空原文件内容
            writer.write(maptoString(treeMap));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if(null != writer){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**map转string
      *@author palading_cr@163.com
      *
      */
    private String maptoString(TreeMap map){
        StringBuffer buffer = new StringBuffer();
        if(null != map && map.size()>0){
            Iterator iter = map.entrySet().iterator();
            while (iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                buffer.append(entry.getKey()+" : "+entry.getValue());
            }
        }
        return buffer.toString();

    }
}
