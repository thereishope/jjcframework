package com.development.transfer.proxy;

/**listener公共处理接口
 * @author chenjiajun
 * @title CommonHandler
 * @project dev
 * @date 2019-01-13
 */
public interface CommonHandler {


    /**准入许可
      *@author chenjiajun
      *@date 2019-01-13
      *
      */
    public boolean allowance() throws Exception;


    /**业务处理
      *@author chenjiajun
      *@date 2019-01-13
      *
      */
    public void  handle() throws Exception;
}
