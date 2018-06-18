package com.yourcompany.solitaire;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.yourcompany.solitaire.Helper.ActionResolver;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.Screens.GameScreen;
import com.yourcompany.solitaire.Screens.InfoScreen;
import com.yourcompany.solitaire.Screens.SettingsScreen;

import static com.yourcompany.solitaire.Helper.Variables.CACHE_AD;
import static com.yourcompany.solitaire.Helper.Variables.CHANGE_ORIENTATION_TO_LANDSCAPE;
import static com.yourcompany.solitaire.Helper.Variables.CHANGE_ORIENTATION_TO_PORTRAIT;
import static com.yourcompany.solitaire.Helper.Variables.SHOW_AD;

public class Solitaire extends Game
{
	public CustomAssetManager assets;

	public static  ActionResolver actionResolver;

	GameScreen gameScreen;
	SettingsScreen settingsScreen;
	InfoScreen infoScreen;

	private boolean isGameScreenSet;

	private boolean isFirstStartUp;



	public Solitaire(ActionResolver actionResolver) {
		this.actionResolver = actionResolver;
		this.isFirstStartUp = true;

		//actionResolver.showAdmobBanner();
	}

	@Override
	public void create()
	{
		assets = new CustomAssetManager();
	}

	@Override
	public void render() {
		if(isGameScreenSet == false) {
			assets.update();

			if(assets.hasFinishedLoading() == true) {
				gameScreen = new GameScreen(this);
				infoScreen = new InfoScreen(this);
				settingsScreen = new SettingsScreen(this);

				setGameScreen();

				isGameScreenSet = true;
			}
		} else {
			if(getScreen() == gameScreen) { gameScreen.render(Gdx.graphics.getDeltaTime()); }
			else if(getScreen() == infoScreen) { infoScreen.render(Gdx.graphics.getDeltaTime()); }
			else { settingsScreen.render(Gdx.graphics.getDeltaTime()); }

			if(assets.hasFinishedLoading() == false) { assets.update(); }
		}
	}

	public void setGameScreen() {
		if(assets.ORIENTATION == assets.ORIENTATION_PORTRAIT) {
			showBannerAD();
		}

		this.setScreen(gameScreen);

		if(isFirstStartUp == true) {
			//assets.ORIENTATION = assets.ORIENTATION_LANDSCAPE;

			changeOrientation();

			isFirstStartUp = false;
		} else {
			if(settingsScreen.orientationChanged == true) { changeOrientation(); }
			else {
				if(assets.ORIENTATION == 1) { actionResolver.customActionRequestHandler(assets.ORIENTATION_PORTRAIT); assets.ORIENTATION = assets.ORIENTATION_PORTRAIT; }
				else if(assets.ORIENTATION == 2) { actionResolver.customActionRequestHandler(assets.ORIENTATION_LANDSCAPE); assets.ORIENTATION = assets.ORIENTATION_LANDSCAPE; }
			}
		}
	}

	public void setSettingsScreen() {
		hideBannerAD();
		actionResolver.customActionRequestHandler(assets.ORIENTATION_PORTRAIT);
		this.setScreen(settingsScreen);
	}

	public void setInfoScreen() {
		hideBannerAD();
		actionResolver.customActionRequestHandler(assets.ORIENTATION_PORTRAIT);
		this.setScreen(infoScreen);
	}

	public void setShowGooglePlayLeaderBoardsScreen() { /*actionResolver.showLeaderBoardUI();*/ }

	public void setShowGooglePlayAchievmentsScreen() { /*actionResolver.showAchievementUI();*/ }

	public void googlePlaySignOut() { /*actionResolver.signOut();*/ }

	public void googlePlayCheckIfSignedIn() { /*IS_LOGGED_IN = actionResolver.isSignedIn();*/ }

	public void unlockAchievements(int currentStreakLenght) {
		//actionResolver.unlockAchievements(currentStreakLenght, assets.drawThree());
		//actionResolver.submitLongestStreak(assets.getNumLongestStreak());
		//actionResolver.submitOverAllWins(assets.getNumWinsOverAll());
	}

	public void changeOrientation() {
		if(assets.ORIENTATION == 1) { actionResolver.customActionRequestHandler(CHANGE_ORIENTATION_TO_PORTRAIT); gameScreen.changeOrientation(CHANGE_ORIENTATION_TO_PORTRAIT); settingsScreen.changeOrientation(CHANGE_ORIENTATION_TO_PORTRAIT); }
		else if(assets.ORIENTATION == 2) { actionResolver.customActionRequestHandler(CHANGE_ORIENTATION_TO_LANDSCAPE); gameScreen.changeOrientation(CHANGE_ORIENTATION_TO_LANDSCAPE); settingsScreen.changeOrientation(CHANGE_ORIENTATION_TO_LANDSCAPE); }
	}

	public void showAD() { actionResolver.customActionRequestHandler(SHOW_AD); }

	public void cacheAD() { actionResolver.customActionRequestHandler(CACHE_AD); }

	public void showBannerAD() { actionResolver.showAdmobBanner(); }

	public void hideBannerAD() { actionResolver.HideAdmobBanner(); }

	@Override
	public void dispose()
	{
		super.dispose();
	}
}
