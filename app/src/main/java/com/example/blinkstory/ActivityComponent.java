package com.example.blinkstory;

import com.example.blinkstory.advertisement.AdsComponent;
import com.example.blinkstory.advertisement.AdsModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {})
public interface ActivityComponent {
    AdsComponent plus(AdsModule adsModule);
}
