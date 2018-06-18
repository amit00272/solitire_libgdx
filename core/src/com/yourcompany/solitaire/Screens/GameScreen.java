package com.yourcompany.solitaire.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.yourcompany.solitaire.GameWorld_And_Renderer.GameRenderer;
import com.yourcompany.solitaire.GameWorld_And_Renderer.GameWorld;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.Helper.InputHandler;

import static com.yourcompany.solitaire.Helper.Variables.SHOW_AD_AFTER_EVERY_X_LEVEL;
import static com.yourcompany.solitaire.Helper.Variables.GAME_COUNTER;
import static com.yourcompany.solitaire.Helper.Variables.USES_GOOGLE_PLAY;

import com.yourcompany.solitaire.Solitaire;

public class GameScreen implements Screen
{
    private CustomAssetManager assets;
    GameWorld gameWorld;
    GameRenderer gameRenderer;
    InputHandler inputHandler;

    Solitaire solitaire;

    float adTimer;
    float checkIfLoggedInToGooglePlayTimer = 15;

    public GameScreen(Solitaire solitaire)
    {
        this.solitaire = solitaire;
        this.assets = solitaire.assets;

        gameWorld = new GameWorld(assets);
        gameRenderer = new GameRenderer(gameWorld);
        inputHandler = new InputHandler(gameWorld, gameRenderer.getCamera());

        gameWorld.resetGame("Random");
    }

    public void changeOrientation(int newOrientation) {
        gameWorld.changeOrientation(newOrientation);
        gameRenderer.changeOrientation(newOrientation);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void render(float delta)
    {
        gameWorld.update(delta);
        gameRenderer.render(delta);

        if(gameWorld.showSettingsScreen()) { solitaire.setSettingsScreen(); gameWorld.setShowSettingsScreen(false); }
        if(gameWorld.showInfoScreen()) { solitaire.setInfoScreen(); gameWorld.setShowInfoScreen(false); }

        if(USES_GOOGLE_PLAY) {
            if(gameWorld.showGooglePlayLeaderBoards()) { solitaire.setShowGooglePlayLeaderBoardsScreen(); gameWorld.setShowGoogleLeaderBoards(false); }
            if(gameWorld.showGooglePlayAchievments()) { solitaire.setShowGooglePlayAchievmentsScreen(); gameWorld.setShowGoogleAchievments(false); }
            if(gameWorld.googlePlaySignOut()) { solitaire.googlePlaySignOut(); gameWorld.setGooglePlaySignOut(false); }
            if(gameWorld.isUnlockAchievments()) { solitaire.unlockAchievements(gameWorld.getConsecutiveGameCounter()); gameWorld.setUnlockAchievments(false); }
        }

        if(gameWorld.isNewGame()) {
            if(GAME_COUNTER == 1) {
                // First StartUp
                solitaire.cacheAD(); gameWorld.setIsADCached(true);
            } else {
                if(GAME_COUNTER % SHOW_AD_AFTER_EVERY_X_LEVEL == 0) { solitaire.showAD(); gameWorld.setIsADCached(false); }
            }

            adTimer = 0;

            gameWorld.setIsNewGame(false);
        } else {
            adTimer += delta;

            if(adTimer >= 5) { if(gameWorld.isADCached() == false) { solitaire.cacheAD(); gameWorld.setIsADCached(true); } }
        }

        if(USES_GOOGLE_PLAY) {
            checkIfLoggedInToGooglePlayTimer += delta;

            if(checkIfLoggedInToGooglePlayTimer >= 25.0) { checkIfLoggedInToGooglePlayTimer = 0; solitaire.googlePlayCheckIfSignedIn(); }
        }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
