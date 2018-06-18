package com.yourcompany.solitaire.android;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.yourcompany.solitaire.Helper.ActionResolver;
import com.yourcompany.solitaire.Helper.Variables;
import com.yourcompany.solitaire.Solitaire;

import static com.yourcompany.solitaire.Helper.Variables.CACHE_AD;
import static com.yourcompany.solitaire.Helper.Variables.CHANGE_ORIENTATION_TO_LANDSCAPE;
import static com.yourcompany.solitaire.Helper.Variables.CHANGE_ORIENTATION_TO_PORTRAIT;
import static com.yourcompany.solitaire.Helper.Variables.SHOW_AD;



public class AndroidLauncher extends AndroidApplication implements ActionResolver {


    private static Admanager admanager;
    public static int frequency_setter = 0;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();

		RelativeLayout layout = new RelativeLayout(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				             WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		View gameView = initializeForView(new Solitaire(this));

		admanager = new Admanager(this);

		// Add the libgdx view
		layout.addView(gameView);

		// Hook it all up
		setContentView(layout);

	}



	@Override
	protected void onStart() {
		super.onStart();

	}

	@Override
	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onPause() {
		super.onPause();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

	}

	@Override
	protected void onResume() {
		super.onResume();

	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
	}

	public void showAdmobBanner(){

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				//if(adView != null)
				//adView.setVisibility(View.VISIBLE);
			}
		});
	}
	public void HideAdmobBanner(){

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				//if(adView != null)
				//adView.setVisibility(View.GONE);
			}
		});

	}
	public void ShowAdmobVideo(){

		runOnUiThread(new Runnable() {
			@Override
			public void run() {


				admanager.playRewardVideo();
			}
		});

	}
	public boolean IsAdmobVideoAvailable(){

		return false;
	}

	@Override
	public void showInterstitial() {

		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				if(++frequency_setter  == 3) {

					admanager.showInterstitial();
					frequency_setter = 0;

				}
			}
		});
	}


	public Handler actionRequestHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
				case Variables.CHANGE_ORIENTATION_TO_PORTRAIT:
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					break;
				case CHANGE_ORIENTATION_TO_LANDSCAPE:
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					break;
				case CACHE_AD:
					//if(AD_NETWORK == CHARTBOOST) { cacheChartBoostIntersitial(); }
					//else { showOrLoadInterstital(CACHE_AD); }
					break;
				case Variables.SHOW_AD:
					//if(AD_NETWORK == CHARTBOOST) { showChartBoostInterstitial(); }
					//else { showOrLoadInterstital(SHOW_AD); }
					break;
			}
		}
	};


	@Override
	public void customActionRequestHandler(int whatAction) {
		if(whatAction == CHANGE_ORIENTATION_TO_LANDSCAPE) {
			actionRequestHandler.sendEmptyMessage(CHANGE_ORIENTATION_TO_LANDSCAPE);
		} else if(whatAction == CHANGE_ORIENTATION_TO_PORTRAIT) {
			actionRequestHandler.sendEmptyMessage(CHANGE_ORIENTATION_TO_PORTRAIT);
		} else if(whatAction == CACHE_AD) {
			actionRequestHandler.sendEmptyMessage(CACHE_AD);
		} else if(whatAction == SHOW_AD) {
			actionRequestHandler.sendEmptyMessage(SHOW_AD);
		}
	}


}
