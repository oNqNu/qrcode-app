package com.qrcode.qrcodeapp.inputItems;

public class OriginInputItem {


    //プロパティ
    private String file_path;
    private String data;
    private String version;
    private String ecc_level;
    private String encoding;
    private String mask_pattern;
    private String traial_times;
    private String threshold;
    private String scale;
    private String variance;
    private String y_axis;
    private String x_axis;


    public String getFile_path(){
        return file_path;
    }

    public void setFile_path(String file_path){
    this.file_path = file_path;
    }

    public String getData(){
        return data;
    }

    public void setData(String data){
    this.data = data;
    }

    public String getVersion(){
        return version;
    }

    public void setVersion(String version){
    this.version = version;
    }

    public String getEcc_level(){
        return ecc_level;
    }

    public void setEcc_level(String ecc_level){
        this.ecc_level = ecc_level;
    }

    public String getEncoding(){
        return encoding;
    }

    public void setEncoding(String encoding){
        this.encoding = encoding;
    }

    public String getMask_pattern(){
        return mask_pattern;
    }

    public void setMask_pattern(String mask_pattern){
        this.mask_pattern = mask_pattern;
    }

    public String getTraial_times(){
        return traial_times;
    }

    public void setTraial_times(String traial_times){
        this.traial_times = traial_times;
    }

    public String getThreshold(){
        return threshold;
    }

    public void setThreshold(String threshold){
        this.threshold = threshold;
    }

    public String getScale(){
        return scale;
    }

    public void setScale(String scale){
        this.scale = scale;
    }

    public String getVariance(){
        return variance;
    }

    public void setVariance(String variance){
        this.variance = variance;
    }

    public String getY_axis(){
        return y_axis;
    }

    public void setY_axis(String y_axis){
        this.y_axis = y_axis;
    }

    public String getX_axis(){
        return x_axis;
    }

    public void setX_axis(String x_axis){
        this.x_axis = x_axis;
    }    
}
