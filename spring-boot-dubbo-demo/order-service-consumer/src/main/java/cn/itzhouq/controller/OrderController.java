package cn.itzhouq.controller;

import cn.itzhouq.bean.UserAddress;
import cn.itzhouq.service.OrderService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author itzhouq
 * @date 2021/2/6 下午4:29
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping("info")
    public List<UserAddress> getUserAddressInfo(@RequestParam("userId") String userId) {
        return orderService.initOrder(userId);
    }
}
