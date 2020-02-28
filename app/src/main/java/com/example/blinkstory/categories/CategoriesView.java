package com.example.blinkstory.categories;

import com.example.blinkstory.model.entity.Category;

import java.util.List;

public interface CategoriesView {
     void onDataResponse(List<Category> categories);
     void onDataSponseError(String status);
}
