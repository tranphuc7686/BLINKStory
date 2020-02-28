package com.example.blinkstory.advertisement;

import com.example.blinkstory.details.DetailImageElementFragment;
import com.example.blinkstory.details.DetailVideoElementFragment;

import dagger.Subcomponent;

@Subcomponent(modules = {AdsModule.class})
@AdsScope
public interface AdsComponent {
    void inject(DetailImageElementFragment target);
    void inject(DetailVideoElementFragment target);
}
