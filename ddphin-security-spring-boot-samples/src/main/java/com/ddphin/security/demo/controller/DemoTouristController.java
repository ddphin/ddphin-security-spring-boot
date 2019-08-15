package com.ddphin.security.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 *
 * @Date 2019/8/15 下午2:40
 * @Author ddphin
 */
@RestController
@RequestMapping("/tourist")
public class DemoTouristController {
    @GetMapping("/hello")
    public Object hello() {
        return  "welcome tourist";
    }
}
