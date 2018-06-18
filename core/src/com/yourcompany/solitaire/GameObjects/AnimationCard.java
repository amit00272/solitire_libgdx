package com.yourcompany.solitaire.GameObjects;

import com.badlogic.gdx.math.Vector2;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

import java.util.Random;

public class AnimationCard {
    private CustomAssetManager assets;
    private Vector2 position;
    private Vector2 toPosition;
    private Vector2 velocity;
    private float width;
    private float height;
    private int ID;
    private boolean isRevealed;

    private Random random;

    private float startTimer;
    private float delay;

    private float amountMovedX;
    private float amountMovedY;
    private float amountToMoveX;
    private float amountToMoveY;
    private boolean isAnimationStarted;
    private boolean isAnimationFinished;

    private float flipStartTimer;
    private float flipDelay;

    private boolean isFlipAnimationStarted;
    private boolean isFlipAnimationFinished;

    private boolean isFree;
    private float isFreeTimer;
    private float alpha;

    public AnimationCard(CustomAssetManager assets, float width, float height) {
        this.assets = assets;
        this.position = new Vector2();
        this.toPosition = new Vector2();
        this.velocity = new Vector2();
        this.width = width;
        this.height = height;
        this.ID = 0;
        this.isRevealed = false;

        this.random = new Random();

        this.isAnimationStarted = false;
        this.isAnimationFinished = false;
        this.isFlipAnimationStarted = false;
        this.isFlipAnimationFinished = false;

        this.isFree = true;
        this.alpha = 0.8f;
    }

    public void update(float delta) {
        startTimer += delta;

        if(startTimer >= delay) { isAnimationStarted = true; }

        if(isAnimationFinished == false && isAnimationStarted == true) {
            setX(getX() + velocity.x * delta);
            setY(getY() + velocity.y * delta);

            amountMovedX += Math.abs(velocity.x * delta);
            amountMovedY += Math.abs(velocity.y * delta);

            if(amountMovedX >= amountToMoveX) { position.x = toPosition.x; }
            if(amountMovedY >= amountToMoveY) { position.y = toPosition.y; }

            if(amountMovedX >= amountToMoveX && amountMovedY >= amountToMoveY) { isAnimationFinished = true; isFreeTimer = 0; }
        }

        if(isFree == false) {
            isFreeTimer += delta;

            if(isFreeTimer >= 1.0f) { isFree = true; }
        }
    }

    public void update2(float delta) {
        flipStartTimer += delta;

        if(!isFlipAnimationStarted) { } if(flipStartTimer >= flipDelay) { isFlipAnimationStarted = true; }

        if(isFlipAnimationFinished == false && isFlipAnimationStarted == true) {
            setWidth(getWidth() - assets.FLIP_SPEED * delta);
            setX(getX() + (assets.FLIP_SPEED * 0.5f) * delta);

            if (getWidth() <= 0) {
                setRevealed(true);

                setWidth(getWidth() - assets.FLIP_SPEED * delta);
                setX(getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                if (getWidth() <= - assets.CARDWIDTH) {
                    setX(toPosition.x);
                    setWidth(assets.CARDWIDTH);

                    isFlipAnimationFinished = true;
                }
            }
        }
    }

    public void calculateSpeed(int id, boolean isRevealed, float x, float y, float toX, float toY, float delay) {
        this.ID = id;
        this.isRevealed = isRevealed;
        this.position.x = x;
        this.position.y = y;
        this.toPosition.x = toX;
        this.toPosition.y = toY;
        this.delay = delay;

        velocity.x = ((position.x - toPosition.x) / 0.25f) * (-1);
        velocity.y = ((position.y - toPosition.y) / 0.25f) * (-1);

        amountMovedX = 0;
        amountMovedY = 0;

        amountToMoveX = Math.abs(toPosition.x - position.x);
        amountToMoveY = Math.abs(toPosition.y - position.y);

        startTimer = 0;

        isAnimationStarted = false;
        isAnimationFinished = false;

        isFree = false;
        isFreeTimer = -100;
    }

    public void calculateSpeed2(int id, boolean isRevealed, float x, float y, float toX, float toY, float delay, float flipDelay) {
        this.ID = id;
        this.isRevealed = isRevealed;
        this.position.x = x;
        this.position.y = y;
        this.toPosition.x = toX;
        this.toPosition.y = toY;
        this.delay = delay;
        this.flipDelay = flipDelay;

        velocity.x = ((position.x - toPosition.x) / 0.25f) * (-1);
        velocity.y = ((position.y - toPosition.y) / 0.25f) * (-1);

        amountMovedX = 0;
        amountMovedY = 0;

        amountToMoveX = Math.abs(toPosition.x - position.x);
        amountToMoveY = Math.abs(toPosition.y - position.y);

        startTimer = 0;
        flipStartTimer = 0;

        isAnimationStarted = false;
        isAnimationFinished = false;
        isFlipAnimationStarted = false;
        isFlipAnimationFinished = false;
    }

    public void calculateSpeed2(int id, boolean isRevealed, float x, float y, float toX, float toY, float delay) {
        this.ID = id;
        this.isRevealed = isRevealed;
        this.position.x = x;
        this.position.y = y;
        this.toPosition.x = toX;
        this.toPosition.y = toY;
        this.delay = delay;

        velocity.x = ((position.x - toPosition.x) / 0.25f) * (-1);
        velocity.y = ((position.y - toPosition.y) / 0.25f) * (-1);

        amountMovedX = 0;
        amountMovedY = 0;

        amountToMoveX = Math.abs(toPosition.x - position.x);
        amountToMoveY = Math.abs(toPosition.y - position.y);

        startTimer = 0;

        isAnimationStarted = false;
        isAnimationFinished = false;
    }

    public float getAlpha() { return alpha; }

    public void setX(float val) { position.x = val; }

    public void setY(float val) { position.y = val; }

    public void setWidth(float val) { width = val; }

    public void setHeight(float val) { height = val; }

    public void setToX(float val) { toPosition.x = val; }

    public void setToY(float val) { toPosition.y = val; }

    public void setRevealed(boolean bool) { isRevealed = bool; }

    public void setID(int val) { ID = val; }

    public void setIsAnimationStarted(boolean bool) { isAnimationStarted = bool; }

    public void setIsAnimationFinished(boolean bool) { isAnimationFinished = true; }

    public boolean isRevealed() { return isRevealed; }

    public float getX() { return position.x; }

    public float getY() { return position.y; }

    public float getWidth() { return width; }

    public float getHeight() { return height; }

    public int getID() { return ID; }

    public boolean isAnimationFinished() { return isAnimationFinished; }

    public boolean isAnimationStarted() { return isAnimationStarted; }

    public boolean isFlipAnimationStarted() { return isFlipAnimationStarted; }

    public boolean isFlipAnimationFinished() { return isFlipAnimationFinished; }

    public void setAnimationFinished(boolean bool) { isAnimationFinished = bool; }

    public boolean isFree() { return isFree; }

    public void setFree(boolean bool) { isFree = bool; }
}