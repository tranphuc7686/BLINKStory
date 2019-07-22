package com.example.blinkstory.model.entity;

public class Element {
    private String Id ;
    private String Caption ;
    private String Url ;
    private int TypeData ;
    private String SrcThumbail ;

    public Element() {
    }

    public Element(String id, String caption, String url, int typeData, String srcThumbail) {
        Id = id;
        Caption = caption;
        Url = url;
        TypeData = typeData;
        SrcThumbail = srcThumbail;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public int getTypeData() {
        return TypeData;
    }

    public void setTypeData(int typeData) {
        TypeData = typeData;
    }

    public String getSrcThumbail() {
        return SrcThumbail;
    }

    public void setSrcThumbail(String srcThumbail) {
        SrcThumbail = srcThumbail;
    }
}
