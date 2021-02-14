package cn.itzhouq.security1.filter;

import cn.itzhouq.security1.security.TokenManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 授权过滤器
 *
 * @author zhouquan
 * @date 2021/2/14 16:07
 */

public class TokenAuthFilter extends BasicAuthenticationFilter {

    private TokenManager tokenManager;
    private RedisTemplate redisTemplate;

    public TokenAuthFilter(AuthenticationManager authenticationManager, TokenManager tokenManager, RedisTemplate redisTemplate) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate;
        this.tokenManager = tokenManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // 获取当前认证成功用户信息
        UsernamePasswordAuthenticationToken authenticRequest = getAuthentication(request);

        // 判断如果有权限信息，放到权限上下文中
        if (authenticRequest != null) {
            SecurityContextHolder.getContext().setAuthentication(authenticRequest);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 从header中获取token
        String token = request.getHeader("token");
        if (token != null) {
            // 从token中获取用户名
            String username = tokenManager.getUserInfoFromToken(token);

            // 从Redis中获取对应权限列表
            List<String> permessionValueList = (List<String>) redisTemplate.opsForValue().get(username);
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            for (String permessionValue : permessionValueList) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority(permessionValue);
                authorities.add(authority);
            }
            return new UsernamePasswordAuthenticationToken(username, token, authorities);
        }
        return null;
    }
}
