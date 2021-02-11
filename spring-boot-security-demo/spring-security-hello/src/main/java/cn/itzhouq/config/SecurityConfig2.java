package cn.itzhouq.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

/**
 * @author zhouquan
 * @date 2021/2/9 上午11:47
 */
@Configuration
public class SecurityConfig2 extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private DataSource dataSource;

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl jdbcTokenRepository = new JdbcTokenRepositoryImpl();
        jdbcTokenRepository.setDataSource(dataSource);
        // 自动生成表
        jdbcTokenRepository.setCreateTableOnStartup(true);
        return jdbcTokenRepository;
    }

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
        // 用户注销映射地址
        http.logout().logoutUrl("/logout").logoutSuccessUrl("/test/hello").permitAll();
        // 配置没有权限访问跳转自定义页面
        http.exceptionHandling().accessDeniedPage("/unauth403.html");
        http.formLogin() // 自定义自己编写的页面
            .loginPage("/login.html") // 登录页面设置
            .loginProcessingUrl("/user/login") // 登录访问路径
            .defaultSuccessUrl("/success.html").permitAll() // 登录成功跳转的页面
            .and().authorizeRequests()
                // 设置哪些路径可以直接访问不需要认证
            // .antMatchers("/", "/test/hello", "/user/login").permitAll()
            // 当前登录用户，只有具有admins权限才能访问这个路径
            // .antMatchers("/test/index").hasAuthority("admins")

            // 当前登录用户，只有具有其中一种权限才能访问这个路径
            // .antMatchers("/test/index").hasAnyAuthority("admins", "manager")

            // 当前登录用户，只有具有sale这个角色才能访问这个路径
            // .antMatchers("/test/index").hasRole("sale")

            // 当前登录用户，只有具有其中一个角色才能访问这个路径
            .antMatchers("/test/index").hasAnyRole("sale", "man")
            .anyRequest().authenticated()

            // 配置自动登录
            .and().rememberMe().tokenRepository(persistentTokenRepository())
            .tokenValiditySeconds(60) // 设置自动登录的有效时间，单位秒
            .userDetailsService(userDetailsService)

            .and().csrf().disable(); // 关闭csrf防护
    }
}
