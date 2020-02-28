package com.example.blinkstory.categories;


import com.example.blinkstory.model.entity.Category;
import com.example.blinkstory.network.Webservice;

import java.util.List;

import io.reactivex.Observable;

public class CategoriesInteractorImpl implements CategoriesInteractor {

    private Webservice webservice;


    public CategoriesInteractorImpl(Webservice webservice) {
        this.webservice = webservice;
    }


    @Override
    public Observable<List<Category>> getCategoryApi() {

       return webservice.categories();

    }




}
