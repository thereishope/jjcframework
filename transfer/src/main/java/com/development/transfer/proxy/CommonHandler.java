package com.development.transfer.proxy;

/**listener公共处理接口
 * @author jiajunchen
 * @title CommonHandler
 * @project dev
 * @date 2019-04-17
 */
public interface CommonHandler {


    /**准入许可
      *@author jiajunchen
      *@date 2019-04-17
      *
      */
    public boolean allowance() throws Exception;


    /**业务处理
      *@author jiajunchen
      *@date 2019-04-17
      *
      */
    public void  handle() throws Exception;
}
