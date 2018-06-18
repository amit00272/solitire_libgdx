package com.yourcompany.solitaire.android;

import android.app.Activity;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

public class Admanager implements RewardedVideoAdListener{

    private AdView smallBannerView=null;
    private InterstitialAd interstitialView=null;

    private RelativeLayout relativeLayout;
    private RelativeLayout.LayoutParams layoutParams1;
    private RelativeLayout.LayoutParams layoutParams;

    private RewardedVideoAd mRewardedVideoAd;
    private Activity activity;

    public Admanager(final Activity context){

        activity = context;



        smallBannerView= new AdView(context);
        smallBannerView.setAdSize(AdSize.SMART_BANNER);
        smallBannerView.setAdUnitId("ca-app-pub-5091213395899781/8558624173");

        interstitialView=new InterstitialAd(context);
        interstitialView.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        interstitialView.setAdListener(new AdListener() {
            public void onAdClosed() {loadInterstitial();}
            public void onAdLoaded() {super.onAdLoaded(); }

        });


        relativeLayout=new RelativeLayout(context);
        layoutParams1=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layoutParams=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);


        relativeLayout.addView(smallBannerView, layoutParams);

        context.addContentView(relativeLayout, layoutParams1);
        smallBannerView.loadAd(new AdRequest.Builder().build());
        smallBannerView.setVisibility(View.GONE);

        loadInterstitial();
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(activity);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadRewardedVideoAd();
    }

    private void loadRewardedVideoAd() {
        mRewardedVideoAd.loadAd("ca-app-pub-5091213395899781/7052711638",
                new AdRequest.Builder().build());
    }


    private void loadInterstitial(){

        interstitialView.loadAd(new AdRequest.Builder().build());

    }

    public void showInterstitial(){

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (interstitialView.isLoaded()) {
                    interstitialView.show();
                } else {
                    //Log.d(TAG, "Interstitial ad is not loaded yet");
                }
            }
        });
    }

    public void playRewardVideo(){

        activity.runOnUiThread(new Runnable() {
            public void run() {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                } else {
                    loadRewardedVideoAd();
                }
            }
        });
     }

    public void onRewardedVideoAdLoaded() { }
    public void onRewardedVideoAdOpened() {}
    public void onRewardedVideoStarted() {}
    public void onRewardedVideoAdClosed() { }
    public void onRewarded(RewardItem rewardItem) { }
    public void onRewardedVideoAdLeftApplication() { }
    public void onRewardedVideoAdFailedToLoad(int i) {}
    public void onRewardedVideoCompleted() { }
}
