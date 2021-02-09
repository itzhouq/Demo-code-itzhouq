package cn.itzhouq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author zhouquan
 * @date 2021/2/9 上午8:47
 */
@SpringBootApplication
@MapperScan("cn.itzhouq.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
