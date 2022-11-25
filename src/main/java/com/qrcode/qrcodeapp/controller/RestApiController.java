package com.qrcode.qrcodeapp.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.awt.image.BufferedImage;


import javax.imageio.ImageIO;

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
    private String post_test(@RequestBody OriginInputItem item) {
        System.out.println(item.getMask_pattern());

        // String str_path = "src/main/resources/img/abe.jpg";
        String str_path = item.getFile_path();
        // String str_data = "https://www.okayama-u.ac.jp";
        String str_data = item.getData();
        String str1 = item.getVersion();
        String str2 = item.getEcc_level();
        String str3 = item.getEncoding();
        String str4 = item.getMask_pattern();
        String str5 = item.getTraial_times();
        String str6 = item.getThreshold();
        String str7 = item.getScale();
        String str8 = item.getVariance();
        String str9 = item.getY_axis();
        String str10 = item.getX_axis();

        String base64data = QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
        
        return base64data;
        // return "aa";
    }

    @RequestMapping(value = "post_test2",method = RequestMethod.POST)
    private String post_test2(@RequestBody OriginInputItem item) {


        // String str_path = "src/main/resources/img/abe.jpg";
        String img_string = item.getImg_string();
        int index = img_string.indexOf(",");
        String result = img_string.substring(index + 1);
        System.out.println(result.substring(0, 3));

        try{
            byte[] bytes = Base64.getDecoder().decode(result.getBytes());
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(input);
            FileOutputStream output =
            new FileOutputStream("src/main/resources/img/input/tttt.jpg");
            ImageIO.write(image, "jpg", output);
            }catch(IOException e){
                System.out.println(e.toString());
                System.out.println("なんかおかしいこと起きてんで");
            }
        
        // String str_path = "src/main/resources/img/kobe.jpg";
        String str_path = "src/main/resources/img/input/tttt.jpg";
        String str_data = "https://www.okayama-u.ac.jp";
        String str1 = item.getVersion();
        String str2 = item.getEcc_level();
        String str3 = item.getEncoding();
        String str4 = item.getMask_pattern();
        String str5 = item.getTraial_times();
        String str6 = item.getThreshold();
        String str7 = item.getScale();
        String str8 = item.getVariance();
        String str9 = item.getY_axis();
        String str10 = item.getX_axis();

        String base64data = QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});

        Path p = Paths.get("src/main/resources/img/input/tttt.jpg");

        // try{
        // Files.deleteIfExists(p);
        // }catch(IOException e){
        // System.out.println(e);
        // }
        
        return base64data;
        // return "aa";
    }

    @RequestMapping(value = "post_test3",method = RequestMethod.POST)
    private String post_test3(@RequestBody OriginInputItem item) {


        // String str_path = "src/main/resources/img/abe.jpg";
        String img_string = item.getImg_string();
        int index = img_string.indexOf(",");
        String result = img_string.substring(index + 1);
        System.out.println(result.substring(0, 3));

        try{
            byte[] bytes = Base64.getDecoder().decode(result.getBytes());
            ByteArrayInputStream input = new ByteArrayInputStream(bytes);
            BufferedImage image = ImageIO.read(input);
            FileOutputStream output =
            new FileOutputStream("src/main/resources/img/input.jpg");
            ImageIO.write(image, "jpg", output);
            }catch(IOException e){
                System.out.println(e.toString());
                System.out.println("なんかおかしいこと起きてんで");
            }
        
        // String str_path = "src/main/resources/img/kobe.jpg";
        String str_path = "src/main/resources/img/kobe.jpg";
        String str_data = "https://www.okayama-u.ac.jp";
        String str1 = item.getVersion();
        String str2 = item.getEcc_level();
        String str3 = item.getEncoding();
        String str4 = item.getMask_pattern();
        String str5 = item.getTraial_times();
        String str6 = item.getThreshold();
        String str7 = item.getScale();
        String str8 = item.getVariance();
        String str9 = item.getY_axis();
        String str10 = item.getX_axis();

        String base64data = QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
        
        return base64data;
        // return "aa";
    }

    @RequestMapping("qr-test")
    private String qr() {
    // private String qr() {
    	
        String str_path = "src/main/resources/img/inputtt.jpg";
        String str_data = "https://www.okayama-u.ac.jp";
        String str1 = "5";
        String str2 = "0";
        String str3 = "0";
        String str4 = "0";
        String str5 = "100";
        String str6 = "96";
        String str7 = "50";
        String str8 = "0.7";
        String str9 = "0.5";
        String str10 = "0.5";
        String base64data = QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
        // return "run qr-test";
        return base64data;
    }

    @RequestMapping("greeting")
    private String add() {
        return "hello";
    }

}