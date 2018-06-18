package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class ArrowButton
{
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean isPressed;
    private TextureRegion tex;

    public ArrowButton(TextureRegion tex, float x, float y, float width, float height)
    {
        this.tex = tex;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public boolean onTouch(float fingerX, float fingerY)
    {
        if(fingerX >= x && fingerX <= x + width && fingerY >= y && fingerY <= y + height) { return true; }

        return false;
    }

    public void draw(SpriteBatch batch)
    {
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.25f);
        batch.draw(tex, x, y, width, height);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);
    }

    public float getX() {
        return x;
    }
    public void setX(float val) { x = val; }

    public float getY() { return y; }
    public void setY(float val) { y = val; }

    public float getWidth() { return width; }
    public void setWidth(float val) { width = val; }

    public float getHeight() { return height; }
    public void setHeight(float val) { height = val; }

    public boolean isPressed() { return isPressed; }

    public void setPressed(boolean val) { isPressed = val; }
}
