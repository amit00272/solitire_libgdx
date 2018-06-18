package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

public class FadeInText {
    private CustomAssetManager assets;
    private Vector2 positionText;
    private float widthText;
    private String strLevel;
    private GlyphLayout layoutLevel;

    private float origScaleX;
    private float origScaleY;
    private float currentSizeX;
    private float currentSizeY;
    private float sizeToGetToX;
    private float sizeToGetToY;

    private float alpha;
    private float deltaAlpha;
    private float deltaSizeX;
    private float deltaSizeY;

    private float startY;
    private BitmapFont font;

    private float startTimer;
    private float delay;

    private boolean isAnimationFinished;

    public FadeInText(CustomAssetManager assets, BitmapFont font, float startY, float delay) {
        this.assets = assets;
        this.font = font;
        this.startY = startY;
        this.delay = delay;
        positionText = new Vector2(0, 0);
        widthText = assets.V_GAMEWIDTH;
        strLevel = "";
        layoutLevel = new GlyphLayout();

        alpha = 0f;
        deltaAlpha = ((0 - 1) / assets.GAMEOVER_TEXT_ANIMATION) * (-1);

        origScaleX = font.getScaleX();
        origScaleY = font.getScaleY();

        sizeToGetToX = origScaleX * 2.5f;
        sizeToGetToY = origScaleY * 2.5f;

        deltaSizeX = (origScaleX - sizeToGetToX) / assets.GAMEOVER_TEXT_ANIMATION * (-1);
        deltaSizeY = (origScaleY - sizeToGetToY) / assets.GAMEOVER_TEXT_ANIMATION * (-1);
    }

    public void update(float delta) {
        startTimer += delta;

        if(startTimer >= delay) {
            if(alpha < 1) {
                alpha += deltaAlpha * delta;

                if(alpha > 1) { alpha = 1; }
            }

            if(currentSizeX != sizeToGetToX) {
                currentSizeX += deltaSizeX * delta;

                if(currentSizeX > sizeToGetToX) { currentSizeX = sizeToGetToX; }
            }

            if(currentSizeY != sizeToGetToY) {
                currentSizeY += deltaSizeY * delta;

                if(currentSizeY < sizeToGetToY) { currentSizeY = sizeToGetToY; }
            }

            if(currentSizeX == sizeToGetToX && currentSizeY == sizeToGetToY) { isAnimationFinished = true; }

            font.setColor(1, 1, 1, alpha);

            font.getData().setScale(currentSizeX, currentSizeY);

            layoutLevel.setText(font, strLevel, font.getColor(), widthText, Align.center, true);

            positionText.y = ((assets.V_GAMEHEIGHT + layoutLevel.height * 0.5f) * 0.5f + startY);
        }
    }

    public void draw(SpriteBatch batch) {
        font.draw(batch, strLevel, positionText.x, positionText.y, widthText, Align.center, false);
    }

    public void reset() {
        alpha = 0;

        startTimer = 0;

        isAnimationFinished = false;

        currentSizeX = origScaleX;
        currentSizeY = origScaleY;

        font.getData().setScale(origScaleX, origScaleY);

        font.setColor(1, 1, 1, alpha);
    }

    public void setText(String strLevel) {
        this.strLevel = strLevel;

        layoutLevel.setText(font, strLevel, font.getColor(), widthText, Align.center, true);

        positionText.y = ((assets.V_GAMEHEIGHT + layoutLevel.height * 0.5f) * 0.5f);
    }

    public boolean isAnimationFinished() { return isAnimationFinished; }

    public void changeOrientation(int newOrientation) {
        origScaleX = font.getScaleX();
        origScaleY = font.getScaleY();

        sizeToGetToX = origScaleX * 2.5f;
        sizeToGetToY = origScaleY * 2.5f;

        deltaSizeX = (origScaleX - sizeToGetToX) / assets.GAMEOVER_TEXT_ANIMATION * (-1);
        deltaSizeY = (origScaleY - sizeToGetToY) / assets.GAMEOVER_TEXT_ANIMATION * (-1);

        currentSizeX = origScaleX;
        currentSizeY = origScaleY;

        widthText = assets.V_GAMEWIDTH;

        setText(strLevel);
    }
}
