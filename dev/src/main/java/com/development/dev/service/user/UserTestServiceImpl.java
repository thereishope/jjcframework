package com.development.dev.service.user;
import com.development.httpPlugin.restBuilder.HttpBuilder;
import com.development.transfer.exception.BuisException;
import com.development.transfer.invoke.AbstractService;
import com.development.transfer.respconse.DevResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author chenjiajun
 * @title UserTestServiceImpl
 * @project dev
 * @date 2019-01-04
 */
@Service
public class UserTestServiceImpl extends AbstractService implements UserTestService {

    @Autowired
    private HttpBuilder httpBuilder;

    private Logger logger = LoggerFactory.getLogger(UserTestServiceImpl.class);

    public DevResponse login(String name, String pwd) throws Exception {
        String o = "";
        try {
            o = httpBuilder.getForObject("https://baike.baidu.com/api/usercenter/login?msg=1&_=1556002061919",String.class);
            logger.info("UserService[login]执行登录,name[" + name + "],pwd[" + pwd + "]");
            if (StringUtils.isEmpty(name)) {
                throw new BuisException("0001", "请输入用户名");
            }
        } catch (Exception e) {
            throwEx(e);
        }
        return commonResponse(true,"0000","操作成功",o);
    }
}
