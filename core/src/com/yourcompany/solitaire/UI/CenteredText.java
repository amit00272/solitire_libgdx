package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

public class CenteredText {
    private CustomAssetManager assets;
    private float box_height;
    private float box_width;
    private float box_x;
    private float box_y;
    private boolean centerX;
    private boolean centerY;
    private float alpha;
    private BitmapFont font;
    private boolean hasShadow;
    private GlyphLayout layout;
    private float origBox_x;
    private float origBox_y;
    private BitmapFont shadowFont;
    private float shadowOffSet;
    private String text;

    public CenteredText(CustomAssetManager assets, float box_x, float box_y, float box_width, float box_height, BitmapFont font) {
        this.assets = assets;
        this.box_x = box_x;
        this.box_y = box_y;
        this.origBox_x = box_x;
        this.origBox_y = box_y;
        this.box_width = box_width;
        this.box_height = box_height;
        this.text = "";
        this.font = font;
        this.layout = new GlyphLayout();
        this.hasShadow = false;
        this.centerX = true;
        this.centerY = true;
        this.alpha = 1f;
    }

    public void draw(SpriteBatch batch) {
        //batch.draw(assets.img_CardBack[0], origBox_x, origBox_y, box_width, box_height);
        //batch.draw(assets.img_CardBack[0], box_x, box_y, box_width, box_height);
        if (centerX && centerY) {
            if (hasShadow) {
                shadowFont.setColor(shadowFont.getColor().r, shadowFont.getColor().g, shadowFont.getColor().b, alpha);
                shadowFont.draw(batch, text, box_x + shadowOffSet, box_y - shadowOffSet);
                shadowFont.setColor(shadowFont.getColor().r, shadowFont.getColor().g, shadowFont.getColor().b, 1f);
            }

            font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, alpha);
            font.draw(batch, text, box_x, box_y);
            font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, 1f);

            return;
        }
        if (hasShadow) {
            shadowFont.setColor(shadowFont.getColor().r, shadowFont.getColor().g, shadowFont.getColor().b, alpha);
            shadowFont.draw(batch, text, origBox_x + shadowOffSet, box_y - shadowOffSet);
            shadowFont.setColor(shadowFont.getColor().r, shadowFont.getColor().g, shadowFont.getColor().b, 1f);
        }

        font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, alpha);
        font.draw(batch, text, origBox_x, box_y);
        font.setColor(font.getColor().r, font.getColor().g, font.getColor().b, 1f);
    }

    public void setDoNotCenterX() {
        centerX = false;
    }

    public void setX(float val) {
        origBox_x = val;
        setText(text);
    }

    public void setY(float val) {
        origBox_y = val;
        setText(text);
    }

    public void setText(String str) {
        text = str;
        layout.setText(font, text);
        box_x = origBox_x;
        box_y = origBox_y;
        box_x += (box_width / 2.0f) - (layout.width / 2.0f);
        box_y += (box_height / 2.0f) + (layout.height / 2.0f);
    }

    public void setHasNormalShadow(BitmapFont shadowFont) {
        shadowOffSet = 1.5f;
        hasShadow = true;
        this.shadowFont = shadowFont;
    }

    public void setHasSlightShadow(BitmapFont shadowFont) {
        shadowOffSet = 1.0f;
        hasShadow = true;
        this.shadowFont = shadowFont;
    }

    public void setAlpha(float val) { this.alpha = val; }

    public float getAlpha() { return alpha; }

    public float getX() {
        return box_x;
    }

    public float getY() {
        return box_y;
    }

    public float getWidth() {
        return box_width;
    }

    public void setWidth(float val) { box_width = val; }

    public float getHeight() {
        return box_height;
    }

    public void setHeight(float val) { box_height = val; }

    public float getLayoutWidth() { return layout.width; }

    public float getLayoutHeight() { return layout.height; }

    public BitmapFont getFont() { return font; }

    public String getText() {
        return text;
    }

    public void setFont(BitmapFont font) { this.font = font; }
}