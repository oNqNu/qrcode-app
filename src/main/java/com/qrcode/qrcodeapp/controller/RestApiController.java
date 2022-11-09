package com.qrcode.qrcodeapp.controller;

import java.io.File;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.qrcode.qrcodeapp.inputItems.OriginInputItem;
import com.qrcode.qrcodeapp.util.QRmain;
// import com.qrcode.qrcodeapp.classes.Test;

@RestController
@RequestMapping("api")
@CrossOrigin
public class RestApiController {

    @RequestMapping(value = "hello")
    private String hello(@RequestParam("test") String str) {
        return str;
    }

    @RequestMapping(value = "post_test",method = RequestMethod.POST)
    private OriginInputItem post_test(@RequestBody OriginInputItem item) {
        System.out.println(item.getMask_pattern());

        String str_path = "src/main/resources/img/abe.jpg";
        String str_data = "https://www.okayama-u.ac.jp";
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
        
        return item;
        // return "aa";
    }

    @RequestMapping("qr-test")
    private String qr() {
    // private String qr() {
    	
        String str_path = "src/main/resources/img/abe.jpg";
        String str_data = "https://www.okayama-u.ac.jp";
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
        String base64data = QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
        // return "run qr-test";
        return base64data;
    }

    @RequestMapping("greeting")
    private String add() {
        return "hello";
    }

}