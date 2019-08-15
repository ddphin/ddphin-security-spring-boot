package com.ddphin.security.demo.controller;

import com.ddphin.security.entity.AIdentity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * DemoController
 *
 * @Date 2019/8/15 下午2:40
 * @Author ddphin
 */
@RestController
@RequestMapping("/auth")
public class DemoAuthController {

    @GetMapping("/hello")
    public Object gethello() {
        AIdentity identity = (AIdentity) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return  "authorized:" + identity.getUserId();
    }

    @PutMapping("/hello")
    public Object puthello() {
        AIdentity identity = (AIdentity) SecurityContextHolder.getContext().getAuthentication().getDetails();
        return  "authorized:" + identity.getUserId();
    }
}
