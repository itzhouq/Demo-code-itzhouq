package cn.itzhouq.service;

import cn.itzhouq.bean.UserAddress;

import java.util.List;

/**
 * @author itzhouq
 * @date 2021/2/6 下午4:18
 */
public interface UserService {

    /**
     * 根据用户ID查询地址信息
     * @param userId 用户ID
     */
    public List<UserAddress> getUserAddressList(String userId);
}
