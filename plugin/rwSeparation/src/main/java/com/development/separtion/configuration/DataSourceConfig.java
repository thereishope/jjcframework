package com.development.separtion.configuration;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.util.StringUtils;
import com.development.separtion.dataSourceHolder.DevDataSource;
import com.development.separtion.enums.DataBase;
import com.development.separtion.extend.SlaveDataSourceExtend;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jiajunchen
 * @title DataSourceConfig
 * @project rwSeparation
 */
@Configuration
public class DataSourceConfig {

    @Autowired(required = false)
    SlaveDataSourceExtend slaveDataSourceExtend;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationUtils applicationUtils;

    @Bean
    public ServletRegistrationBean druidServlet() {
        ServletRegistrationBean servletRegistrationBean = new ServletRegistrationBean(new StatViewServlet(), "/druid/*");
        try {
            servletRegistrationBean.addInitParameter("loginUsername", getProperty("dev.datasource.duridUserName"));
            servletRegistrationBean.addInitParameter("loginPassword", getProperty("dev.datasource.druidPwd"));
            servletRegistrationBean.addInitParameter("resetEnable", "false");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return servletRegistrationBean;
    }

    /**
     * @author jiajunchen
     */
    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }


    /**从当前激活环境中获取数据
     * @author jiajunchen
     * @date 2019-05-21
     */
    public String getProperty(String property) throws Exception {
        if (StringUtils.isEmpty(property)) {
            throw new Exception("DataSourceConfig property为空");
        }
        String propertyVal = applicationUtils.getContext().getEnvironment().getProperty(property);
        logger.info("propertyVal[" + propertyVal + "]");
        if (StringUtils.isEmpty(propertyVal)) {
            throw new Exception("DataSourceConfig property[" + property + "]为空");
        }
        return propertyVal;
    }

    /**
     *
     * @return
     */
    public DataSource masterDataSource() {
        DruidDataSource datasource = new DruidDataSource();
        try {
            datasource.setUrl(getProperty("spring.datasource.url"));
            datasource.setDriverClassName(getProperty("spring.datasource.driver-class-name"));
            datasource.setUsername(getProperty("spring.datasource.username"));
            datasource.setPassword(getProperty("spring.datasource.password"));
            datasource.setInitialSize(Integer.valueOf(getProperty("spring.datasource.initialSize")));
            datasource.setMinIdle(Integer.valueOf(getProperty("spring.datasource.minIdle")));
            datasource.setMaxWait(Long.valueOf(getProperty("spring.datasource.maxWait")));
            datasource.setMaxActive(Integer.valueOf(getProperty("spring.datasource.maxActive")));
            datasource.setMinEvictableIdleTimeMillis(Long.valueOf(getProperty("spring.datasource.minEvictableIdleTimeMillis")));
            datasource.setValidationQuery((getProperty("spring.datasource.validationQuery")));
            datasource.setTestWhileIdle(Boolean.valueOf(getProperty("spring.datasource.testWhileIdle")));
            datasource.setTestOnBorrow(Boolean.valueOf(getProperty("spring.datasource.testOnBorrow")));
            datasource.setTestOnReturn(Boolean.valueOf(getProperty("spring.datasource.testOnReturn")));
            datasource.setPoolPreparedStatements(Boolean.valueOf(getProperty("spring.datasource.poolPreparedStatements")));
            datasource.setFilters(getProperty("spring.datasource.filters"));
        } catch (Exception e) {
            return null;
        }
        return datasource;
    }

    /**
     * 注册数据源
     * setDefaultTargetDataSource设置默认数据源为master所在的数据源
     * 事务走master
     * @author jiajunchen
     */
    @Bean(name = "devDataSource")
    public DevDataSource devDataSource() {
        //主数据源
        DataSource master = masterDataSource();
        Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
        //从扩展接口重获取从数据源集合
        //如果未实现从数据源扩展接口，则默认不加载
        List<Map<DataBase, DataSource>> slaveDataSource = getSlaveDatasources();
        if (null != slaveDataSource && slaveDataSource.size() > 0) {
            for (Map<DataBase, DataSource> data : slaveDataSource) {
                if (null != data && data.size() > 0) {
                    targetDataSources.put(data.keySet().iterator().next(), data.values().iterator().next());
                }
            }
        }
        DevDataSource.set(targetDataSources);
        targetDataSources.put(DataBase.MASTER, master);
        DevDataSource dataSource = new DevDataSource();
        dataSource.setTargetDataSources(targetDataSources);
        //默认走主数据源，包括事务
        dataSource.setDefaultTargetDataSource(master);
        return dataSource;
    }

    /**
     * 记载从数据源集合
     * @return
     */
    public List<Map<DataBase, DataSource>> getSlaveDatasources(){
        return null != slaveDataSourceExtend ? slaveDataSourceExtend.getSlaveDataSourceList() : null;
    }
}
