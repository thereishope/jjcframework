# jjcframework
## 前言

   在现实工作中，我们需要一个什么样的开发框架？
   ## 
   ![image](https://github.com/thereishope/gitconfig/blob/master/repo/timg.jpg)
   ## 
   **能促进团队代码风格统一**、**扩展性强**、**有点思想**、**性能得不差吧**、**使用简单**、**最好能生发**
   ![image](https://github.com/thereishope/gitconfig/blob/master/repo/timg2.jpg)
   
## 特点
  jjcframework框架包含(**dev**,**transfer**,**plugin**,**tool**)，它有如下**特点**

  - 统一传参样式以及返回参数(可自由定制)，统一异常处理，开发人员可更专注业务开发，且统一风格后减小维护成本
  - 无需aop、拦截器等即可完成请求前置及后置处理，提升开发便利性的同时提升了业务响应速度
  - 提供扩展点，实现扩展接口，自由定制项目需求
  - 在吞吐量及响应速度相对于spring传统调用方式均有小幅提升
  - 按需插拔，可自由选择需要集成的模块
  - 简单易用，高度封装
  - 核心脚手架稍作修改仍然兼容spring 3
## 项目环境介绍
  jdk:1.8+(plugins需要使用stream流进行并行计算,其他模块均可使用jdk1.7进行编译)
  spring boot:1.5.4（未使用spring boot 2.0，主要考虑到部分项目仍然使用jdk7）
## 模块简介
  注:后续将以中文名称进行描述
  - dev:开发脚手架
  - transfer:适配层
  - plugin:插件层(读写分离,httpPlugin,codeCheck)
  - tool:工具层
## 如何使用
  - 确保已安装maven3.+
  - 编译工具层、插件层、适配层、脚手架（maven clean install）
  - 启动脚手架(idea：鼠标右键脚手架**DevApplication**，点击Run DevApplication.main(),或者在控制台使用java -jar命令等)
  
## 扩展点（可选）
  - BuisInterceptoHander（请求前置及后置处理以及异常转换）
  - BuisServiceInvoke （业务方法执行接口）
  - RwSeparationExtend（读写分离策略以及强制读写策略）
  - SlaveDataSourceExtend（从数据源加载）
  - Strategy（http工具监控策略）
  - CheckExcutor（代码检查策略）
  以上接口已进行默认实现，可替换默认实现
## 代码细节
### 1.脚手架及适配层
  - 注：有效提升脚手架吞吐量（容器启动后提前加载BuisServiceInvoke所有实现；缓存业务层方法信息；利用请求前置及后置处理避免aop）
  ![image](https://github.com/thereishope/gitconfig/blob/master/repo/invoke.png)
#### 1.1 controller中获取参数容器及设置业务层元信息细节
```java
@RequestMapping(value = "/login", method = RequestMethod.POST)
    public DevResponse login(@RequestParam("name") String userName,
                             @RequestParam("pwd") String userPwd) {
        DevResponse response = null;
        DevParamContainer container = getBaseContainer();
        container.setMethodEnum(UserMethodEnums.login);
        container.put("name", userName);
        container.put("pwd", userPwd);
        try {
            response = invoke(container);
        } catch (Exception e) {
            flushLogger(e);
        }
        return response;
    }
    DevParamContainer类中持有ServiceMethod接口，而所有业务层对象枚举均实现ServiceMethod接口，这样不同业务层枚举均可独立编写，便于管理
```
  #### 1.2 请求前置处理，便于对请求进行加工
    /**
     * 初始化参数容器
     * @author chenjiajun
     * @date 2019-01-13
     */
      public DevParamContainer getBaseContainer(){
        //通过request添加基础信息，例如放置在header里的token,用户ip，操作时间等
        DevParamContainer container = new DevParamContainer(){
            {
                put("operateTime",new Date());
                put("token",request.getHeader("x-token"));
            }
        };
        return container;
    }
     /**
     * 发起调用
     * @author chenjiajun
     * @date 2019-01-13
     */
     public DevResponse invoke(DevParamContainer container)throws Exception {
        DevResponse res = null;
        res = serviceInvoke.doInvoke(container, DevResponse.class);
        return res;
    }
  #### 1.3 通过参数容器中的方法元信息进行适配调用，并提供扩展接口实现请求前置及后置处理（见开发层ext包）并对异常进行统一处理，另外结合项目需要可对返回值进行定制
  ```java
  @Component
public class DevServiceInvoke{

    private static Logger logger = LoggerFactory.getLogger(DevServiceInvoke.class);

    @Autowired
    Environment environment;


    @Autowired(required = false)
    private BuisInterceptoHander buisInterceptoHander;

    /**
     * 统一调用业务层方法，并实现异常统一处理
     * 并实现适配调用
     * @author jiajunchen
     * @date 2019-4-18
     */
    public <T> T doInvoke(DevParamContainer param, Class<T> t) throws Exception {
        T res = null;
        try {
            //前置处理
            preHandle(param);
            BuisServiceInvoke service = ApplicationWrapBuilder.getBean(
                    param.getServiceMethod()
                            .getServiceEnums()
                            .getServiceName(),
                    BuisServiceInvoke.class);
            //处理业务请求
            res = (T)service.excute(param, t);
            //请求后置处理
            afterHandle(t, param);
        } catch (Exception e) {
            //定位/打印错误日志
            logerExport(param, e);
            //默认异常转换
            if (defaultResponse()) {
                return (T) getExResponse(e, t);
            }
            //自定义异常逻辑
            return (T) getExtendResponse(e, t);
        }
        return res;
    }

    /**自定义异常转换
     *@author jiajunchen
     *@date 2019-05-13
     *
     */
    private <T> T getExtendResponse(Exception e, Class<T> t) throws Exception {
        return null != buisInterceptoHander ? (T) buisInterceptoHander.getExResponse(e, t) : null;
    }

    /**
     * 请求前置处理
     *
     * @author jiajunchen
     * @date 2019-05-13
     */
    private BuisInterceptoHander preHandle(DevParamContainer container) throws Exception {
        return null != buisInterceptoHander ? buisInterceptoHander.preHandle(container) : null;
    }

    /**
     * 请求后置处理
     *
     * @author jiajunchen
     * @date 2019-05-13
     */
    private void afterHandle(Class t, DevParamContainer container) throws Exception {
        if (null != buisInterceptoHander) {
            buisInterceptoHander.afterHandle(t, container);
        }
    }

    /**
     * 判断是否使用默认内置的返回值样例
     *
     * @author jiajunchen
     * @date 2019-05-13
     */
    private boolean defaultResponse() {
        return null == environment.getProperty("dev.res.default", Boolean.class)
                || environment.getProperty("dev.res.default", Boolean.class);
    }



    private <T> T getExResponse(Exception e, Class<T> t) {
        return (T) getResponse(e);
    }

    private <T> T getExResponse(Class<? extends Throwable> e, Class<T> t) {
        return null;
    }
``` 
  ####  1.4 调用目标业务层,所有业务层均需继承AbstractService并实现BuisServiceInvoke,这样请求就可以适配到具体业务类进行调用
```java
public interface BuisServiceInvoke {

    public <T> T excute(DevParamContainer container, Class<T> t) throws Exception;

}

public abstract class AbstractService implements BuisServiceInvoke {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired(required = false)
    private RwSeparationExtend rwSeparationExtend;

    /**发起service层请求
      *@author jiajunchen
      *@date 2019-05-13
      *
      */
   @Transactional(rollbackFor = Exception.class)
    public <T> T excute(DevParamContainer paraMap, Class<T> t) throws Exception {
        T res = null;
        Method method = null;
        method = getMethodCache(paraMap);
            if (null != method) {
                setDataSourceExt(method);
                res = (T) method.invoke(this, paraMap);
            }
        return res;
    }

    /**加载读写分离规则，并根据规则设置数据源
     *在dev工程中进行扩展
      *@author jiajunchen
      *@date 2019-05-26
      */
    private void setDataSourceExt(Method method){
        if(null != rwSeparationExtend){
            rwSeparationExtend.setDataSource(method);
        }
    }

    /**
     * 内存缓存方法反射对象
     * 提高调用效率
     * @author jiajunchen
     * @date 2019-04-24
     */
    private Method getMethodCache(DevParamContainer paraMap) {
        String methodKey = paraMap.getServiceMethod().getServiceEnums()
                .getServiceName()
                + "_"
                + paraMap.getServiceMethod().getServiceMethodName()
                .concat("key");
        Method methodCache = MethodCache.getMehod(methodKey);
    
```
### 2.插件层

  注：插件层主要集成依赖了spring的业务工具，例如mybatis以及RestTemplate，且需要对脚手架提供扩展接口，进行业务定制化开发
#### 2.1 装配
  需要在resource下建立META-INF文件夹提供spring.factories及spring.providers进行bean的装配
#### 2.2 http监控
  - 提供一个可对外扩展的策略接口
```java
/**策略接口
 * @author chenjiajun
 * @title StrategyHandlerFactory
 * @project request
 * @date 2019-03-16
 */
public interface Strategy {

	//获取执行策略
    public String getStrategy();
	//执行
    public <T> void excute(T t);
	//获取监控分析结果
    public String getAnalysisMetrics()throws Exception;

}
```
- 在脚手架层通过java SPI机制（在resource/META-INF下建spi文件）织入策略，并且实现Strategy接口，在插件层利用ServiceLoader进行调用
```java
 public void handleMetrics(MetricsTimeModel metricsTimeModel) {
        try {
            ServiceLoader<Strategy> serviceLoader = ServiceLoader.load(Strategy.class);
            if (null != serviceLoader && !serviceLoaderMap.containsKey(SERVICE_LOADER_KEY)) {
                serviceLoaderMap.put(SERVICE_LOADER_KEY, serviceLoader);
            }
            ExecutorService pool = Executors.newFixedThreadPool(2);
            pool.execute(new AsyncMetricsExcute(metricsTimeModel));
        } catch (Exception e) {
            logger.error("MetricsStrategy[handleMetrics]发生异常", e);
        }

    }

    /**
     * 异步定长线程处理短任务
     *
     * @author chenjiajun
     * @date 2019-04-24
     */
    class AsyncMetricsExcute implements Runnable {

        private MetricsTimeModel metricsTimeModel;

        public AsyncMetricsExcute(MetricsTimeModel metricsTimeModel) {
            this.metricsTimeModel = metricsTimeModel;
        }

        @Override
        public void run() {
            logger.info("MetricsStrategy[handleMetrics]获取到的strategy[" + RestTemplateWrap.
                    strategyMap.get("strategy") + "]");
            Object o = RestTemplateWrap.strategyMap.get("strategy");
            String strategyName = null != o ? String.valueOf(o) : "detail";
            ServiceLoader<Strategy> serviceLoader = serviceLoaderMap.get(SERVICE_LOADER_KEY);
            for (Strategy strategy : serviceLoader) {
                logger.info("MetricsStrategy[handleMetrics]==" + strategy.getStrategy());
                if (strategyName.equals(strategy.getStrategy())) {
                    strategy.excute(metricsTimeModel);
                }
            }
        }
    }
```
#### 2.3读写分离
```java
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
 ```
 #### 2.4代码检查
```java
public class CheckHandler implements CommonHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public static Map<String, TreeMap> ctrMap = new HashMap<>();

    private final RequestMappingHandlerMapping requestMappingHandlerMapping;

    private ApplicationContext context;

    public CheckHandler(ApplicationContext context,
                        RequestMappingHandlerMapping requestMappingHandlerMapping) {
        this.context = context;
        this.requestMappingHandlerMapping = requestMappingHandlerMapping;
    }

    /**
     *@author jiajunchen
     *@date 2019-05-05
     *
     */
    public void handle() throws Exception {
        ServiceLoader<CheckExcutor> serviceLoader = ServiceLoader.load(CheckExcutor.class);
        if(!serviceLoader.iterator().hasNext() ){
            defaultExcute();
            return;
        }
        spiExcute();
    }

    /**
     * 校验是是否需要进行检查
     * @author palading_cr@163.com
     * @date 2019-04-23
     */
    public boolean allowance() throws Exception {
        Environment env = context.getEnvironment();
        return null != env.getProperty("dev.controller.method.check", boolean.class) &&
                env.getProperty("dev.controller.method.check", boolean.class) && !"product".equals(env.getProperty("spring.profiles.active"));
    }

    /**支持编写不同扩展处理器执行代码检查逻辑
     * 在开发层需添加spi
      *@author jiajunchen
      *@date 2019-05-05
      *
      */
    private void spiExcute()throws Exception{
        ServiceLoader<CheckExcutor> serviceLoader = ServiceLoader.load(CheckExcutor.class);
        logger.info("spiExcute扩展处理器执行了");
        for (CheckExcutor extender : serviceLoader) {
            AbstractCodeCheckExcutor abstractCodeCheckExtender = (AbstractCodeCheckExcutor) extender;
            abstractCodeCheckExtender.setContext(context);
            abstractCodeCheckExtender.setRequestMappingHandlerMapping(requestMappingHandlerMapping);
            abstractCodeCheckExtender.extendExcute();
        }
    }

    /**如果开发层未使用spi注入接口实现，则使用默认内置的扩展处理器
      *@author jiajunchen
      *
      */
    private void defaultExcute()throws Exception{
        logger.info("默认内置的扩展处理器执行了");
        DefaultCodeCheckExcutor defaultCodeCheckExtender = new DefaultCodeCheckExcutor();
        defaultCodeCheckExtender.setContext(context);
        defaultCodeCheckExtender.setRequestMappingHandlerMapping(requestMappingHandlerMapping);
        defaultCodeCheckExtender.extendExcute();
    }
 ```
### 3.工具层
 - 注(工具层主要集成与spring无关的业务辅助类，例如json工具类，等)
 - 若需要读取spring 配置文件：
```java

public class CommonHandlerProxy implements CommonHandler{

    private CommonHandler commonHandler;
    public CommonHandlerProxy(CommonHandler commonHandler) {
        this.commonHandler = commonHandler;
    }
    
    public boolean allowance() throws Exception {
       return  commonHandler.allowance();
    }
    
    public void handle() throws Exception {
        commonHandler.handle();
    }
    
    public void excute()throws Exception{
        if(allowance()){
            handle();
        }
    }
}

/**
 * 工具类加载spring环境配置
 * 后续将适配基于配置中心的配置读取
 * @author chenjiajun
 * @title PropertiesLoadListener
 * @project dev
 * @date 2019-04-17
 */
@Component
public class PropertiesLoadListener implements CommandLineRunner {


    @Autowired
    private ApplicationBuilder builder;
    
    /**
     * @author chenjiajun
     * @date 2019-04-17
     */
    @Async
    public void run(String... args) throws Exception {
        new CommonHandlerProxy(new PropertiesLoadHandler(
                builder.getContext())).excute();

    }
}
```
### 4. 性能介绍
#### 4.1 测试前提
 - 未对springboot内置tomcat进行进行优化
 - a.分别编写通过适配层调用，b.通过直接注入测试实现接口进行调用
 - 测试项目部署在同一台centos7 机器下
 - apache ab压测工具
#### 测试结果（5次取均值，1000并发）
 - time taken for test：a(0.587)，b(0.728)
 - Request per second: a(1703.63)，b(1373)




