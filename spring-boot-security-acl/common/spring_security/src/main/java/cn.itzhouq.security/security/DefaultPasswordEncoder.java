package cn.itzhouq.security.security;

import cn.itzhouq.utils.utils.MD5;
import org.springframework.security.crypto.password.PasswordEncoder;

/**密码处理
 * @author zhouquan
 * @date 2021/2/14 14:46
 */

public class DefaultPasswordEncoder implements PasswordEncoder {

    public DefaultPasswordEncoder() {
        this(-1);
    }

    public DefaultPasswordEncoder(int strength) {

    }

    // 进行MD5加密
    @Override
    public String encode(CharSequence charSequence) {
        return MD5.encrypt(charSequence.toString());
    }

    // 进行密码比对
    @Override
    public boolean matches(CharSequence charSequence, String encoderPassword) {
        return encoderPassword.equals(MD5.encrypt(charSequence.toString()));
    }
}
