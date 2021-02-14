package cn.itzhouq.security1.security;

import cn.itzhouq.utils.utils.R;
import cn.itzhouq.utils.utils.ResponseUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 退出处理器
 * @author zhouquan
 * @date 2021/2/14 15:01
 */
public class TokenLogoutHandler implements LogoutHandler {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenLogoutHandler(TokenManager tokenManager, RedisTemplate redisTemplate) {
        this.tokenManager = tokenManager;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        // 1. 从header中获取token
        // 2. token不为空，删除token，从Redis中移除token
        String token = request.getHeader("token");
        if (!StringUtils.isEmpty(token)) {
            // 移除
            tokenManager.removeToken(token);

            // 从Redis中删除
            String userInfoFromToken = tokenManager.getUserInfoFromToken(token);
            redisTemplate.delete(userInfoFromToken);
        }
        ResponseUtil.out(response, R.ok());
    }
}
