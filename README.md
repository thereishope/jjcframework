# jjcframework
## 前言

 1. 由于部门成立后，团队规模以及项目数量在持续增长，不同的开发团队在框架选型或搭建上重复投入人力，并且部门成员跨团队流动增加了学习不同框架的成本
 2. 由于项目启动阶段时间仓促，搭建的框架过于简陋，项目交付后不利于部门品牌影响力的提升
 
 ## 特点
jjcframework框架包含(**dev**,**transfer**,**plugin**,**tool**)，它有如下**特点**

-  统一传参样式以及返回参数(可自由定制)，统一异常处理等，开发人员可更专注业务开发，且统一风格后减小维护成本
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
 - plugin:插件层
 - tool:工具层
 ## 如何使用
- 确保已安装maven3.+
- 编译工具层、插件层、适配层、脚手架（maven clean install）
- 启动脚手架(idea：鼠标右键脚手架**DevApplication**，点击Run DevApplication.main(),或者在控制台使用java -jar命令等)

## 模块详情
### 脚手架及适配层
#### controller中获取参数容器及设置业务层元信息细节
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
   #### 实例化参数容器时对请求进行前置处理，例如设置操作时间获取用户IP等
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
#### 通过参数容器中的方法元信息进行适配调用，并对异常进行统一处理。另外结合项目需要可对返回值进行定制
  ```java
  public class DevServiceInvoke{

    private static DevServiceInvoke devServiceInvoke = new DevServiceInvoke();

    /**
     * 统一调用业务层方法，并实现异常统一处理
     * 实现适配调用
     * @author chenjiajun
     * @date 2019-01-13
     */
    public <T> T doInvoke(DevParamContainer param, Class<T> t) throws Exception {
        try {
            BuisServiceInvoke service = ApplicationWrapBuilder.getBean(param
                        .getServiceMethod()
                        .getServiceEnums()
                        .getServiceName(),
                BuisServiceInvoke.class);
            return (T) service.excute(param, t);
        } catch (Exception e) {
            return getExResponse(e, t);
        }
    }



    /**业务异常组装并返回至控制层
     * 业务层异常抛出后，识别业务异常及其他异常
      *@author chenjiajun
      *@date 2019-01-21
      *
      */
    public <T> T getExResponse(Exception e, Class<T> t) {
        return (T) getResponse(e);
    }
``` 
####  调用目标业务层,所有业务层均需继承AbstractService并实现BuisServiceInvoke,这样请求就可以适配到具体业务类进行调用 

public interface BuisServiceInvoke {

    public <T> T excute(DevParamContainer container, Class<T> t) throws Exception;

}

public abstract class AbstractService implements BuisServiceInvoke {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    //@Transactional(rollbackFor = Exception.class)
    public <T> T excute(DevParamContainer paraMap, Class<T> t) throws Exception {
        T res = null;
        Method method = getMethodCache(paraMap);
        if (null != method) {
            res = (T) method.invoke(this, paraMap);
            /**
             * 1.根据res对象返回码，进行操作日志异步记录
             * 2.新老系统数据同步
             * 3.
             */
        }
        return res;
    }
```


