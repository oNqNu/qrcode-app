package com.qrcode.qrcodeapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.qrcodeapp.classes.Test;

@RestController
@RequestMapping("api")
public class RestApiController {

    @RequestMapping("hello")
    private String hello(@RequestParam("test") String str) {
        return str;
    }

    @RequestMapping("greeting")
    private String add() {
        Test test = new Test();
        test.string = "hello!!";
        return test.greeting();
    }

}