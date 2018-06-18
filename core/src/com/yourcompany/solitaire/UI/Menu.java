package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.Helper.Variables;

public class Menu
{

    private CustomAssetManager assets;
    SimpleBackground menuBackground;
    SimpleButton btnMenuPlay;
    SimpleButton btnMenuSettings;
    SimpleButton btnMenuStatistic;
    SimpleButton btnMenuInfo;
    SimpleButton btnMenuUndo;
    SimpleButton btnMenuHint;
    SimpleButton btnGooglePlay;

    boolean isScrolling;
    boolean isShowing;

    private float buttonWidth;
    private float buttonHeight;

    private String direction;

    public Sprite getSprite(String s){

        Texture t = new Texture(Gdx.files.internal(s));
                t.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

                return new Sprite(t);

    }


    public Menu(CustomAssetManager assets)
    {
        this.assets = assets;
        menuBackground = new SimpleBackground(assets.UI_Black, 0, assets.V_GAMEHEIGHT, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);
        btnMenuPlay = new SimpleButton(getSprite("data/new/play.png"), 0, assets.V_GAMEHEIGHT, 50, 50);
        btnMenuSettings = new SimpleButton(getSprite("data/new/setting.png"), 50, assets.V_GAMEHEIGHT, 50, 50);

        Sprite statics =  getSprite("data/new/leaderboard.png");
                statics.flip(true,false);

        btnMenuStatistic = new SimpleButton(statics, assets.V_GAMEWIDTH-50*3, assets.V_GAMEHEIGHT, 50, 50);

        Sprite info =getSprite("data/new/info.png");
                info.flip(true,false);

                btnMenuInfo = new SimpleButton(info, assets.V_GAMEWIDTH-50, assets.V_GAMEHEIGHT, 49, 49);

        Sprite lampp =getSprite("data/new/lamp.png");
               lampp.flip(true,false);

        Sprite googlePlay =getSprite("data/new/googleplay.png");
               googlePlay.flip(true,false);

        btnMenuUndo = new SimpleButton(getSprite("data/new/return.png"), (assets.V_GAMEWIDTH-50) * 0.5f, assets.V_GAMEHEIGHT, 50, 50);
        btnMenuHint = new SimpleButton(lampp, 100, assets.V_GAMEHEIGHT, 50, 50);
        btnGooglePlay = new SimpleButton(googlePlay, assets.V_GAMEWIDTH-50*2, assets.V_GAMEHEIGHT, 50, 50);

    }

    public void changeOrientation(int newOrientation) {
        if(newOrientation == 1) {
            buttonWidth = 50;
            buttonHeight = 50;
        } else {
            buttonWidth = 18;
            buttonHeight = 50;
        }

        btnMenuPlay.setSize(buttonWidth, buttonHeight);
        btnMenuSettings.setSize(buttonWidth, buttonHeight);
        btnMenuStatistic.setSize(buttonWidth, buttonHeight);
        btnMenuInfo.setSize(buttonWidth, buttonHeight);
        btnMenuUndo.setSize(buttonWidth, buttonHeight);
        btnMenuHint.setSize(buttonWidth, buttonHeight);
        btnGooglePlay.setSize(buttonWidth, buttonHeight);

        btnMenuPlay.setX(0);
        btnMenuSettings.setX(buttonWidth+5);
        btnMenuStatistic.setX((assets.V_GAMEWIDTH-buttonWidth*3));
        btnMenuInfo.setX(assets.V_GAMEWIDTH-buttonWidth);
        btnMenuUndo.setX((assets.V_GAMEWIDTH-buttonWidth) * 0.5f);
        btnGooglePlay.setX(assets.V_GAMEWIDTH-buttonWidth*2);
        btnMenuHint.setX((buttonWidth*2)+5);
    }

    public void update(float delta)
    {
        if(isScrolling)
        {
            if(direction.equals("up"))
            {
                menuBackground.update(delta);
                btnMenuPlay.update(delta);
                btnMenuSettings.update(delta);
                btnMenuStatistic.update(delta);
                btnMenuInfo.update(delta);
                btnMenuUndo.update(delta);
                btnMenuHint.update(delta);
                btnGooglePlay.update(delta);

                if(menuBackground.getY() <= assets.V_GAMEHEIGHT-50)
                {
                    menuBackground.setY(assets.V_GAMEHEIGHT - 50);
                    btnMenuPlay.setY(assets.V_GAMEHEIGHT - 50);
                    btnMenuSettings.setY(assets.V_GAMEHEIGHT - 50);
                    btnMenuStatistic.setY(assets.V_GAMEHEIGHT - 50);
                    btnMenuInfo.setY(assets.V_GAMEHEIGHT - 50);
                    btnMenuUndo.setY(assets.V_GAMEHEIGHT - 50);
                    btnMenuHint.setY(assets.V_GAMEHEIGHT - 50);
                    btnGooglePlay.setY(assets.V_GAMEHEIGHT - 50);

                    isScrolling = false;
                    isShowing = true;
                }
            } else
            {
                menuBackground.update2(delta);
                btnMenuPlay.update2(delta);
                btnMenuSettings.update2(delta);
                btnMenuStatistic.update2(delta);
                btnMenuInfo.update2(delta);
                btnMenuUndo.update2(delta);
                btnMenuHint.update2(delta);
                btnGooglePlay.update2(delta);

                if(menuBackground.getY() >= assets.V_GAMEHEIGHT)
                {
                    menuBackground.setY(assets.V_GAMEHEIGHT);
                    btnMenuPlay.setY(assets.V_GAMEHEIGHT);
                    btnMenuSettings.setY(assets.V_GAMEHEIGHT);
                    btnMenuStatistic.setY(assets.V_GAMEHEIGHT);
                    btnMenuInfo.setY(assets.V_GAMEHEIGHT);
                    btnMenuUndo.setY(assets.V_GAMEHEIGHT);
                    btnMenuHint.setY(assets.V_GAMEHEIGHT);
                    btnGooglePlay.setY(assets.V_GAMEHEIGHT);

                    isScrolling = false;
                    isShowing = false;
                }
            }
        }
    }

    public void draw(SpriteBatch batch)
    {
        menuBackground.draw(batch);
        btnMenuPlay.draw(batch);
        btnMenuSettings.draw(batch);
        btnMenuStatistic.draw(batch);
        btnMenuInfo.draw(batch);
        btnMenuUndo.draw(batch);
        btnMenuHint.draw(batch);
        if(Variables.USES_GOOGLE_PLAY) { btnGooglePlay.draw(batch); }
    }

    public boolean onTouchBtnPlay(float fingerX, float fingerY)
    {
        if(btnMenuPlay.onTouch(fingerX, fingerY)) { return true; }

        return false;
    }

    public boolean onTouchBtnSettings(float fingerX, float fingerY)
    {
        if(btnMenuSettings.onTouch(fingerX, fingerY)) { return true; }

        return false;
    }

    public boolean onTouchBtnStatistics(float fingerX, float fingerY)
    {
        if(btnMenuStatistic.onTouch(fingerX, fingerY)) { return true; }

        return false;
    }

    public boolean onTouchBtnInfo(float fingerX, float fingerY)
    {
        if(btnMenuInfo.onTouch(fingerX, fingerY)) { return true; }

        return false;
    }

    public boolean onTouchBtnUndo(float fingerX, float fingerY)
    {
        if(btnMenuUndo.onTouch(fingerX, fingerY)) { return true; }

        return false;
    }

    public boolean onTouchBtnHint(float fingerX, float fingerY)
    {
        if(btnMenuHint.onTouch(fingerX, fingerY)) { return true; }

        return false;
    }

    public boolean onTouchBtnGooglePlay(float fingerX, float fingerY)
    {
        if(btnGooglePlay.onTouch(fingerX, fingerY) && Variables.USES_GOOGLE_PLAY) { return true; }

        return false;
    }

    public boolean isScrolling() { return isScrolling; }

    public void setScrolling(boolean bool, String direction) { isScrolling = bool; this.direction = direction; }

    public boolean isShowing() { return isShowing; }

    public void setShowing(boolean bool) { isShowing = bool; }

    public void reset()
    {
        menuBackground.setY(assets.V_GAMEHEIGHT);
        btnMenuPlay.setY(assets.V_GAMEHEIGHT);
        btnMenuSettings.setY(assets.V_GAMEHEIGHT);
        btnMenuStatistic.setY(assets.V_GAMEHEIGHT);
        btnMenuInfo.setY(assets.V_GAMEHEIGHT);
        btnMenuUndo.setY(assets.V_GAMEHEIGHT);
        btnMenuHint.setY(assets.V_GAMEHEIGHT);
        btnGooglePlay.setY(assets.V_GAMEHEIGHT);

        isScrolling = false;
        isShowing = false;
    }
}