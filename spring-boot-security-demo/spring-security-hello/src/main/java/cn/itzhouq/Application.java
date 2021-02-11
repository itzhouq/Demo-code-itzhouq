package cn.itzhouq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * @author zhouquan
 * @date 2021/2/9 上午8:47
 */
@SpringBootApplication
@MapperScan("cn.itzhouq.mapper")
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
