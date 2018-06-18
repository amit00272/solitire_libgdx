package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class SimpleBackground
{
    private float x;
    private float y;
    private float originalX;
    private float originalY;
    private float width;
    private float height;
    private TextureRegion tex;

    public SimpleBackground(TextureRegion tex, float x, float y, float width, float height)
    {
        this.tex = tex;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        originalX = x;
        originalY = y;
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
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.15f);
        batch.draw(tex, x, y, width, height);
        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
    }

    public float getX() { return x; }

    public float getY() { return y; }

    public void setY(float val) { y = val; }

    public float getWidth() { return width; }

    public float getheight() { return height; }
}