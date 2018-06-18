package com.yourcompany.solitaire.GameObjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;

public class Card
{
    private Vector2 position;
    private float width;
    private float height;
    private int value;
    private char color;
    private int id;
    private boolean isRevealed;
    private boolean isFlipping;

    private boolean isMoveAble;

    public Card(float x, float y, float width, float height, int id)
    {
        this.position = new Vector2(x, y);
        this.width = width;
        this.height = height;
        this.id = id;
        this.isRevealed = false;
        this.isFlipping = false;
        this.isMoveAble = false;
    }

    public boolean onTouch(float fingerX, float fingerY)
    {
        if(fingerX >= position.x && fingerX <= position.x + width && fingerY >= position.y && fingerY <= position.y + height && isRevealed)
        {
            //Gdx.app.log("", "ID = " + id + ", Value = " + value + ", Color = " + color);
            return true;
        }

        return false;
    }

    public float getX() { return position.x; }
    public void setX(float val) { position.x = val; }

    public float getY() { return position.y; }
    public void setY(float val) { position.y = val; }

    public float getWidth() { return width; }
    public void setWidth(float val) { width = val; }

    public float getHeight() { return height; }
    public void setHeight(float val) { height = val; }

    public int getId() { return id; }
    public void setId(int val) { id = val; }

    public boolean isRevealed() { return isRevealed; }
    public void setRevealed(boolean bool) { isRevealed = bool; }

    public boolean isFlipping() { return isFlipping; }
    public void setFlipping(boolean bool) { isFlipping = bool; }

    public int getValue() { return value; }
    public void setValue(int val) { value = val; }

    public char getColor() { return color; }
    public void setColor(char val) { color = val; }

    public boolean isMoveAble() { return isMoveAble; }
    public void setMoveAble(boolean bool) { isMoveAble = bool; }
}
