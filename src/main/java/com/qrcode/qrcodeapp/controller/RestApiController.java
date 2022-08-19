package com.qrcode.qrcodeapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api")
public class RestApiController {
 
    @RequestMapping("hello")
    private String hello(@RequestParam("test") String str) {
        return str;
    }

}