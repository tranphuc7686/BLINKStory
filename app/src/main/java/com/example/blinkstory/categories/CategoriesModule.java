package com.example.blinkstory.categories;

import com.example.blinkstory.network.Webservice;

import dagger.Module;
import dagger.Provides;

@Module
public class CategoriesModule {

    @Provides
    @CategoriesScope
    CategoriesPresenter providesCategoriesPresenter(CategoriesInteractor categoriesInteractor){
        return new CategoriesPresenterImpl(categoriesInteractor);
    }
    @Provides
    @CategoriesScope
    CategoriesInteractor providesCategoriesInteractor(Webservice webservice){
        return new CategoriesInteractorImpl(webservice);
    }

}
