package cn.itzhouq.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhouquan
 * @date 2021/2/9 上午8:57
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/hello")
    public String hello () {
        return "Spring Security Hello!";
    }

    @GetMapping("/index")
    public String index () {
        return "index!";
    }

    @GetMapping("/update")
    @Secured({"ROLE_sale", "ROLE_manager"})
    public String update () {
        return "update!";
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('admins')")
//    @PostAuthorize()
    public String add () {
        return "add!";
    }
}
