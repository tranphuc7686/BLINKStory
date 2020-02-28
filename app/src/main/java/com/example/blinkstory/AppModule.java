package com.example.blinkstory;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private Context context;

    AppModule(Application application)
    {
        context = application;
    }

    @Provides
    @Singleton
    public Context provideContext()
    {
        return context;
    }

}
