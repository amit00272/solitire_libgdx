package com.yourcompany.solitaire.GameWorld_And_Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.yourcompany.solitaire.GameObjects.AnimationCard;
import com.yourcompany.solitaire.GameObjects.Card;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.UI.ArrowButton;
import com.yourcompany.solitaire.UI.AutoCompleteButton;
import com.yourcompany.solitaire.UI.Dialog;
import com.yourcompany.solitaire.UI.Menu;

import java.util.ArrayList;

import static com.yourcompany.solitaire.Helper.Variables.*;

public class GameRenderer
{
    private CustomAssetManager assets;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    private GameWorld gameWorld;
    Card[] cards;
    ArrayList<Integer> TableStack1;
    ArrayList<Integer> TableStack2;
    ArrayList<Integer> TableStack3;
    ArrayList<Integer> TableStack4;
    ArrayList<Integer> TableStack5;
    ArrayList<Integer> TableStack6;
    ArrayList<Integer> TableStack7;

    ArrayList<Integer> FoundationStack1;
    ArrayList<Integer> FoundationStack2;
    ArrayList<Integer> FoundationStack3;
    ArrayList<Integer> FoundationStack4;

    ArrayList<Integer> StockStack;
    ArrayList<Integer> StockStackRevealed;
    ArrayList<Integer> MoveCards;

    AnimationCard[] AnimationCards;
    AnimationCard[] PossibleMoveCards;

    ArrowButton btnArrowUp;
    ArrowButton btnArrowDown;
    Menu menu;
    Dialog dialog;
    AutoCompleteButton btnAutoComplete;

    private boolean textPressAgainToExitHasShown;

    public GameRenderer(GameWorld gameWorld)
    {
        this.gameWorld = gameWorld;
        this.assets = gameWorld.assets;

        camera = new OrthographicCamera();
        camera.setToOrtho(true, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);

        batch = new SpriteBatch();
        batch.setProjectionMatrix(camera.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        initGameObjects();
    }

    private void initGameObjects()
    {
        cards = gameWorld.getCards();

        TableStack1 = gameWorld.getTableStack1();
        TableStack2 = gameWorld.getTableStack2();
        TableStack3 = gameWorld.getTableStack3();
        TableStack4 = gameWorld.getTableStack4();
        TableStack5 = gameWorld.getTableStack5();
        TableStack6 = gameWorld.getTableStack6();
        TableStack7 = gameWorld.getTableStack7();
        FoundationStack1 = gameWorld.getFoundationStack1();
        FoundationStack2 = gameWorld.getFoundationStack2();
        FoundationStack3 = gameWorld.getFoundationStack3();
        FoundationStack4 = gameWorld.getFoundationStack4();
        StockStack = gameWorld.getStockStack();
        StockStackRevealed = gameWorld.getStockStackRevealed();
        MoveCards = gameWorld.getMoveCards();
        AnimationCards = gameWorld.getAnimationCards();
        PossibleMoveCards = gameWorld.getPossibleMoveCards();
        btnArrowDown = gameWorld.getBtnArrowDown();
        btnArrowUp = gameWorld.getBtnArrowUp();
        menu = gameWorld.getMenu();
        dialog = gameWorld.getDialog();
        btnAutoComplete = gameWorld.getBtnAutoComplete();
    }

    public void render(float delta)
    {
        batch.begin();

        batch.disableBlending();

        batch.draw(assets.img_Background[assets.BACKGROUND_ID], 0, 0, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);

        batch.enableBlending();

        if(gameWorld.getGameState() != GameWorld.GameState.GOOGLE_PLAY_SCREEN) {
            if(StockStackRevealed.size() > 0)
            {
                batch.draw(assets.img_CardFoundationRefresh, gameWorld.stockStackX, gameWorld.stockStackY, assets.CARDWIDTH, assets.CARDHEIGHT);
            } else
            {
                batch.draw(assets.img_CardFreeCell, gameWorld.stockStackX, gameWorld.stockStackY, assets.CARDWIDTH, assets.CARDHEIGHT);
            }

            batch.draw(assets.img_CardFoundation, gameWorld.foundationStacksX[0], gameWorld.foundationStacksY[0], assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFoundation, gameWorld.foundationStacksX[1], gameWorld.foundationStacksY[1], assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFoundation, gameWorld.foundationStacksX[2], gameWorld.foundationStacksY[2], assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFoundation, gameWorld.foundationStacksX[3], gameWorld.foundationStacksY[3], assets.CARDWIDTH, assets.CARDHEIGHT);

            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[0], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[1], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[2], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[3], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[4], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[5], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);
            batch.draw(assets.img_CardFreeCell, gameWorld.tableStacksX[6], gameWorld.tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT);

            drawCards();

            drawText();

            if(!btnArrowUp.isPressed()) { btnArrowUp.draw(batch); }
            if(!btnArrowDown.isPressed()) { btnArrowDown.draw(batch); }

            if(gameWorld.isGameOverAnimation()) {
                gameWorld.labelGameOver1.draw(batch);
                gameWorld.labelGameOver2.draw(batch);
            }

            if(menu.isScrolling() || menu.isShowing())
            {
                menu.draw(batch);
            }

            if(dialog.isShowing())
            {
                dialog.draw(batch);
            }

            if(gameWorld.isGameOver())
            {
                if(gameWorld.isAutoCompletingTriggered == false) { btnAutoComplete.draw(batch); }
            }

            if(gameWorld.showNoMovesPossibleText == true) {
                gameWorld.textNoMovesPossible.draw(batch);

                if(gameWorld.textNoMovesPossible.getAlpha() > 0) {
                    gameWorld.textNoMovesPossible.setAlpha(gameWorld.textNoMovesPossible.getAlpha() - 1.5f * delta);
                    if(gameWorld.textNoMovesPossible.getAlpha() <= 0) { gameWorld.textNoMovesPossible.setAlpha(1); gameWorld.showNoMovesPossibleText = false; }
                }
            }

            if(assets.IS_BACK_BUTTONPRESSED) {
                if(assets.SHOW_EXIT_DIALOG == false) {
                    gameWorld.textPressAgainToExit.draw(batch);

                    if(textPressAgainToExitHasShown == false) {
                        if(gameWorld.textPressAgainToExit.getAlpha() < 1) {
                            gameWorld.textPressAgainToExit.setAlpha(gameWorld.textPressAgainToExit.getAlpha() + 1.5f * delta);
                            if(gameWorld.textPressAgainToExit.getAlpha() >= 1) { textPressAgainToExitHasShown = true; gameWorld.textPressAgainToExit.setAlpha(1f); }
                        }
                    } else {
                        if(gameWorld.textPressAgainToExit.getAlpha() > 0) {
                            gameWorld.textPressAgainToExit.setAlpha(gameWorld.textPressAgainToExit.getAlpha() - 1.5f * delta);
                            if(gameWorld.textPressAgainToExit.getAlpha() <= 0) { assets.IS_BACK_BUTTONPRESSED = false; textPressAgainToExitHasShown = false; gameWorld.textPressAgainToExit.setAlpha(0); }
                        }
                    }
                } else {
                    gameWorld.exitDialog.draw(batch);
                }
            }

            gameWorld.statisticScreen.draw(batch);

        } else {
            gameWorld.btnShowGooglePlayLeaderBoards.draw(batch);
            gameWorld.btnShowGooglePlayAchievments.draw(batch);
            if(IS_LOGGED_IN) { gameWorld.btnGooglePlaySignOut.draw(batch); }
            gameWorld.btnHideGooglePlayScreen.draw(batch);
        }

        batch.end();
    }

    private void drawText()
    {
        if(assets.ORIENTATION == 1) {
            if(assets.SHOW_SCORE) {
                assets.shadow.draw(batch, gameWorld.getStrScore(), 1, gameWorld.startY + 10);
                assets.font.draw(batch, gameWorld.getStrScore(), 0, gameWorld.startY + 9);
            }

            if(assets.SHOW_MOVES) {
                assets.shadow.draw(batch, gameWorld.getStrMoves(), assets.V_GAMEWIDTH - assets.glyphLayout_moves.width+1, gameWorld.startY + 10);
                assets.font.draw(batch, gameWorld.getStrMoves(), assets.V_GAMEWIDTH - assets.glyphLayout_moves.width, gameWorld.startY + 9);
            }

            if(assets.SHOW_TIME) {
                assets.shadow.draw(batch, gameWorld.getStrTime(), (assets.V_GAMEWIDTH - assets.glyphLayout_time.width) / 2 + 1, gameWorld.startY + 10);
                assets.font.draw(batch, gameWorld.getStrTime(), (assets.V_GAMEWIDTH - assets.glyphLayout_time.width) / 2, gameWorld.startY + 9);
            }
        } else {
            if(assets.SHOW_SCORE) {
                assets.shadow.draw(batch, gameWorld.getStrScore(), 2.5f, 10);
                assets.font.draw(batch, gameWorld.getStrScore(), 2, 10 - 0.5f);
            }

            if(assets.SHOW_MOVES) {
                assets.shadow.draw(batch, gameWorld.getStrMoves(), assets.V_GAMEWIDTH - assets.glyphLayout_moves.width+0.5f - 2, 10);
                assets.font.draw(batch, gameWorld.getStrMoves(), assets.V_GAMEWIDTH - assets.glyphLayout_moves.width - 2, 10 - 0.5f);
            }

            if(assets.SHOW_TIME) {
                assets.shadow.draw(batch, gameWorld.getStrTime(), (assets.V_GAMEWIDTH - assets.glyphLayout_time.width) / 2 + 0.5f, 10);
                assets.font.draw(batch, gameWorld.getStrTime(), (assets.V_GAMEWIDTH - assets.glyphLayout_time.width) / 2, 10 - 0.5f);
            }
        }
    }

    private void drawCards()
    {
        if(gameWorld.isNewGameAnimation()) {
            batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[StockStack.get(0)].getX(), cards[StockStack.get(0)].getY(), cards[StockStack.get(0)].getWidth(), cards[StockStack.get(0)].getHeight());

            for(int i = 0;i < 28;i++) {
                if(AnimationCards[i].isAnimationStarted()) {
                    if(AnimationCards[i].isRevealed()) {
                        batch.draw(assets.img_Cards.get(AnimationCards[i].getID() + (assets.FACE_CARD_ID*52)), AnimationCards[i].getX(), AnimationCards[i].getY(), AnimationCards[i].getWidth(), AnimationCards[i].getHeight());
                    } else {
                        batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], AnimationCards[i].getX(), AnimationCards[i].getY(), AnimationCards[i].getWidth(), AnimationCards[i].getHeight());
                    }
                }
            }
        } else {
            for(int i = 0;i < FoundationStack1.size();i++)
            {
                batch.draw(assets.img_Cards.get(FoundationStack1.get(i) + (assets.FACE_CARD_ID*52)), cards[FoundationStack1.get(i)].getX(), cards[FoundationStack1.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
            }

            for(int i = 0;i < FoundationStack2.size();i++)
            {
                batch.draw(assets.img_Cards.get(FoundationStack2.get(i) + (assets.FACE_CARD_ID*52)), cards[FoundationStack2.get(i)].getX(), cards[FoundationStack2.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
            }

            for(int i = 0;i < FoundationStack3.size();i++)
            {
                batch.draw(assets.img_Cards.get(FoundationStack3.get(i) + (assets.FACE_CARD_ID*52)), cards[FoundationStack3.get(i)].getX(), cards[FoundationStack3.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
            }

            for(int i = 0;i < FoundationStack4.size();i++)
            {
                batch.draw(assets.img_Cards.get(FoundationStack4.get(i) + (assets.FACE_CARD_ID*52)), cards[FoundationStack4.get(i)].getX(), cards[FoundationStack4.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
            }

            for(int i = 0;i < TableStack1.size();i++)
            {
                if(cards[TableStack1.get(i)].isRevealed())
                {
                    if(!cards[TableStack1.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack1.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack1.get(i)].getX(), cards[TableStack1.get(i)].getY(), cards[TableStack1.get(i)].getWidth(), cards[TableStack1.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack1.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack1.get(i)].getX()+cards[TableStack1.get(i)].getWidth(), cards[TableStack1.get(i)].getY(), - cards[TableStack1.get(i)].getWidth()/2, cards[TableStack1.get(i)].getHeight()/2, - cards[TableStack1.get(i)].getWidth(), cards[TableStack1.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack1.get(i)].getX(), cards[TableStack1.get(i)].getY(), cards[TableStack1.get(i)].getWidth(), cards[TableStack1.get(i)].getHeight());
                }
            }

            for(int i = 0;i < TableStack2.size();i++)
            {
                if(cards[TableStack2.get(i)].isRevealed())
                {
                    if(!cards[TableStack2.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack2.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack2.get(i)].getX(), cards[TableStack2.get(i)].getY(), cards[TableStack2.get(i)].getWidth(), cards[TableStack2.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack2.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack2.get(i)].getX()+cards[TableStack2.get(i)].getWidth(), cards[TableStack2.get(i)].getY(), - cards[TableStack2.get(i)].getWidth()/2, cards[TableStack2.get(i)].getHeight()/2, - cards[TableStack2.get(i)].getWidth(), cards[TableStack2.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack2.get(i)].getX(), cards[TableStack2.get(i)].getY(), cards[TableStack2.get(i)].getWidth(), cards[TableStack2.get(i)].getHeight());
                }
            }

            for(int i = 0;i < TableStack3.size();i++)
            {
                if(cards[TableStack3.get(i)].isRevealed())
                {
                    if(!cards[TableStack3.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack3.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack3.get(i)].getX(), cards[TableStack3.get(i)].getY(), cards[TableStack3.get(i)].getWidth(), cards[TableStack3.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack3.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack3.get(i)].getX()+cards[TableStack3.get(i)].getWidth(), cards[TableStack3.get(i)].getY(), - cards[TableStack3.get(i)].getWidth()/2, cards[TableStack3.get(i)].getHeight()/2, - cards[TableStack3.get(i)].getWidth(), cards[TableStack3.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack3.get(i)].getX(), cards[TableStack3.get(i)].getY(), cards[TableStack3.get(i)].getWidth(), cards[TableStack3.get(i)].getHeight());
                }
            }

            for(int i = 0;i < TableStack4.size();i++)
            {
                if(cards[TableStack4.get(i)].isRevealed())
                {
                    if(!cards[TableStack4.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack4.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack4.get(i)].getX(), cards[TableStack4.get(i)].getY(), cards[TableStack4.get(i)].getWidth(), cards[TableStack4.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack4.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack4.get(i)].getX()+cards[TableStack4.get(i)].getWidth(), cards[TableStack4.get(i)].getY(), - cards[TableStack4.get(i)].getWidth()/2, cards[TableStack4.get(i)].getHeight()/2, - cards[TableStack4.get(i)].getWidth(), cards[TableStack4.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack4.get(i)].getX(), cards[TableStack4.get(i)].getY(), cards[TableStack4.get(i)].getWidth(), cards[TableStack4.get(i)].getHeight());
                }
            }

            for(int i = 0;i < TableStack5.size();i++)
            {
                if(cards[TableStack5.get(i)].isRevealed())
                {
                    if(!cards[TableStack5.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack5.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack5.get(i)].getX(), cards[TableStack5.get(i)].getY(), cards[TableStack5.get(i)].getWidth(), cards[TableStack5.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack5.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack5.get(i)].getX()+cards[TableStack5.get(i)].getWidth(), cards[TableStack5.get(i)].getY(), - cards[TableStack5.get(i)].getWidth()/2, cards[TableStack5.get(i)].getHeight()/2, - cards[TableStack5.get(i)].getWidth(), cards[TableStack5.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack5.get(i)].getX(), cards[TableStack5.get(i)].getY(), cards[TableStack5.get(i)].getWidth(), cards[TableStack5.get(i)].getHeight());
                }
            }

            for(int i = 0;i < TableStack6.size();i++)
            {
                if(cards[TableStack6.get(i)].isRevealed())
                {
                    if(!cards[TableStack6.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack6.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack6.get(i)].getX(), cards[TableStack6.get(i)].getY(), cards[TableStack6.get(i)].getWidth(), cards[TableStack6.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack6.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack6.get(i)].getX()+cards[TableStack6.get(i)].getWidth(), cards[TableStack6.get(i)].getY(), - cards[TableStack6.get(i)].getWidth()/2, cards[TableStack6.get(i)].getHeight()/2, - cards[TableStack6.get(i)].getWidth(), cards[TableStack6.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack6.get(i)].getX(), cards[TableStack6.get(i)].getY(), cards[TableStack6.get(i)].getWidth(), cards[TableStack6.get(i)].getHeight());
                }
            }

            for(int i = 0;i < TableStack7.size();i++)
            {
                if(cards[TableStack7.get(i)].isRevealed())
                {
                    if(!cards[TableStack7.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(TableStack7.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack7.get(i)].getX(), cards[TableStack7.get(i)].getY(), cards[TableStack7.get(i)].getWidth(), cards[TableStack7.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(TableStack7.get(i) + (assets.FACE_CARD_ID*52)), cards[TableStack7.get(i)].getX()+cards[TableStack7.get(i)].getWidth(), cards[TableStack7.get(i)].getY(), - cards[TableStack7.get(i)].getWidth()/2, cards[TableStack7.get(i)].getHeight()/2, - cards[TableStack7.get(i)].getWidth(), cards[TableStack7.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[TableStack7.get(i)].getX(), cards[TableStack7.get(i)].getY(), cards[TableStack7.get(i)].getWidth(), cards[TableStack7.get(i)].getHeight());
                }
            }

            for(int i = 0;i < StockStack.size();i++)
            {
                if(cards[StockStack.get(i)].isRevealed())
                {
                    if(!cards[StockStack.get(i)].isFlipping())
                    {
                        batch.draw(assets.img_Cards.get(StockStack.get(i) + (assets.FACE_CARD_ID*52)), cards[StockStack.get(i)].getX(), cards[StockStack.get(i)].getY(), cards[StockStack.get(i)].getWidth(), cards[StockStack.get(i)].getHeight());
                    } else
                    {
                        batch.draw(assets.img_Cards.get(StockStack.get(i) + (assets.FACE_CARD_ID*52)), cards[StockStack.get(i)].getX()+cards[StockStack.get(i)].getWidth(), cards[StockStack.get(i)].getY(), - cards[StockStack.get(i)].getWidth()/2, cards[StockStack.get(i)].getHeight()/2, - cards[StockStack.get(i)].getWidth(), cards[StockStack.get(i)].getHeight(), 1, 1, 0);
                    }
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[StockStack.get(i)].getX(), cards[StockStack.get(i)].getY(), cards[StockStack.get(i)].getWidth(), cards[StockStack.get(i)].getHeight());
                }
            }

            for(int i = 0;i < StockStackRevealed.size();i++)
            {
                if(cards[StockStackRevealed.get(i)].isRevealed())
                {
                    batch.draw(assets.img_Cards.get(StockStackRevealed.get(i) + (assets.FACE_CARD_ID*52)), cards[StockStackRevealed.get(i)].getX(), cards[StockStackRevealed.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[StockStackRevealed.get(i)].getX(), cards[StockStackRevealed.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
                }
            }

            for(int i = 0;i < 32;i++) {
                if(PossibleMoveCards[i].isAnimationStarted() && PossibleMoveCards[i].isFree() == false) {
                    if(PossibleMoveCards[i].isRevealed()) {
                        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, PossibleMoveCards[i].getAlpha());
                        batch.draw(assets.img_Cards.get(PossibleMoveCards[i].getID() + (assets.FACE_CARD_ID*52)), PossibleMoveCards[i].getX(), PossibleMoveCards[i].getY(), PossibleMoveCards[i].getWidth(), PossibleMoveCards[i].getHeight());
                        batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1);
                    }
                }
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if(cards[MoveCards.get(i)].isRevealed())
                {
                    batch.draw(assets.img_Cards.get(MoveCards.get(i) + (assets.FACE_CARD_ID*52)), cards[MoveCards.get(i)].getX(), cards[MoveCards.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
                } else
                {
                    batch.draw(assets.img_CardBack[assets.BACK_CARD_ID], cards[MoveCards.get(i)].getX(), cards[MoveCards.get(i)].getY(), assets.CARDWIDTH, assets.CARDHEIGHT);
                }
            }
        }
    }

    public void changeOrientation(int newOrientation) {
        camera.setToOrtho(true, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);

        batch.setProjectionMatrix(camera.combined);
    }

    public OrthographicCamera getCamera() { return camera; }
}
