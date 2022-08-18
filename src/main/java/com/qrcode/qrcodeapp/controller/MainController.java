package com.qrcode.qrcodeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showHello(Model model) {
        model.addAttribute("title", "Hello World!");
        model.addAttribute("message", "お願いしまふ");
        return "hello";
    }


    @RequestMapping(value = "/hello", method = RequestMethod.POST)
    public String postFromhello(@RequestParam("text1")String str ,Model model) {
        model.addAttribute("sample", str);
        return "helloResponse";
    
    }
}