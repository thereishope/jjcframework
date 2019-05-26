package com.development.separtion.inteceptor;

/**
 * @author jiajunchen
 * @title SeparationInteceptor
 * @project rwSeparation
 * @date 2019-05-21
 */
import com.development.separtion.anno.Master;
import com.development.separtion.dataSourceHolder.DevDataSource;
import com.development.separtion.enums.DataBase;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 利用mybatis拦截器动态设置数据源
 * 判断是否存在master注解，如果存在则强制走主数据源
 * 已废弃
 */
@Deprecated
//@Component
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class SeparationInteceptor implements Interceptor {

    public static Map<String, DataBase> map = new HashMap<>();

    private  Lock lock = new ReentrantLock();

    private Logger logger = LoggerFactory.getLogger(getClass());

    public  DataBase getCachedDataSource(String className, String namespace) {
        try {
            if (map.containsKey(namespace)) {
                return map.get(namespace);
            } else {
                Method[] ms = Class.forName(className).getMethods();
                lock.lock();
                for (Method m : ms) {
                    if (m.getName().startsWith("select") || m.getName().startsWith("get")) {
                        Annotation annotation = m.getAnnotation(Master.class);
                        if (null != annotation) {
                            map.put(className.concat(".").concat(m.getName()), DataBase.MASTER);
                        } else {
                            map.put(className.concat(".").concat(m.getName()), DevDataSource.getSlaveRondom());
                        }
                    }
                }
                DataBase dataBase = map.get(namespace);
                logger.info("SeparationInteceptor[getCachedDataSource]dataBase["+dataBase+"] ");
                return dataBase;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return DataBase.MASTER;
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        final Object[] args = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) args[0];
        String namespace = mappedStatement.getId();
        String className = namespace.substring(0, namespace.lastIndexOf("."));
        String methedName = namespace.substring(namespace.lastIndexOf(".") + 1, namespace.length());
        if (methedName.startsWith("insert") || methedName.startsWith("update") || methedName.startsWith("delete")) {
            DevDataSource.setMaster();
        } else {
            DevDataSource.setDataBase(getCachedDataSource(className, namespace));
        }
        logger.info("SeparationInteceptor[intercept]获取到的namespace[" + namespace + "]," +
                "className[" + className + "],methodName[" + methedName + "],dataBaseHolder[" + DevDataSource.getDataBase() + "]");
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
