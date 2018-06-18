package com.yourcompany.solitaire.UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;

import java.util.ArrayList;

public class Dialog
{
    private CustomAssetManager assets;
    private float x;
    private float y;
    private float width;
    private float height;
    private float origWidth, origHeight;
    private boolean isShowing;
    private boolean isExitDialog;
    private String title;
    private ArrayList<DialogButton> buttons = new ArrayList<DialogButton>();

    public Dialog(CustomAssetManager assets, String title, float x, float y, float width, float height)
    {
        this.assets = assets;
        this.title = title;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.isExitDialog = false;

        origWidth = width;
        origHeight = height;
    }

    public Dialog(String title, float x, float width)
    {
        this.title = title;
        this.x = x;
        this.width = width;
    }

    public void changeOrientation(int newOrientation) {
        if(!isExitDialog) {
            if(newOrientation == 1) {
                width = origWidth;
                height = origHeight;
                x = (assets.V_GAMEWIDTH-width) * 0.5f;
                y = (assets.V_GAMEHEIGHT-height-height*buttons.size()) * 0.5f;

                for(int i = 0;i < buttons.size();i++) {
                    buttons.get(i).setX(x);
                    buttons.get(i).setSize(width, height);
                    buttons.get(i).setY(y + (i+1) * height);
                }

            } else {
                width = 95;
                x = (assets.V_GAMEWIDTH-width)/2;
                height = 55;
                y = (assets.V_GAMEHEIGHT-height-height*buttons.size()) * 0.5f;

                for(int i = 0;i < buttons.size();i++) {
                    buttons.get(i).setX(x);
                    buttons.get(i).setSize(width, height);
                    buttons.get(i).setY(y + (i+1) * height);
                }
            }
        } else {
            if(newOrientation == 1) {
                width = origWidth;
                height = origHeight;
                x = (assets.V_GAMEWIDTH-width) * 0.5f;
                y = (assets.V_GAMEHEIGHT-height-height*1) * 0.5f;

                for(int i = 0;i < buttons.size();i++) {
                    buttons.get(i).setX(x + i * width/2);
                    buttons.get(i).setSize(width / 2, height);
                    buttons.get(i).setY(y + (1) * height);
                    buttons.get(i).setCenterText(true);
                }

            } else {
                width = 95;
                x = (assets.V_GAMEWIDTH-width)/2;
                height = 55;
                y = (assets.V_GAMEHEIGHT-height-height*1) * 0.5f;

                for(int i = 0;i < buttons.size();i++) {
                    buttons.get(i).setX(x + i * width/2);
                    buttons.get(i).setSize(width / 2, height);
                    buttons.get(i).setY(y + (1) * height);
                    buttons.get(i).setCenterText(true);
                }
            }
        }
    }

    public void addButton(String text, String id)
    {
        buttons.add(new DialogButton(assets, text, id, x, y + (buttons.size() + 1) * height, width, height));

        y = (assets.V_GAMEHEIGHT-(buttons.size())*height)/2;

        for(int i = 0;i < buttons.size();i++)
        {
            buttons.get(i).setY(y + i * height);
        }

        y -= height;
    }

    public void addButton2(String text, String id)
    {
        buttons.add(new DialogButton(assets,  text, id, x, y + 1 * height, width / 2, height));

        for(int i = 0;i < buttons.size();i++)
        {
            buttons.get(i).setY(y + 1 * height);
            buttons.get(i).setX(x + i * width / 2);
            buttons.get(i).setCenterText(true);
        }

        isExitDialog = true;
    }

    public void draw(SpriteBatch batch)
    {
        if(!isExitDialog) {
            if(assets.ORIENTATION == 1) {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.4f);
                batch.draw(assets.UI_Black, 0, 0, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);

                batch.draw(assets.UI_Frame, x - 1, y - 1, width + 2, (buttons.size() + 1) * height + 1);
                batch.draw(assets.UI_Frame2, x - 2.0f, y - 1.0f, width + 4.0f, (buttons.size() + 1) * height + 3.0f);
                batch.draw(assets.UI_Black, x, y, width, height);
                assets.dialogFont.draw(batch, title, x + 4, y + 20.5f);

                for(int i = 0;i < buttons.size();i++)
                {
                    buttons.get(i).draw(batch);
                }
            } else {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.4f);
                batch.draw(assets.UI_Black, 0, 0, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);

                batch.draw(assets.UI_Frame, x - 1, y - 1, width + 2, (buttons.size() + 1) * height + 1);
                batch.draw(assets.UI_Frame2, x - 2.0f, y - 1.0f, width + 4.0f, (buttons.size() + 1) * height + 5.0f);
                batch.draw(assets.UI_Black, x, y, width, height);
                assets.dialogFont.draw(batch, title, x + 4, y + 20.5f);

                for(int i = 0;i < buttons.size();i++)
                {
                    buttons.get(i).draw(batch);
                }
            }
        } else {
            if(assets.ORIENTATION == 1) {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.4f);
                batch.draw(assets.UI_Black, 0, 0, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);

                batch.draw(assets.UI_Frame, x - 1, y - 1, width + 2, (2) * height + 1);
                batch.draw(assets.UI_Frame2, x - 2.0f, y - 1.0f, width + 4.0f, (2) * height + 3.0f);
                batch.draw(assets.UI_Black, x, y, width, height);
                assets.dialogFont.draw(batch, title, x + 4, y + 20.5f);

                for(int i = 0;i < buttons.size();i++)
                {
                    buttons.get(i).draw(batch);
                }

                batch.draw(assets.UI_Line, x+width/2-0.5f, y+height, 1, height);
            } else {
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.4f);
                batch.draw(assets.UI_Black, 0, 0, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);
                batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);

                batch.draw(assets.UI_Frame, x - 1, y - 1, width + 2, (2) * height + 1);
                batch.draw(assets.UI_Frame2, x - 2.0f, y - 1.0f, width + 4.0f, (2) * height + 5.0f);
                batch.draw(assets.UI_Black, x, y, width, height);
                assets.dialogFont.draw(batch, title, x + 4, y + 20.5f);

                for(int i = 0;i < buttons.size();i++)
                {
                    buttons.get(i).draw(batch);
                }

                batch.draw(assets.UI_Line, x+width/2-0.5f, y+height, 1, height);
            }
        }
    }

    public void onTouch(float fingerX, float fingerY)
    {
        for(int i = 0;i < buttons.size();i++)
        {
            if(buttons.get(i).onTouch(fingerX, fingerY))
            {
                if(getPressedButtonID().equals("Nothing"))
                {
                    buttons.get(i).setPressed(true);
                }

                break;
            }
        }
    }

    public boolean isStillOnTouch(String pressedButtonID, float fingerX, float fingerY)
    {
        for(int i = 0;i < buttons.size();i++)
        {
            if(buttons.get(i).getId().equals(pressedButtonID) && buttons.get(i).onTouch(fingerX, fingerY))
            {
                return true;
            }
        }

        return false;
    }

    public String getPressedButtonID()
    {
        for(int i = 0;i < buttons.size();i++)
        {
            if(buttons.get(i).isPressed()) { return buttons.get(i).getId(); }
        }

        return "Nothing";
    }

    public void reset()
    {
        for(int i = 0;i < buttons.size();i++)
        {
            buttons.get(i).setPressed(false);
        }

        isShowing = false;
    }

    public void reset2()
    {
        for(int i = 0;i < buttons.size();i++)
        {
            buttons.get(i).setPressed(false);
        }
    }

    public void show() { isShowing = true; }

    public boolean isShowing() { return isShowing; }
}