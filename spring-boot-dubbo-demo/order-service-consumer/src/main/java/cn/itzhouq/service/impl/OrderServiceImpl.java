package cn.itzhouq.service.impl;

import cn.itzhouq.bean.UserAddress;
import cn.itzhouq.service.OrderService;
import cn.itzhouq.service.UserService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author itzhouq
 * @date 2021/2/6 下午4:27
 */
@Service
public class OrderServiceImpl implements OrderService {

    @DubboReference
    UserService userService;

    public List<UserAddress> initOrder(String userId) {
        // 查询用户收货地址
        return userService.getUserAddressList(userId);
    }
}
