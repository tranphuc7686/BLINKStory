package com.example.blinkstory.elements;

import com.example.blinkstory.network.Webservice;

import dagger.Module;
import dagger.Provides;

@Module
public class ElementModule {


    @Provides
    @ElementScope
    ElementsPresenter providesElementsPresenter(ElementsInteractor elementsInteractor){
        return new ElementsPresenterImpl(elementsInteractor);
    }
    @Provides
    @ElementScope
    ElementsInteractor providesElementsInteractor(Webservice webservice){
        return new ElementsInteractorImpl(webservice);
    }
}
