package cn.itzhouq.security.security;

import cn.itzhouq.utils.utils.R;
import cn.itzhouq.utils.utils.ResponseUtil;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 未授权统一处理类
 *
 * @author zhouquan
 * @date 2021/2/14 15:10
 */
public class UnAuthEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e)
            throws IOException, ServletException {
        ResponseUtil.out(httpServletResponse, R.error());
    }
}
