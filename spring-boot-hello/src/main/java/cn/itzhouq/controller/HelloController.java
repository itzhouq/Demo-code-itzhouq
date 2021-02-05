package cn.itzhouq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author itzhouq
 * @date 2021/2/5 下午2:51
 */
@RestController
public class HelloController {

    @GetMapping("hello")
    public String hello() {
        return "Spring Boot Hello!";
    }
}
