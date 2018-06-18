package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

public class TextButton {
    private CustomAssetManager assets;
    private CenteredText txt;
    private float x;
    private float y;
    private float origY;
    private float width;
    private float height;
    private boolean isPressed;

    public TextButton(CustomAssetManager assets, float x, float y, float width, float height, String text) {
        this.assets = assets;
        txt = new CenteredText(assets, x, y, width, height, assets.font);
        txt.setAlpha(0.8f);
        txt.setText(text);

        this.origY = y;

        this.x = txt.getX() - 10;
        this.y = txt.getY() - ((txt.getLayoutHeight() + txt.getHeight()) / 2);
        this.width = txt.getLayoutWidth() + 20;
        this.height = txt.getHeight();
    }

    public void changeOrientation(int newOrientation) {
        if(newOrientation == 1) {
            txt.getFont().getData().setScale(assets.font.getScaleX(), assets.font.getScaleY());
            txt.setWidth(assets.V_GAMEWIDTH);
            txt.setHeight(50);
            txt.setX(0);
            txt.setY(origY);

            x = txt.getX() - 10;
            y = txt.getY() - ((txt.getLayoutHeight() + txt.getHeight()) / 2);
            width = txt.getLayoutWidth() + 20;
            height = txt.getHeight();
        }
    }

    public void changeOrientation2(float newY) {
        txt.getFont().getData().setScale(assets.font.getScaleX(), assets.font.getScaleY());
        txt.setWidth(assets.V_GAMEWIDTH * 0.35f);
        txt.setHeight(50);
        txt.setX((assets.V_GAMEWIDTH - txt.getWidth()) * 0.5f);
        txt.setY(newY);

        x = txt.getX() - 10;
        y = txt.getY() - ((txt.getLayoutHeight() + txt.getHeight()) / 2);
        width = txt.getLayoutWidth() + 20;
        height = txt.getHeight();
    }

    public void draw(SpriteBatch batch) {
        if(isPressed) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
            batch.draw(assets.UI_Black_rounded, x, y + 5, width, height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);

            txt.draw(batch);
        } else {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
            batch.draw(assets.UI_Black_rounded, x, y, width, height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);

            txt.draw(batch);
        }
    }

    public boolean onTouch(float fingerX, float fingerY) {
        if(fingerX >= x && fingerX <= x + width && fingerY >= y && fingerY <= y + height) { return true; }

        return false;
    }

    public boolean isPressed() { return isPressed; }

    public void setPressed(boolean bool)
    {
        isPressed = bool;

        if(isPressed) { txt.setY(y + 5); }
        else { txt.setY(y); }
    }
}