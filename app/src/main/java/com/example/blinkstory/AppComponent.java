package com.example.blinkstory;

import com.example.blinkstory.categories.CategoriesComponent;
import com.example.blinkstory.categories.CategoriesModule;
import com.example.blinkstory.elements.ElementComponent;
import com.example.blinkstory.elements.ElementModule;
import com.example.blinkstory.network.NetworkModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {
    CategoriesComponent plus(CategoriesModule categoriesModule);
    ElementComponent plus(ElementModule elementModule);
}
