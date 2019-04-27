package com.development.transfer.proxy;

/**公用的自定义监听器静态代理
 * @author chenjiajun
 * @title CommonHandlerProxy
 * @project dev
 * @date 2019-01-13
 */
public class CommonHandlerProxy implements CommonHandler{

    private CommonHandler commonHandler;


    public CommonHandlerProxy(CommonHandler commonHandler) {
        this.commonHandler = commonHandler;
    }


    /**
      *@author chenjiajun
      *@date 2019-01-13
      *
      */
    public boolean allowance() throws Exception {
       return  commonHandler.allowance();
    }
    
    /**
      *@author chenjiajun
      *@date 2019-01-13
      *
      */

    public void handle() throws Exception {
        commonHandler.handle();
    }
    
    
    /**
      *@author chenjiajun
      *@date 2019-01-13
      *
      */
    public void excute()throws Exception{
        if(allowance()){
            handle();
        }
    }
}
