package com.example.blinkstory.advertisement;

import androidx.appcompat.app.AppCompatActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class AdsModule {
    private AppCompatActivity mActivity;


    public AdsModule(AppCompatActivity activity) {
        this.mActivity = activity;
    }

    @Provides
    @RewardAdsInfo
    @AdsScope
    BaseAdsRepository providesRewardAds(){
        return new RewardAdsImpl(mActivity);
    }

    @Provides
    @BannerAdsInfo
    @AdsScope
    BaseAdsRepository providesBannerAds(){
        return new BannerAdsImpl();
    }

    @Provides
    @InterstitialAdsInfo
    @AdsScope
    BaseAdsRepository providesInterstitialAds(){
        return new InterstitialAdsImpl();
    }
}
