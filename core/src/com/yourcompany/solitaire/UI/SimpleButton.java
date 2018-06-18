package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SimpleButton
{
    private float x;
    private float y;
    private float originalX;
    private float originalY;
    private float width;
    private float height;
    private boolean isPressed;
    private Sprite tex;
    private TextureRegion[] tex2;

    private boolean hasPressDownImage;

    public SimpleButton(Sprite tex, float x, float y, float width, float height)
    {
        this.tex = tex;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        originalX = x;
        originalY = y;

        hasPressDownImage = false;
    }



    public boolean onTouch(float fingerX, float fingerY)
    {
        if(fingerX >= x && fingerX <= x + width && fingerY >= y && fingerY <= y + height) { return true; }

        return false;
    }

    public void update(float delta)
    {
        y -= 400 * delta;
    }

    public void update2(float delta)
    {
        y += 450 * delta;
    }

    public void draw(SpriteBatch batch)
    {
        if(hasPressDownImage == false) {
            if(isPressed()) {
                batch.draw(tex, x, y + 5, width, height);
            } else {
                batch.draw(tex, x, y, width, height);
            }
        } else {
            if(isPressed()) {
                batch.draw(tex2[0], x, y, width, height);
            } else {
                batch.draw(tex2[1], x, y, width, height);
            }
        }
    }

    public float getX() { return x; }

    public void setX(float val) { x = val; }

    public float getY() { return y; }

    public void setY(float val) { y = val; }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public void setWidth(float val) { width = val; }

    public void setHeight(float val) { height = val; }

    public void setSize(float w, float h) { width = w; height = h; }

    public boolean isPressed() { return  isPressed; }

    public void setPressed(boolean val) { isPressed = val; }
}