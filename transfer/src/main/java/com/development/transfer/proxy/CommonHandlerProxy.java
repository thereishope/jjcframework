package com.development.transfer.proxy;

/**公用的自定义监听器静态代理
 * @author jiajunchen
 * @title CommonHandlerProxy
 * @project dev
 * @date 2019-04-17
 */
public class CommonHandlerProxy implements CommonHandler{

    private CommonHandler commonHandler;


    public CommonHandlerProxy(CommonHandler commonHandler) {
        this.commonHandler = commonHandler;
    }


    /**
      *@author jiajunchen
      *@date 2019-04-17
      *
      */
    public boolean allowance() throws Exception {
       return  commonHandler.allowance();
    }
    
    /**
      *@author jiajunchen
      *@date 2019-04-17
      *
      */

    public void handle() throws Exception {
        commonHandler.handle();
    }
    
    
    /**
      *@author jiajunchen
      *@date 2019-04-17
      *
      */
    public void excute()throws Exception{
        if(allowance()){
            handle();
        }
    }
}
