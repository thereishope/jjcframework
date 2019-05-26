package com.development.dev.ext;
import com.development.separtion.anno.Master;
import com.development.separtion.dataSourceHolder.DevDataSource;
import com.development.transfer.invoke.RwSeparationExtend;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**读写分离规则
 * 根据分离规则选择设置数据源
 * 如果不实现，则增删改查都走主数据源
 *  (可选)
 * @author jiajunchen
 * @title DataSourceExtender
 * @project fastdev
 * @date 2019-05-22
 */
@Component
public class RwSepartionExample implements RwSeparationExtend {

    //查询
    private static String PREFIX_METHOD_SELECT = "select";

    //查询
    private static String PREFIX_METHOD_GET = "query";

    //修改
    private static String PREFIX_METHOD_UPDATE = "update";

    //修改
    private static String PREFIX_METHOD_MODIFY = "modify";

    //添加
    private static String PREFIX_METHOD_ADD = "add";

    //添加
    private static String PREFIX_METHOD_INSERT = "insert";

    //删除
    private static String PREFIX_METHOD_DELETE = "delete";

    public void setDataSource(Method method) {
        String methodName = method.getName();
        if ((methodName.startsWith(PREFIX_METHOD_SELECT)
                || methodName.startsWith(PREFIX_METHOD_GET)) && null == method.getAnnotation(Master.class)) {
            DevDataSource.setSlaveRondom();
            return;
        }
        Master methodAnnotation = method.getAnnotation(Master.class);
        if (null != methodAnnotation) {
            DevDataSource.setMaster();
            return;
        }
        if (methodName.startsWith(PREFIX_METHOD_UPDATE)
                || methodName.startsWith(PREFIX_METHOD_MODIFY)
                || methodName.startsWith(PREFIX_METHOD_ADD)
                || methodName.startsWith(PREFIX_METHOD_INSERT) || methodName.startsWith(PREFIX_METHOD_DELETE)) {
            DevDataSource.setMaster();
            return;
        }

    }
}
