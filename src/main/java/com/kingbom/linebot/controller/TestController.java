package com.kingbom.linebot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by bombay on 13/8/2018 AD.
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test(@RequestParam("message") String message){
        return message;
    }
}
