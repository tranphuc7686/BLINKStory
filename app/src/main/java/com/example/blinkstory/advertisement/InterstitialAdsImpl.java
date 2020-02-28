package com.example.blinkstory.advertisement;

import android.content.Context;

import com.example.blinkstory.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import javax.inject.Inject;

public class InterstitialAdsImpl implements BaseAdsRepository {
    InterstitialAd mInterstitialAd;
    @Inject
    Context mContext;

    public InterstitialAdsImpl(){
    }

    @Override
    public void setAdvertisement(OnAdsListener adsListener) {
        addAds();
    }
    private void addAds() {
        MobileAds.initialize(mContext,
                Constant.APP_ADS);
        mInterstitialAd = new InterstitialAd(mContext);
        mInterstitialAd.setAdUnitId(Constant.XENKE_ADS);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


    }
}
