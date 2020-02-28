package com.example.blinkstory.advertisement;


public interface OnAdsListener {
    void setEarnAds();

    void setDoNotEarnAds();

    void setClosedAds();

    void onFailedLoadAds();
}