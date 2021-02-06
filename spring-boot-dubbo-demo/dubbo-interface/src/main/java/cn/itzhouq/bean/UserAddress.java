package cn.itzhouq.bean;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author zhouquan
 * @date 2021/2/6 下午4:03
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAddress implements Serializable {

    private Integer id;

    /**
     * 收件地址
     */
    private String userAddress;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 收件人
     */
    private String consignee;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 是否为默认地址 Y-是 N-否
     */
    private String isDefault;
}
