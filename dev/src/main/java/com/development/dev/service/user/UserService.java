package com.development.dev.service.user;

import com.development.dev.domain.User.User;
import com.development.dev.service.AbstractService;
import com.development.plugin.http.restBuilder.HttpBuilder;
import com.development.transfer.container.DevParamContainer;
import com.development.transfer.exception.BuisException;
import com.development.transfer.respconse.DevResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author chenjiajun
 * @title UserService
 * @project dev
 * @date 2019-01-02
 */
@Service("UserService")
public class UserService extends AbstractService {


    @Autowired
    private HttpBuilder httpBuilder;

    /**
     * @author chenjiajun
     * @date 2019-01-02
     */
    public DevResponse login(DevParamContainer container) throws Exception {
        String o = "";
        try {
            o = httpBuilder.getForObject("https://baike.baidu.com/api/usercenter/login?msg=1&_=1556002061919",String.class);
//            logger.info("UserService[login]执行登录,name[" + name + "],pwd[" + pwd + "]");
//            if (StringUtils.isEmpty(name)) {
//                throw new BuisException("0001", "请输入用户名");
//            }
        } catch (Exception e) {
            throwEx(e);
        }
        return commonResponse(true,"0000","操作成功",o);
    }


    /**
     * @author chenjiajun
     * @date 2019-01-04
     */
    public DevResponse inserUser(DevParamContainer container) throws Exception {
        User user = container.get("user", User.class);
        if (null == user) {
            throw new BuisException("0002", "请输入用户名和密码");
        }
        return commonResponse();
    }
}
