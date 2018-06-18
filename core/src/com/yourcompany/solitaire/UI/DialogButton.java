package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

public class DialogButton
{
    private CustomAssetManager assets;
    private float x;
    private float y;
    private float width;
    private float height;
    private String text;
    private String id;
    private boolean isPressed;

    private GlyphLayout layout;
    private float centerTextX;
    private boolean centerText;

    public DialogButton(CustomAssetManager assets, String text, String id, float x, float y, float width, float height)
    {
        this.assets = assets;
        this.text = text;
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.layout = new GlyphLayout();
        this.centerText = false;
    }

    public boolean onTouch(float fingerX, float fingerY)
    {
        if(fingerX >= x && fingerX <= x + width && fingerY >= y && fingerY <= y + height) { return true; }

        return false;
    }

    public void draw(SpriteBatch batch)
    {
        if(!isPressed) { batch.draw(assets.UI_White, x, y, width, height); }
        else { batch.draw(assets.UI_Orange, x, y, width, height); }

        if(!centerText) {
            assets.dialogButtonFont.draw(batch, text, x+4, y+22.5f);
        } else {
            assets.dialogButtonFont.draw(batch, text, centerTextX, y+22.5f);
        }
        batch.draw(assets.UI_Line, x, y+height-1, width, 1);
    }

    public float getX() { return x; }

    public void setX(float val) { x = val; }

    public float getY() { return y; }

    public void setY(float val) { y = val; }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public void setSize(float w, float h) { width = w; height = h; }

    public String getId() { return id; }

    public boolean isPressed() { return isPressed; }

    public void setPressed(boolean val) { isPressed = val; }

    public void setCenterText(boolean bool) {
        centerText = bool;

        layout.setText(assets.dialogButtonFont, text);

        centerTextX = x;

        centerTextX += (width / 2.0f) - (layout.width / 2.0f);
    }
}
