package com.development.dev.ext;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.util.StringUtils;
import com.development.separtion.configuration.ApplicationUtils;
import com.development.separtion.enums.DataBase;
import com.development.separtion.extend.SlaveDataSourceExtend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**加载从数据源，如果不实现或者getSlaveDataSourceList为空，则默认使用主数据源
 *  (可选)
 * @author jiajunchen
 * @title SlaveDataSourceExample
 * @project rwSeparation
 */
@Component
public class SlaveDataSourceExample implements SlaveDataSourceExtend {

    @Autowired
    private ApplicationUtils applicationUtils;

    /**
     * 加载从数据源
     * @return
     */
    @Override
    public List<Map<DataBase, DataSource>> getSlaveDataSourceList() {
        List<Map<DataBase, DataSource>> dataSourceList = new ArrayList<Map<DataBase, DataSource>>();
        dataSourceList.add(slaveDataSource1Example());
        dataSourceList.add(slaveDataSource2Example());
        return dataSourceList;
    }

    public Map<DataBase,DataSource> slaveDataSource1Example() {
        Map<DataBase,DataSource> slave = new HashMap<DataBase,DataSource>();
        DruidDataSource datasource = new DruidDataSource();
        try {
            datasource.setUrl(getProperty("spring.slave[1].datasource.url"));
            datasource.setDriverClassName(getProperty("spring.slave[1].datasource.driver-class-name"));
            datasource.setUsername(getProperty("spring.slave[1].datasource.username"));
            datasource.setPassword(getProperty("spring.slave[1].datasource.password"));
            datasource.setInitialSize(Integer.valueOf(getProperty("spring.slave[1].datasource.initialSize")));
            datasource.setMinIdle(Integer.valueOf(getProperty("spring.slave[1].datasource.minIdle")));
            datasource.setMaxWait(Long.valueOf(getProperty("spring.slave[1].datasource.maxWait")));
            datasource.setMaxActive(Integer.valueOf(getProperty("spring.slave[1].datasource.maxActive")));
            datasource.setMinEvictableIdleTimeMillis(Long.valueOf(getProperty("spring.slave[1].datasource.minEvictableIdleTimeMillis")));
            datasource.setValidationQuery((getProperty("spring.slave[1].datasource.validationQuery")));
            datasource.setTestWhileIdle(Boolean.valueOf(getProperty("spring.slave[1].datasource.testWhileIdle")));
            datasource.setTestOnBorrow(Boolean.valueOf(getProperty("spring.slave[1].datasource.testOnBorrow")));
            datasource.setTestOnReturn(Boolean.valueOf(getProperty("spring.slave[1].datasource.testOnReturn")));
            datasource.setPoolPreparedStatements(Boolean.valueOf(getProperty("spring.slave[1].datasource.poolPreparedStatements")));
            datasource.setFilters(getProperty("spring.slave[1].datasource.filters"));
            slave.put(DataBase.SLAVE1,datasource);
        } catch (Exception e) {
            return null;
        }
        return slave;
    }


    public Map<DataBase,DataSource> slaveDataSource2Example() {
        Map<DataBase,DataSource> slave = new HashMap<DataBase,DataSource>();
        DruidDataSource datasource = new DruidDataSource();
        try {
            datasource.setUrl(getProperty(""));
            datasource.setDriverClassName(getProperty(""));
            datasource.setUsername(getProperty(""));
            datasource.setPassword(getProperty(""));
            datasource.setInitialSize(Integer.valueOf(getProperty("")));
            datasource.setMinIdle(Integer.valueOf(getProperty("")));
            datasource.setMaxWait(Long.valueOf(getProperty("")));
            datasource.setMaxActive(Integer.valueOf(getProperty("")));
            datasource.setMinEvictableIdleTimeMillis(Long.valueOf(getProperty("")));
            datasource.setValidationQuery((getProperty("")));
            datasource.setTestWhileIdle(Boolean.valueOf(getProperty("")));
            datasource.setTestOnBorrow(Boolean.valueOf(getProperty("")));
            datasource.setTestOnReturn(Boolean.valueOf(getProperty("")));
            datasource.setPoolPreparedStatements(Boolean.valueOf(getProperty("")));
            datasource.setFilters(getProperty(""));
            slave.put(DataBase.SLAVE2,datasource);
        } catch (Exception e) {
            return null;
        }
        return slave;
    }

    /**
     * @author jiajunchen
     * @date 2019-05-21
     */
    public String getProperty(String property) throws Exception {
        if (StringUtils.isEmpty(property)) {
            throw new Exception("never mind if not exists slave datasource!!!property["+property+"] is null");
        }
        String propertyVal = applicationUtils.getContext().getEnvironment().getProperty(property);
        if (StringUtils.isEmpty(propertyVal)) {
            throw new Exception("never mind if not exists slave datasource!!!property["+property+"] is null");
        }
        return propertyVal;
    }
}
