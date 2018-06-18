package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

public class AutoCompleteButton {
    private CustomAssetManager assets;
    private CenteredText txtAutoComplete;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean isPressed;

    public AutoCompleteButton(CustomAssetManager assets) {
        this.assets = assets;
        txtAutoComplete = new CenteredText(assets, 0, assets.V_GAMEHEIGHT - 130, assets.V_GAMEWIDTH, 50, assets.font);
        txtAutoComplete.setAlpha(0.8f);
        txtAutoComplete.setText("auto complete");

        x = txtAutoComplete.getX() - 10;
        y = txtAutoComplete.getY() - ((txtAutoComplete.getLayoutHeight() + txtAutoComplete.getHeight()) / 2);
        width = txtAutoComplete.getLayoutWidth() + 20;
        height = txtAutoComplete.getHeight();
    }

    public void changeOrientation(int newOrientation) {
        if(newOrientation == 1) {
            txtAutoComplete.getFont().getData().setScale(assets.font.getScaleX(), assets.font.getScaleY());
            txtAutoComplete.setWidth(assets.V_GAMEWIDTH);
            txtAutoComplete.setHeight(50);
            txtAutoComplete.setX(0);
            txtAutoComplete.setY(assets.V_GAMEHEIGHT - 130);

            x = txtAutoComplete.getX() - 10;
            y = txtAutoComplete.getY() - ((txtAutoComplete.getLayoutHeight() + txtAutoComplete.getHeight()) / 2);
            width = txtAutoComplete.getLayoutWidth() + 20;
            height = txtAutoComplete.getHeight();
        } else {
            txtAutoComplete.getFont().getData().setScale(assets.font.getScaleX(), assets.font.getScaleY());
            txtAutoComplete.setWidth(assets.V_GAMEWIDTH * 0.35f);
            txtAutoComplete.setHeight(50);
            txtAutoComplete.setX((assets.V_GAMEWIDTH - txtAutoComplete.getWidth()) * 0.5f);
            txtAutoComplete.setY(assets.V_GAMEHEIGHT - 110);

            x = txtAutoComplete.getX() - 10;
            y = txtAutoComplete.getY() - ((txtAutoComplete.getLayoutHeight() + txtAutoComplete.getHeight()) / 2);
            width = txtAutoComplete.getLayoutWidth() + 20;
            height = txtAutoComplete.getHeight();
        }
    }

    public void draw(SpriteBatch batch) {
        if(isPressed) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
            batch.draw(assets.UI_Black_rounded, x, y + 5, width, height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);

            txtAutoComplete.draw(batch);
        } else {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.5f);
            batch.draw(assets.UI_Black_rounded, x, y, width, height);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);

            txtAutoComplete.draw(batch);
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

        if(isPressed) { txtAutoComplete.setY(y + 5); }
        else { txtAutoComplete.setY(y); }
    }
}