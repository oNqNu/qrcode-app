package com.qrcode.qrcodeapp.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;
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

    @RequestMapping(value = "json_test")
    private Map<String, String> json_test() {
        return Map.of("Key1","Value1","Key2","Value2");
    }

    @RequestMapping(value = "test", method = RequestMethod.GET)
    public String test() {

        return "helloWorld";
    }


    // @RequestMapping(value = "post_test2",method = RequestMethod.POST)
    // private String post_test2(@RequestBody OriginInputItem item) {


    //     // String str_path = "src/main/resources/img/abe.jpg";
    //     String img_string = item.getImg_string();
    //     int index = img_string.indexOf(",");
    //     String result = img_string.substring(index + 1);
    //     // System.out.println(result.substring(0, 3));

    //     try{
    //         // byte[] bytes = Base64.getDecoder().decode(result.getBytes());
    //         ByteArrayInputStream input = new ByteArrayInputStream(Base64.getDecoder().decode(result.getBytes()));
    //         BufferedImage image = ImageIO.read(input);
    //         FileOutputStream output =
    //         new FileOutputStream("src/main/resources/img/input/tttt.jpg");
    //         ImageIO.write(image, "jpg", output);
    //         }catch(IOException e){
    //             System.out.println(e.toString());
    //             System.out.println("なんかおかしいこと起きてんで");
    //         }
        
    //     String str_path = "src/main/resources/img/input/tttt.jpg";
    //     String str_data = "https://www.okayama-u.ac.jp";
    //     String str1 = item.getVersion();
    //     String str2 = item.getEcc_level();
    //     String str3 = item.getEncoding();
    //     String str4 = item.getMask_pattern();
    //     String str5 = item.getTraial_times();
    //     String str6 = item.getThreshold();
    //     String str7 = item.getScale();
    //     String str8 = item.getVariance();
    //     String str9 = item.getY_axis();
    //     String str10 = item.getX_axis();
        
    //     String base64data = QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
        
    //     return base64data;
    // }

    @RequestMapping(value = "create_qrcode",method = RequestMethod.POST)
    private Map<String,String> create_qrcode(@RequestBody OriginInputItem item) {

	    System.out.println("最初");
        long total = Runtime.getRuntime().totalMemory();
	    long free = Runtime.getRuntime().freeMemory();
	    long max = Runtime.getRuntime().maxMemory();
	    System.out.println("total: " + total / 1024 + "kb");
	    System.out.println("free: " + free / 1024 + "kb");
	    System.out.println("max: " + max / 1024 + "kb");

        String img_string = item.getImg_string();
        int index = img_string.indexOf(",");
        String result = img_string.substring(index + 1);
        img_string = null;

        try{
            // byte[] bytes = Base64.getDecoder().decode(result.getBytes());
            ByteArrayInputStream input = new ByteArrayInputStream(Base64.getDecoder().decode(result.getBytes()));
            BufferedImage image = ImageIO.read(input);
            input = null;
            FileOutputStream output =
            new FileOutputStream("src/main/resources/img/input/tttt.jpg");
            ImageIO.write(image, "jpg", output);
            output = null;
            }catch(IOException e){
                System.out.println(e.toString());
                System.out.println("なんかおかしいこと起きてんで");
            }

        result = null;
        
        // String str_path = "src/main/resources/img/kobe.jpg";
        String str_path = "src/main/resources/img/input/tttt.jpg";
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

        item = null;
        System.gc();

        long atotal = Runtime.getRuntime().totalMemory();
	    long afree = Runtime.getRuntime().freeMemory();
	    long amax = Runtime.getRuntime().maxMemory();
	    System.out.println("total: " + atotal / 1024 + "kb");
	    System.out.println("free: " + afree / 1024 + "kb");
	    System.out.println("max: " + amax / 1024 + "kb");
        
        return QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
    }

    @RequestMapping(value = "create_qrcode_bymap",method = RequestMethod.POST,consumes = "application/x-www-form-urlencoded")
    private Map<String,String> create_qrcode_bymap(@RequestParam Map<String,String> item) {

	    System.out.println("最初");
        long total = Runtime.getRuntime().totalMemory();
	    long free = Runtime.getRuntime().freeMemory();
	    long max = Runtime.getRuntime().maxMemory();
	    System.out.println("total: " + total / 1024 + "kb");
	    System.out.println("free: " + free / 1024 + "kb");
	    System.out.println("max: " + max / 1024 + "kb");

        String img_string = item.get("img_string");
	    // System.out.println(item);

        int index = img_string.indexOf(",");
        String result = img_string.substring(index + 1);
        img_string = null;

        try{
            // byte[] bytes = Base64.getDecoder().decode(result.getBytes());
            ByteArrayInputStream input = new ByteArrayInputStream(Base64.getMimeDecoder().decode(result.getBytes()));
            // ByteArrayInputStream input = new ByteArrayInputStream(Base64.getDecoder().decode(result.getBytes()));
            BufferedImage image = ImageIO.read(input);
            input = null;
            FileOutputStream output =
            new FileOutputStream("src/main/resources/img/input/tttt.jpg");
            ImageIO.write(image, "jpg", output);
            output = null;
            }catch(IOException e){
                System.out.println(e.toString());
                System.out.println("なんかおかしいこと起きてんで");
            }

        result = null;
        
        // String str_path = "src/main/resources/img/kobe.jpg";
        String str_path = "src/main/resources/img/input/tttt.jpg";
        String str_data = item.get("data");
        String str1 = item.get("version");
        String str2 = item.get("ecc_level");
        String str3 = item.get("encoding");
        String str4 = item.get("mask_pattern");
        String str5 = item.get("trial_times");
        String str6 = item.get("threshold");
        String str7 = item.get("scale");
        String str8 = item.get("variance");
        String str9 = item.get("y_axis");
        String str10 = item.get("x_axis");

        item = null;
        System.gc();

        long atotal = Runtime.getRuntime().totalMemory();
	    long afree = Runtime.getRuntime().freeMemory();
	    long amax = Runtime.getRuntime().maxMemory();
	    System.out.println("total: " + atotal / 1024 + "kb");
	    System.out.println("free: " + afree / 1024 + "kb");
	    System.out.println("max: " + amax / 1024 + "kb");
        
        return QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
    }

    @RequestMapping(value = "post_test",method = RequestMethod.POST)
    private Map<String,String> post_test(@RequestBody OriginInputItem item) {

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


        return QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
    }

    @RequestMapping(value = "get_test")
    private Map<String,String> get_test() {

        long total = Runtime.getRuntime().totalMemory();
	    long free = Runtime.getRuntime().freeMemory();
	    long max = Runtime.getRuntime().maxMemory();
	    System.out.println("total: " + total / 1024 + "kb");
	    System.out.println("free: " + free / 1024 + "kb");
	    System.out.println("max: " + max / 1024 + "kb");
        
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

        return QRmain.execute(new String[] {str_path,str_data,str1,str2,str3,str4,str5,str6,str7,str8,str9,str10});
    }

    @RequestMapping("greeting")
    private String add() {
        return "hello";
    }

}