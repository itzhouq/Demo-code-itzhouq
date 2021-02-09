package cn.itzhouq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author zhouquan
 * @date 2021/2/9 上午11:47
 */
@Configuration
public class SecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin() // 自定义自己编写的页面
            .loginPage("/login.html") // 登录页面设置
            .loginProcessingUrl("/user/login") // 登录访问路径
            .defaultSuccessUrl("/test/index").permitAll() // 登录成功跳转的页面
            .and().authorizeRequests()
                // 设置哪些路径可以直接访问不需要认证
                .antMatchers("/", "/test/hello", "/user/login").permitAll()
            .anyRequest().authenticated()
            .and().csrf().disable(); // 关闭csrf防护
    }
}
