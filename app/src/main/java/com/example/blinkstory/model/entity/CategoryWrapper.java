package com.example.blinkstory.model.entity;

import com.squareup.moshi.Json;

import java.util.List;

public class CategoryWrapper {
    @Json(name = "")
    private List<Category> categoryList;

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }
}
