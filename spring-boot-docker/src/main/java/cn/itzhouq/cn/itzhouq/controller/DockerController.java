package cn.itzhouq.cn.itzhouq.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author itzhouq
 * @date 2021/2/5 下午4:11
 */
@RestController
public class DockerController {

    @GetMapping("/docker")
    public String docker() {
        return "Spring Boot Docker!";
    }
}
