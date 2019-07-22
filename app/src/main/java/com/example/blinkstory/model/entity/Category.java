package com.example.blinkstory.model.entity;


public class Category {
    private int idCategory;
    private String name;
    private String urlCategory;

    public Category(int idCategory, String name, String urlCategory) {
        this.idCategory = idCategory;
        this.name = name;
        this.urlCategory = urlCategory;
    }



    public int getIdCategory() {
        return idCategory;
    }


    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlCategory() {
        return urlCategory;
    }

    public void setUrlCategory(String urlCategory) {
        this.urlCategory = urlCategory;
    }
}
