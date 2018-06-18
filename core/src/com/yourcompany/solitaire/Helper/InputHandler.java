package com.yourcompany.solitaire.Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.yourcompany.solitaire.GameObjects.Card;
import com.yourcompany.solitaire.GameWorld_And_Renderer.GameWorld;
import com.yourcompany.solitaire.Solitaire;
import com.yourcompany.solitaire.UI.ArrowButton;
import com.yourcompany.solitaire.UI.AutoCompleteButton;
import com.yourcompany.solitaire.UI.Dialog;
import com.yourcompany.solitaire.UI.Menu;

import java.util.ArrayList;

import static com.yourcompany.solitaire.Helper.Variables.IS_LOGGED_IN;

public class InputHandler implements InputProcessor
{
    private CustomAssetManager assets;
    private GameWorld gameWorld;
    private OrthographicCamera camera;
    private Vector3 touch;

    private Card[] cards;

    private ArrayList<Integer> StockStack;
    private ArrayList<Integer> StockStackRevealed;
    private ArrayList<Integer> MoveCards;

    private ArrowButton btnArrowUp;
    private ArrowButton btnArrowDown;
    private Menu menu;
    private Dialog dialog;
    private AutoCompleteButton btnAutoComplete;

    private boolean newGameTouchUp;

    public InputHandler(GameWorld gameWorld, OrthographicCamera camera)
    {
        this.gameWorld = gameWorld;
        this.assets = gameWorld.assets;
        this.camera = camera;

        touch = new Vector3();

        cards = gameWorld.getCards();
        StockStack = gameWorld.getStockStack();
        StockStackRevealed = gameWorld.getStockStackRevealed();
        MoveCards = gameWorld.getMoveCards();
        btnArrowDown = gameWorld.getBtnArrowDown();
        btnArrowUp = gameWorld.getBtnArrowUp();
        menu = gameWorld.getMenu();
        dialog = gameWorld.getDialog();
        btnAutoComplete = gameWorld.getBtnAutoComplete();

        newGameTouchUp = false;
    }

    @Override
    public boolean keyDown(int keycode) {


        if(gameWorld.getGameState() != GameWorld.GameState.GOOGLE_PLAY_SCREEN) {
            if (keycode == Input.Keys.BACK) {
                if(assets.SHOW_EXIT_DIALOG == false) {
                    if(assets.IS_BACK_BUTTONPRESSED) { Gdx.app.exit(); }
                    else { assets.IS_BACK_BUTTONPRESSED = true; }
                } else {
                    if(assets.IS_BACK_BUTTONPRESSED == false) { assets.IS_BACK_BUTTONPRESSED = true; gameWorld.exitDialog.show(); }
                }
            }
        }

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {

        Solitaire.actionResolver.ShowAdmobVideo();
        camera.unproject(touch.set(screenX, screenY, 0));

        switch(gameWorld.getGameState())
        {
            case GOOGLE_PLAY_SCREEN:
                if(gameWorld.btnShowGooglePlayLeaderBoards.onTouch(touch.x, touch.y)) { gameWorld.btnShowGooglePlayLeaderBoards.setPressed(true); }

                if(gameWorld.btnShowGooglePlayAchievments.onTouch(touch.x, touch.y)) { gameWorld.btnShowGooglePlayAchievments.setPressed(true); }

                if(gameWorld.btnHideGooglePlayScreen.onTouch(touch.x, touch.y)) { gameWorld.btnHideGooglePlayScreen.setPressed(true); }

                if(gameWorld.btnGooglePlaySignOut.onTouch(touch.x, touch.y) && IS_LOGGED_IN) { gameWorld.btnGooglePlaySignOut.setPressed(true); }
                break;
            case READY:
                if(gameWorld.statisticScreen.isShowing) {
                    if(gameWorld.statisticScreen.btnClose.onTouch(touch.x, touch.y)) { gameWorld.statisticScreen.btnClose.setPressed(true); }
                }

                if(gameWorld.isGameOver())
                {
                    if(btnAutoComplete.onTouch(touch.x, touch.y)) { btnAutoComplete.setPressed(true); }
                }

                if(MoveCards.size() == 0 && gameWorld.statisticScreen.isShowing == false) {
                    newGameTouchUp = false;

                    gameWorld.setDragging(false);
                    gameWorld.setDragAble(false);

                    gameWorld.setOriginalFingerX(-100);
                    gameWorld.setOriginalFingerY(-100);

                    if(btnAutoComplete.isPressed() || gameWorld.isNewGameAnimation() || (gameWorld.isAutoCompletingTriggered && !gameWorld.isAutoCompletingFinished())) {
                        // do nothin'
                    } else {
                        gameWorld.selectCardsDragging(touch.x, touch.y);
                    }
                }

                if(!menu.isShowing())
                {
                    if(btnArrowUp.onTouch(touch.x, touch.y) && !btnArrowUp.isPressed() && gameWorld.statisticScreen.isShowing == false)
                    {
                        btnArrowUp.setPressed(true);
                        btnArrowDown.setPressed(false);
                        menu.setScrolling(true, "up");
                        break;
                    }
                } else
                {
                    if(btnArrowDown.onTouch(touch.x, touch.y) && !btnArrowDown.isPressed() && !dialog.isShowing() && gameWorld.statisticScreen.isShowing == false)
                    {
                        btnArrowDown.setPressed(true);
                        btnArrowUp.setPressed(false);
                        menu.setScrolling(true, "down");
                        break;
                    }

                    if(dialog.isShowing()) { dialog.onTouch(touch.x, touch.y); }
                }

                if(gameWorld.exitDialog.isShowing()) { gameWorld.exitDialog.onTouch(touch.x, touch.y); }
                else {
                    if(menu.onTouchBtnUndo(touch.x, touch.y) && menu.isShowing() && gameWorld.isGameOver() == false) { gameWorld.undoMove(); break; }
                }

                break;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        camera.unproject(touch.set(screenX, screenY, 0));

        switch(gameWorld.getGameState())
        {
            case GOOGLE_PLAY_SCREEN:
                if(gameWorld.btnShowGooglePlayLeaderBoards.isPressed()) {
                    if(gameWorld.btnShowGooglePlayLeaderBoards.onTouch(touch.x, touch.y)) { gameWorld.setShowGoogleLeaderBoards(true); }
                    gameWorld.btnShowGooglePlayLeaderBoards.setPressed(false);
                }

                if(gameWorld.btnShowGooglePlayAchievments.isPressed()) {
                    if(gameWorld.btnShowGooglePlayAchievments.onTouch(touch.x, touch.y)) { gameWorld.setShowGoogleAchievments(true); }
                    gameWorld.btnShowGooglePlayAchievments.setPressed(false);
                }

                if(gameWorld.btnHideGooglePlayScreen.isPressed()) {
                    if(gameWorld.btnHideGooglePlayScreen.onTouch(touch.x, touch.y)) { gameWorld.setShowGooglePlayUIScreen(false); }
                    gameWorld.btnHideGooglePlayScreen.setPressed(false);
                }

                if(gameWorld.btnGooglePlaySignOut.isPressed()) {
                    if(gameWorld.btnGooglePlaySignOut.onTouch(touch.x, touch.y)) { gameWorld.setGooglePlaySignOut(true); }
                    gameWorld.btnGooglePlaySignOut.setPressed(false);
                }
                break;
            case READY:
                if(gameWorld.isGameOver())
                {
                    if(btnAutoComplete.onTouch(touch.x, touch.y) && btnAutoComplete.isPressed()) { gameWorld.isAutoCompletingTriggered = true; }

                    btnAutoComplete.setPressed(false);
                }

                if(menu.isShowing() && gameWorld.statisticScreen.isShowing == false)
                {
                    if(!dialog.isShowing())
                    {
                        if(menu.onTouchBtnPlay(touch.x, touch.y)) { dialog.show(); break; }
                        if(menu.onTouchBtnSettings(touch.x, touch.y)) {
                            gameWorld.setShowSettingsScreen(true);
                            Solitaire.actionResolver.ShowAdmobVideo();
                            break;
                        }
                        if(menu.onTouchBtnStatistics(touch.x, touch.y)) {
                            gameWorld.statisticScreen.show();
                            break; }
                        if(menu.onTouchBtnInfo(touch.x, touch.y)) {
                            gameWorld.setShowInfoScreen(true);

                            Solitaire.actionResolver.ShowAdmobVideo();
                            break; }
                        if(menu.onTouchBtnHint(touch.x, touch.y)) {

                            gameWorld.showPossibleMove();
                            //Solitaire.actionResolver.ShowAdmobVideo();
                            break;

                        }
                        if(menu.onTouchBtnGooglePlay(touch.x, touch.y)) {
                            Solitaire.actionResolver.ShowAdmobVideo();
                            gameWorld.setShowGooglePlayUIScreen(true);
                            break;
                        }
                    } else
                    {
                        if(dialog.getPressedButtonID().equals("NewGame_RandomDeal") && dialog.isStillOnTouch(dialog.getPressedButtonID(), touch.x, touch.y))
                        {
                            gameWorld.resetGame("Random");
                            newGameTouchUp = true;
                        } else if(dialog.getPressedButtonID().equals("NewGame_WinningDeal") && dialog.isStillOnTouch(dialog.getPressedButtonID(), touch.x, touch.y))
                        {

                            gameWorld.resetGame("Winning");
                            newGameTouchUp = true;
                        }  else if(dialog.getPressedButtonID().equals("Restart_Deal") && dialog.isStillOnTouch(dialog.getPressedButtonID(), touch.x, touch.y))
                        {
                            Solitaire.actionResolver.showInterstitial();
                            gameWorld.resetGame("Redeal");
                            newGameTouchUp = true;
                        } else if(dialog.getPressedButtonID().equals("NewGame_Cancel") && dialog.isStillOnTouch(dialog.getPressedButtonID(), touch.x, touch.y))
                        {
                            dialog.reset();
                            newGameTouchUp = true;
                        }

                        dialog.reset2();
                    }
                }

                if(gameWorld.exitDialog.isShowing()) {
                    if(gameWorld.exitDialog.getPressedButtonID().equals("No_Exit") && gameWorld.exitDialog.isStillOnTouch(gameWorld.exitDialog.getPressedButtonID(), touch.x, touch.y))
                    {
                        assets.IS_BACK_BUTTONPRESSED = false; gameWorld.exitDialog.reset();
                    } else if(gameWorld.exitDialog.getPressedButtonID().equals("Yes_Exit") && gameWorld.exitDialog.isStillOnTouch(gameWorld.exitDialog.getPressedButtonID(), touch.x, touch.y)) {
                        assets.IS_BACK_BUTTONPRESSED = false; gameWorld.exitDialog.reset(); Gdx.app.exit();
                    }

                    gameWorld.exitDialog.reset2();
                }

                if(!dialog.isShowing() && !newGameTouchUp && gameWorld.statisticScreen.isShowing == false)
                {
                    // StockStack
                    if(touch.x >= gameWorld.stockStackX && touch.x <= gameWorld.stockStackX + assets.CARDWIDTH && touch.y >= gameWorld.stockStackY && touch.y <= gameWorld.stockStackY + assets.CARDHEIGHT && MoveCards.size() == 0)
                    {
                        if(StockStack.size() > 0)
                        {
                            gameWorld.startFlipCard("StockStack", cards[StockStack.get(StockStack.size()-1)].getId());

                            break;
                        } else
                        {
                            if(StockStackRevealed.size() > 0)
                            {
                                gameWorld.start_RevealedToStockStack();

                                break;
                            }
                        }
                    }

                    if(btnAutoComplete.isPressed() || gameWorld.isNewGameAnimation() || (gameWorld.isAutoCompletingTriggered() && !gameWorld.isAutoCompletingFinished())) {
                        //do nothin'
                    } else {
                        if(!gameWorld.isDragging())
                        {
                            gameWorld.selectCards(touch.x, touch.y);
                            gameWorld.setCardsToMoveCards();
                            gameWorld.moveCards(touch.x, touch.y);

                            break;
                        } else {
                            gameWorld.moveCards(touch.x, touch.y);
                        }
                    }
                }

                if(gameWorld.statisticScreen.isShowing) {
                    if(gameWorld.statisticScreen.btnClose.isPressed()) {
                        if(gameWorld.statisticScreen.btnClose.onTouch(touch.x, touch.y)) { gameWorld.statisticScreen.hide(); }

                        gameWorld.statisticScreen.btnClose.setPressed(false);
                    }
                }

                break;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        switch(gameWorld.getGameState())
        {
            case READY:
                camera.unproject(touch.set(screenX, screenY, 0));

                if(!dialog.isShowing() && gameWorld.statisticScreen.isShowing == false)
                {
                    if(!gameWorld.isDragging() && gameWorld.isDragAble() && gameWorld.getOriginalFingerX() != -100 && gameWorld.getOriginalFingerY() != -100)
                    {
                        if(btnAutoComplete.isPressed() || gameWorld.isNewGameAnimation() || (gameWorld.isAutoCompletingTriggered() && !gameWorld.isAutoCompletingFinished()))  {
                            // do nothin'
                        } else {
                            if(touch.x - gameWorld.getOriginalFingerX() >= assets.CARDWIDTH*0.40f || gameWorld.getOriginalFingerX() - touch.x >= assets.CARDWIDTH*0.40f || touch.y - gameWorld.getOriginalFingerY() >= assets.CARDWIDTH*0.40f || gameWorld.getOriginalFingerY() - touch.y >= assets.CARDWIDTH*0.40f)
                            {
                                gameWorld.setDragging(true);

                                gameWorld.setCardsToMoveCards();

                                break;
                            }
                        }
                    }

                    if(btnAutoComplete.isPressed() || gameWorld.statisticScreen.isShowing || gameWorld.isNewGameAnimation() || (gameWorld.isAutoCompletingTriggered() && !gameWorld.isAutoCompletingFinished()))  {
                        // do nothin'
                    } else {
                        if(gameWorld.isDragging())
                        {
                            for(int i = 0;i < MoveCards.size();i++)
                            {
                                cards[MoveCards.get(i)].setX((touch.x + gameWorld.getDeltaX()));
                                cards[MoveCards.get(i)].setY((touch.y + gameWorld.getDeltaY() + i * assets.CARDHEIGHT * 0.35f));
                            }
                        }
                    }

                    break;
                }
        }

        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
