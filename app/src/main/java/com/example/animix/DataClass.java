package com.example.animix;

public class DataClass {

    private String dataTopic;
    private String dataDesc;
    private String dataLang;
    private String dataImageURL;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDataTopic() {return dataTopic;}

    public String getDataDesc() {return dataDesc;}

    public String getDataLang() {
        return dataLang;
    }

    public String getDataImage() {return dataImageURL;}

    public DataClass(String dataTopic, String dataDesc, String dataLang, String dataImage) {
        this.dataTopic = dataTopic;
        this.dataDesc = dataDesc;
        this.dataLang = dataLang;
        this.dataImageURL = dataImage;
    }

    public DataClass(){

    }
}
