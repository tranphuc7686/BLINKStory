package com.example.blinkstory.network.model;

import com.squareup.moshi.Json;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Paging {
    @Json(name = "id")
    private int id;
    @Json(name = "index")
    private int index;
}
