package com.example.blinkstory.advertisement;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.example.blinkstory.Constant;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class RewardAdsImpl implements BaseAdsRepository {
    RewardedAd rewardedAd;
    Activity activity;
    public RewardAdsImpl(AppCompatActivity activity) {
        MobileAds.initialize(activity, Constant.APP_ADS);
        this.activity = activity;
        this.rewardedAd = createAndLoadRewardedAd();
    }

    private RewardedAd createAndLoadRewardedAd() {
        RewardedAd rewardedAd = new RewardedAd(activity.getBaseContext(),
                Constant.TRATHUONG_ADS);
        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

            @Override
            public void onRewardedAdFailedToLoad(int errorCode) {
                // Ad failed to load.
            }
        };
        rewardedAd.loadAd(new AdRequest.Builder().build(), adLoadCallback);
        return rewardedAd;
    }




    @Override
    public void setAdvertisement(OnAdsListener adsListener) {
        if (rewardedAd.isLoaded()) {
            RewardedAdCallback adCallback = new RewardedAdCallback() {
                boolean isEarn = false;

                @Override
                public void onRewardedAdOpened() {
                    // Ad opened.
                }

                @Override
                public void onRewardedAdClosed() {
                    if (!isEarn) {
                        adsListener.setDoNotEarnAds();
                    }
                    else {
                        adsListener.setClosedAds();
                    }
                    rewardedAd = createAndLoadRewardedAd();

                }

                @Override
                public void onUserEarnedReward(@androidx.annotation.NonNull RewardItem reward) {
                    isEarn = true;
                    adsListener.setEarnAds();
                }

                @Override
                public void onRewardedAdFailedToShow(int errorCode) {
                    // Ad failed to display
                }

            };
            rewardedAd.show(activity, adCallback);
        } else {
            adsListener.onFailedLoadAds();
        }
    }
}
