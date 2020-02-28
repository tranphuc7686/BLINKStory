package com.example.blinkstory.categories;

import dagger.Subcomponent;

@CategoriesScope
@Subcomponent(modules = {CategoriesModule.class})
public interface CategoriesComponent {
    void inject(CategoriesActivity categoriesActivity);
}
