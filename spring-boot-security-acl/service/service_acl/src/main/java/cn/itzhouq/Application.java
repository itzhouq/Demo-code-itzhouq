package cn.itzhouq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author zhouquan
 * @date 2021/2/17 18:28
 */
@SpringBootApplication
@EnableDiscoveryClient
//@ComponentScan("cn.itzhouq")
@MapperScan("cn.itzhouq.service.acl.mapper")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
