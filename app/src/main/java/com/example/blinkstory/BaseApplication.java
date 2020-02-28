package com.example.blinkstory;

import android.app.Application;
import android.os.StrictMode;

import com.example.blinkstory.categories.CategoriesComponent;
import com.example.blinkstory.categories.CategoriesModule;
import com.example.blinkstory.elements.ElementComponent;
import com.example.blinkstory.elements.ElementModule;
import com.example.blinkstory.network.NetworkModule;

public class BaseApplication extends Application {
    private AppComponent appComponent;
    private CategoriesComponent categoriesComponent;
    private ElementComponent elementComponentle;
    @Override
    public void onCreate()
    {
        super.onCreate();
        StrictMode.enableDefaults();
        appComponent = createAppComponent();
    }

    private AppComponent createAppComponent()
    {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .networkModule(new NetworkModule())
                .build();
    }
    public CategoriesComponent createCategoriesComponent(){
        categoriesComponent = appComponent.plus(new CategoriesModule());
        return categoriesComponent;
    }
    public ElementComponent createElementComponent(){
        elementComponentle = appComponent.plus(new ElementModule());
        return elementComponentle;
    }
}
