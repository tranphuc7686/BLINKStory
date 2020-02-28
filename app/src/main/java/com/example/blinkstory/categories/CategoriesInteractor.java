package com.example.blinkstory.categories;

import com.example.blinkstory.model.entity.Category;

import java.util.List;

import io.reactivex.Observable;

public interface CategoriesInteractor {

     Observable<List<Category>> getCategoryApi();


}
