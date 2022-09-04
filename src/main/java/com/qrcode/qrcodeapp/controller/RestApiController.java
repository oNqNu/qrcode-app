package com.qrcode.qrcodeapp.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.qrcodeapp.util.QRmain;
// import com.qrcode.qrcodeapp.classes.Test;

@RestController
@RequestMapping("api")
public class RestApiController {

    @RequestMapping("hello")
    private String hello(@RequestParam("test") String str) {
        return str;
    }

    @RequestMapping("qr-test")
    private String qr() {
        String str_path = "/Users/s18121/Desktop/ws/qrcode-app/src/main/img/abe.jpg";
        String str_data = "https://www.okayama-u.ac.jp/";
        String str1 = "5";
        String str2 = "0";
        String str3 = "0";
        String str4 = "0";
        String str5 = "100";
        String str6 = "96";
        String str7 = "50";
        String str8 = "0.7";
        String str9 = "0.3";
        String str10 = "0.6";
        QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
        return "run qr-test";
    }

    @RequestMapping("greeting")
    private String add() {
        return "hello";
    }

}