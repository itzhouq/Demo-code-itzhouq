package cn.itzhouq.service.impl;

import cn.itzhouq.bean.UserAddress;
import cn.itzhouq.service.UserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * @author itzhouq
 * @date 2021/2/6 下午4:15
 */
@DubboService
@Component
public class UserServiceImpl implements UserService {
    public List<UserAddress> getUserAddressList(String userId) {
        UserAddress address = new UserAddress(1, "上海市徐汇区冠生园路", "1", "张三", "18888888888", "N");
        UserAddress address1 = new UserAddress(2, "上海市闵行区龙之梦", "2", "张三", "18888888888", "Y");

        return Arrays.asList(address, address1);
    }
}
