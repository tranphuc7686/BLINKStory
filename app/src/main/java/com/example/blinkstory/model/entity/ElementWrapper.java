package com.example.blinkstory.model.entity;

import com.squareup.moshi.Json;

import java.util.List;

public class ElementWrapper {
    @Json(name = "data")
    private List<Element> elementList;

    public List<Element> getElementList() {
        return elementList;
    }

    public void setElementList(List<Element> elementList) {
        this.elementList = elementList;
    }
}
