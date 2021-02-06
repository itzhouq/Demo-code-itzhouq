package cn.itzhouq.service;

import cn.itzhouq.bean.UserAddress;

import java.util.List;

/**
 * @author itzhouq
 * @date 2021/2/6 下午4:01
 */
public interface OrderService {

    /**
     * @param userId 用户ID
     * @Description 初始化订单接口
     */
    public List<UserAddress> initOrder(String userId);
}
