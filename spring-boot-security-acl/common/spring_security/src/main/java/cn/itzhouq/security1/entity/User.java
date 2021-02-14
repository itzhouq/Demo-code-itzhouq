package cn.itzhouq.security1.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author zhouquan
 * @date 2021/2/14 15:32
 */
@Data
@ApiModel(description = "用户实体类")
public class User implements Serializable {

    public static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "微信openId")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "昵称")
    private String nickName;

    @ApiModelProperty(value = "用户头像")
    private String salt;

    @ApiModelProperty(value = "用户签名")
    private String token;

}
