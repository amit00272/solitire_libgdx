package com.yourcompany.solitaire.GameWorld_And_Renderer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.yourcompany.solitaire.GameObjects.AnimationCard;
import com.yourcompany.solitaire.GameObjects.Card;
import com.yourcompany.solitaire.GameObjects.Move;
import com.yourcompany.solitaire.GameObjects.PossibleMove;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.Helper.Variables;
import com.yourcompany.solitaire.Screens.StatisticsScreen;
import com.yourcompany.solitaire.UI.ArrowButton;
import com.yourcompany.solitaire.UI.AutoCompleteButton;
import com.yourcompany.solitaire.UI.CenteredText;
import com.yourcompany.solitaire.UI.CustomButton;
import com.yourcompany.solitaire.UI.Dialog;
import com.yourcompany.solitaire.UI.FadeInText;
import com.yourcompany.solitaire.UI.Menu;
import com.yourcompany.solitaire.UI.SimpleButton;
import com.yourcompany.solitaire.UI.TextButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import static com.yourcompany.solitaire.Helper.Variables.*;

public class GameWorld
{
    public CustomAssetManager assets;

    public enum GameState
    {
        READY,
        FLIP_CARD,
        WAIT,
        MOVE_CARD,
        INVALID_MOVE_TAPPING,
        GOOGLE_PLAY_SCREEN
    }

    private GameState currentGameState;
    private GameState saveGameState;

    private Card[] cards = new Card[52];

    private ArrayList<Move> MovesList = new ArrayList<Move>();
    private ArrayList<PossibleMove> PossibleMovesList = new ArrayList<PossibleMove>();

    private ArrayList<Integer> TableStack1 = new ArrayList<Integer>();
    private ArrayList<Integer> TableStack2 = new ArrayList<Integer>();
    private ArrayList<Integer> TableStack3 = new ArrayList<Integer>();
    private ArrayList<Integer> TableStack4 = new ArrayList<Integer>();
    private ArrayList<Integer> TableStack5 = new ArrayList<Integer>();
    private ArrayList<Integer> TableStack6 = new ArrayList<Integer>();
    private ArrayList<Integer> TableStack7 = new ArrayList<Integer>();

    private ArrayList<Integer> FoundationStack1 = new ArrayList<Integer>();
    private ArrayList<Integer> FoundationStack2 = new ArrayList<Integer>();
    private ArrayList<Integer> FoundationStack3 = new ArrayList<Integer>();
    private ArrayList<Integer> FoundationStack4 = new ArrayList<Integer>();

    private ArrayList<Integer> StockStack = new ArrayList<Integer>();
    private ArrayList<Integer> StockStackRevealed = new ArrayList<Integer>();
    ArrayList<Integer> MoveCards = new ArrayList<Integer>();

    ArrayList<Integer> cardsAlreadyAssigned = new ArrayList<Integer>(); // Fuer die Initialisierung

    private AnimationCard[] AnimationCards = new AnimationCard[28];
    private AnimationCard[] PossibleMoveCards = new AnimationCard[32];

    private ArrowButton btnArrowUp;
    private ArrowButton btnArrowDown;
    private Menu menu;
    private Dialog dialog;
    private AutoCompleteButton btnAutoComplete;
    public CenteredText textPressAgainToExit;
    public CenteredText textNoMovesPossible;
    public Dialog exitDialog;

    public CustomButton btnShowGooglePlayLeaderBoards;
    public CustomButton btnShowGooglePlayAchievments;
    public TextButton btnGooglePlaySignOut;
    public TextButton btnHideGooglePlayScreen;

    public FadeInText labelGameOver1;
    public FadeInText labelGameOver2;

    public boolean showNoMovesPossibleText; // TODO added 16.06
    private int curMoveID = 0;

    public float stockStackX;
    public float stockStackY;
    float stockStackRevealedX;
    float stockStackRevealedY;
    float[] foundationStacksY = new float[4];
    float[] foundationStacksX = new float[4];
    float tableStacksY;
    float[] tableStacksX = new float[7];

    private int SourceStack;
    private int DestinationStack;
    private int StartingCardPosition;

    private int whichAction;
    private float whichActionGoalTime;
    private float whichActionTimePassed;
    private float time_waitTime;

    private float[] moveCards_xSpeed = new float[25];
    private float[] moveCards_ySpeed = new float[25];

    private float[] drawThreeXGoalPos = new float[25];

    private boolean isDragging;
    private boolean isDragAble;
    private float originalFingerX;
    private float originalFingerY;
    private float deltaX;
    private float deltaY;
    private int timer;

    private int score;
    private int moves;
    private float milliseconds;
    private int seconds;
    private int minutes;
    private String strScore = "";
    private String strMoves = "";
    private String strTime = "";

    private boolean isRandomDeal;
    private boolean isGameOver;
    private boolean allCardsDealtToFoundations;
    public boolean isAutoCompletingTriggered;
    private boolean isAutoCompletingFinished;
    private boolean isVegasScoringSet;

    private boolean showSettingsScreen;
    private boolean showInfoScreen;

    private boolean showGooglePlayLeaderBoards;
    private boolean showGooglePlayAchievments;
    private boolean GooglePlaySignOut;
    private boolean unlockAchievments = false;
    private boolean lastGameWasAWin = false;
    private boolean lastGameWasAWinSet = false;
    private int consecutiveGameCounter;

    private boolean isADCached = false;
    private boolean isNewGame = false;

    private boolean isNewGameAnimation;
    private boolean isGameOverAnimation;

    private boolean isFirstGame = true;

    public float startY = 0;

    private int completeTimeInSeconds = 0;

    public StatisticsScreen statisticScreen;

    public GameWorld(CustomAssetManager assets)
    {
        this.assets = assets;

        statisticScreen = new StatisticsScreen(assets);

        foundationStacksY[0] = (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksY[1] = (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksY[2] = (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksY[3] = (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksX[0] = 0;
        foundationStacksX[1] = 0 + assets.CARDWIDTH * 1;
        foundationStacksX[2] = 0 + assets.CARDWIDTH * 2;
        foundationStacksX[3] = 0 + assets.CARDWIDTH * 3;

        tableStacksY = (int) (0.5 * assets.CARDHEIGHT + assets.CARDHEIGHT * 1.5);
        tableStacksX[0] = 0;// + assets.CARD_SPACING_HORIZONTAL;
        tableStacksX[1] = 0 + assets.CARDWIDTH * 1 + assets.CARD_SPACING_HORIZONTAL * 1;
        tableStacksX[2] = 0 + assets.CARDWIDTH * 2 + assets.CARD_SPACING_HORIZONTAL * 2;
        tableStacksX[3] = 0 + assets.CARDWIDTH * 3 + assets.CARD_SPACING_HORIZONTAL * 3;
        tableStacksX[4] = 0 + assets.CARDWIDTH * 4 + assets.CARD_SPACING_HORIZONTAL * 4;
        tableStacksX[5] = 0 + assets.CARDWIDTH * 5 + assets.CARD_SPACING_HORIZONTAL * 5;
        tableStacksX[6] = 0 + assets.CARDWIDTH * 6 + assets.CARD_SPACING_HORIZONTAL * 6;

        stockStackX = tableStacksX[6];
        stockStackY = foundationStacksY[0];

        stockStackRevealedX = (int) (0 + tableStacksX[3] + 1.15 * assets.CARDWIDTH);
        stockStackRevealedY = foundationStacksY[0];

        for(int i = 0;i < 52;i++) { cards[i] = new Card(0, tableStacksY, assets.CARDWIDTH, assets.CARDHEIGHT, i); }

        for(int i = 0;i < 28;i++) { AnimationCards[i] = new AnimationCard(assets, assets.CARDWIDTH, assets.CARDHEIGHT); }
        for(int i = 0;i < 32;i++) { PossibleMoveCards[i] = new AnimationCard(assets, assets.CARDWIDTH, assets.CARDHEIGHT); }

        btnArrowDown = new ArrowButton(assets.UI_ArrowDown, (assets.V_GAMEWIDTH - 95) / 2, (assets.V_GAMEHEIGHT - 85), 95, 35);
        btnArrowDown.setPressed(true);
        btnArrowUp = new ArrowButton(assets.UI_ArrowUp, (assets.V_GAMEWIDTH - 95) / 2, (assets.V_GAMEHEIGHT - 50), 95, 35);

        menu = new Menu(assets);

        dialog = new Dialog(assets, "New Game", (assets.V_GAMEWIDTH-275)/2, 60, 275, 65);
        dialog.addButton("Random Deal", "NewGame_RandomDeal");
        dialog.addButton("Winning Deal", "NewGame_WinningDeal");
        dialog.addButton("Restart Deal", "Restart_Deal");
        dialog.addButton("Cancel", "NewGame_Cancel");

        btnAutoComplete = new AutoCompleteButton(assets);

        textPressAgainToExit = new CenteredText(assets, 0, assets.V_GAMEHEIGHT-100, assets.V_GAMEWIDTH, 50, assets.font);
        textPressAgainToExit.setAlpha(0);
        textPressAgainToExit.setText("Press again to exit...");

        textNoMovesPossible = new CenteredText(assets, 0, assets.V_GAMEHEIGHT-100, assets.V_GAMEWIDTH, 50, assets.font);
        textNoMovesPossible.setAlpha(0);
        textNoMovesPossible.setText("No moves detected...");

        exitDialog = new Dialog(assets, "Exit?", (assets.V_GAMEWIDTH-275)/2, (assets.V_GAMEHEIGHT-2*65) * 0.5f, 275, 65);
        exitDialog.addButton2("No", "No_Exit");
        exitDialog.addButton2("Yes", "Yes_Exit");

        btnShowGooglePlayLeaderBoards = new CustomButton(assets.UI_Leaderboards, (assets.V_GAMEWIDTH - 235) * 0.5f, (assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f - 10, 235, 60);
        btnShowGooglePlayAchievments = new CustomButton(assets.UI_Achievments, (assets.V_GAMEWIDTH - 235) * 0.5f, (assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f + 60 + 10, 235, 60);
        btnGooglePlaySignOut = new TextButton(assets, 0, assets.V_GAMEHEIGHT - 130, assets.V_GAMEWIDTH, 50, "Sign Out!");
        btnHideGooglePlayScreen = new TextButton(assets, 0, assets.V_GAMEHEIGHT - 130 + 50 + 10, assets.V_GAMEWIDTH, 50, "Back");

        labelGameOver1 = new FadeInText(assets, assets.gameOverLabelFont, -40, 0);//(0, (assets.assets.V_GAMEHEIGHT - 50*2) * 0.5f - 25, assets.assets.V_GAMEWIDTH, 50, assets.gameOverLabelFont);
        labelGameOver1.setText("Congratulations!");
        labelGameOver1.reset();
        labelGameOver2 = new FadeInText(assets, assets.gameOverLabelFont2, +40, 0.35f);//(0, (assets.assets.V_GAMEHEIGHT - 50*2) * 0.5f + 25, assets.assets.V_GAMEWIDTH, 50, assets.gameOverLabelFont);
        labelGameOver2.reset();
        labelGameOver2.setText("You Won!");

        consecutiveGameCounter = assets.getNumLongestStreak();
    }

    public void update(float delta)
    {
        if(currentGameState != GameState.GOOGLE_PLAY_SCREEN) {
            if(menu.isScrolling())
            {
                menu.update(delta);
            }

            if(isNewGameAnimation) {
                int animtionOverCounter = 0;

                for(int i = 0;i < 28;i++) {
                    AnimationCards[i].update(delta);

                    if(AnimationCards[i].isAnimationFinished()) { animtionOverCounter++; }
                }

                if(animtionOverCounter >= 28) { isNewGameAnimation = false; }
            }

            for(int i = 0;i < 32;i++) {
                PossibleMoveCards[i].update(delta);
            }

            if(isGameOverAnimation) {
                labelGameOver1.update(delta);
                labelGameOver2.update(delta);
            }

            countTime(delta);

            if(currentGameState == GameState.READY)
            {
                checkIfGameOver();

                if(isGameOver) {
                    if(isAutoCompletingTriggered == true) {
                        autoComplete();
                    }
                }
            } else if(currentGameState == GameState.FLIP_CARD)
            {
                switch(SourceStack)
                {
                    case 12:
                        // SourceStack

                        cards[StockStack.get(StockStack.size()-1)].setWidth(cards[StockStack.get(StockStack.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[StockStack.get(StockStack.size()-1)].setX(cards[StockStack.get(StockStack.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[StockStack.get(StockStack.size()-1)].getWidth() <= 0)
                        {
                            cards[StockStack.get(StockStack.size()-1)].setRevealed(true);
                            cards[StockStack.get(StockStack.size()-1)].setFlipping(true);
                            cards[StockStack.get(StockStack.size()-1)].setWidth(cards[StockStack.get(StockStack.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[StockStack.get(StockStack.size() - 1)].setX(cards[StockStack.get(StockStack.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[StockStack.get(StockStack.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[StockStack.get(StockStack.size()-1)].setX(stockStackX);
                                cards[StockStack.get(StockStack.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[StockStack.get(StockStack.size()-1)].setFlipping(false);

                                time_waitTime = 0;

                                currentGameState = GameState.WAIT;
                            }
                        }

                        break;
                    case 1:

                        cards[TableStack1.get(TableStack1.size()-1)].setWidth(cards[TableStack1.get(TableStack1.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack1.get(TableStack1.size()-1)].setX(cards[TableStack1.get(TableStack1.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack1.get(TableStack1.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack1.get(TableStack1.size()-1)].setRevealed(true);
                            cards[TableStack1.get(TableStack1.size()-1)].setFlipping(true);
                            cards[TableStack1.get(TableStack1.size()-1)].setWidth(cards[TableStack1.get(TableStack1.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack1.get(TableStack1.size() - 1)].setX(cards[TableStack1.get(TableStack1.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack1.get(TableStack1.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack1.get(TableStack1.size()-1)].setX(tableStacksX[0]);
                                cards[TableStack1.get(TableStack1.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack1.get(TableStack1.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                    case 2:

                        cards[TableStack2.get(TableStack2.size()-1)].setWidth(cards[TableStack2.get(TableStack2.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack2.get(TableStack2.size()-1)].setX(cards[TableStack2.get(TableStack2.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack2.get(TableStack2.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack2.get(TableStack2.size()-1)].setRevealed(true);
                            cards[TableStack2.get(TableStack2.size()-1)].setFlipping(true);
                            cards[TableStack2.get(TableStack2.size()-1)].setWidth(cards[TableStack2.get(TableStack2.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack2.get(TableStack2.size() - 1)].setX(cards[TableStack2.get(TableStack2.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack2.get(TableStack2.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack2.get(TableStack2.size()-1)].setX(tableStacksX[1]);
                                cards[TableStack2.get(TableStack2.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack2.get(TableStack2.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                    case 3:
                        cards[TableStack3.get(TableStack3.size()-1)].setWidth(cards[TableStack3.get(TableStack3.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack3.get(TableStack3.size()-1)].setX(cards[TableStack3.get(TableStack3.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack3.get(TableStack3.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack3.get(TableStack3.size()-1)].setRevealed(true);
                            cards[TableStack3.get(TableStack3.size()-1)].setFlipping(true);
                            cards[TableStack3.get(TableStack3.size()-1)].setWidth(cards[TableStack3.get(TableStack3.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack3.get(TableStack3.size() - 1)].setX(cards[TableStack3.get(TableStack3.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack3.get(TableStack3.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack3.get(TableStack3.size()-1)].setX(tableStacksX[2]);
                                cards[TableStack3.get(TableStack3.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack3.get(TableStack3.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                    case 4:
                        cards[TableStack4.get(TableStack4.size()-1)].setWidth(cards[TableStack4.get(TableStack4.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack4.get(TableStack4.size()-1)].setX(cards[TableStack4.get(TableStack4.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack4.get(TableStack4.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack4.get(TableStack4.size()-1)].setRevealed(true);
                            cards[TableStack4.get(TableStack4.size()-1)].setFlipping(true);
                            cards[TableStack4.get(TableStack4.size()-1)].setWidth(cards[TableStack4.get(TableStack4.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack4.get(TableStack4.size() - 1)].setX(cards[TableStack4.get(TableStack4.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack4.get(TableStack4.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack4.get(TableStack4.size()-1)].setX(tableStacksX[3]);
                                cards[TableStack4.get(TableStack4.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack4.get(TableStack4.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                    case 5:
                        cards[TableStack5.get(TableStack5.size()-1)].setWidth(cards[TableStack5.get(TableStack5.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack5.get(TableStack5.size()-1)].setX(cards[TableStack5.get(TableStack5.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack5.get(TableStack5.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack5.get(TableStack5.size()-1)].setRevealed(true);
                            cards[TableStack5.get(TableStack5.size()-1)].setFlipping(true);
                            cards[TableStack5.get(TableStack5.size()-1)].setWidth(cards[TableStack5.get(TableStack5.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack5.get(TableStack5.size() - 1)].setX(cards[TableStack5.get(TableStack5.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack5.get(TableStack5.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack5.get(TableStack5.size()-1)].setX(tableStacksX[4]);
                                cards[TableStack5.get(TableStack5.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack5.get(TableStack5.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                    case 6:
                        cards[TableStack6.get(TableStack6.size()-1)].setWidth(cards[TableStack6.get(TableStack6.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack6.get(TableStack6.size()-1)].setX(cards[TableStack6.get(TableStack6.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack6.get(TableStack6.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack6.get(TableStack6.size()-1)].setRevealed(true);
                            cards[TableStack6.get(TableStack6.size()-1)].setFlipping(true);
                            cards[TableStack6.get(TableStack6.size()-1)].setWidth(cards[TableStack6.get(TableStack6.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack6.get(TableStack6.size() - 1)].setX(cards[TableStack6.get(TableStack6.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack6.get(TableStack6.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack6.get(TableStack6.size()-1)].setX(tableStacksX[5]);
                                cards[TableStack6.get(TableStack6.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack6.get(TableStack6.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                    case 7:
                        cards[TableStack7.get(TableStack7.size()-1)].setWidth(cards[TableStack7.get(TableStack7.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                        cards[TableStack7.get(TableStack7.size()-1)].setX(cards[TableStack7.get(TableStack7.size() - 1)].getX() + (assets.FLIP_SPEED*0.5f) * delta);

                        if(cards[TableStack7.get(TableStack7.size()-1)].getWidth() <= 0)
                        {
                            cards[TableStack7.get(TableStack7.size()-1)].setRevealed(true);
                            cards[TableStack7.get(TableStack7.size()-1)].setFlipping(true);
                            cards[TableStack7.get(TableStack7.size()-1)].setWidth(cards[TableStack7.get(TableStack7.size() - 1)].getWidth() - assets.FLIP_SPEED * delta);
                            cards[TableStack7.get(TableStack7.size() - 1)].setX(cards[TableStack7.get(TableStack7.size() - 1)].getX() + (assets.FLIP_SPEED * 0.5f) * delta);

                            if(cards[TableStack7.get(TableStack7.size()-1)].getWidth() <= -assets.CARDWIDTH)
                            {
                                cards[TableStack7.get(TableStack7.size()-1)].setX(tableStacksX[6]);
                                cards[TableStack7.get(TableStack7.size()-1)].setWidth(assets.CARDWIDTH);
                                cards[TableStack7.get(TableStack7.size()-1)].setFlipping(false);

                                setMoveAbleCards();

                                currentGameState = GameState.READY;
                            }
                        }

                        break;
                }
            } else if(currentGameState == GameState.WAIT)
            {
                time_waitTime += delta;

                if(time_waitTime >= 0.12f)
                {
                    start_StockStackToRevealed();
                }
            } else if(currentGameState == GameState.MOVE_CARD)
            {
                //whichActionTimePassed++;

                if(delta >= .15f) { delta = .15f; }

                whichActionTimePassed += delta;

                for(int i = 0;i < MoveCards.size();i++)
                {
                    cards[MoveCards.get(i)].setX(cards[MoveCards.get(i)].getX() + moveCards_xSpeed[i] * delta);
                    cards[MoveCards.get(i)].setY(cards[MoveCards.get(i)].getY() + moveCards_ySpeed[i] * delta);
                }

                if(whichActionTimePassed >= whichActionGoalTime)
                {
                    if(whichAction == 2) { end_StockStackToRevealed(); }
                    if(whichAction == 3) { end_RevealedToStockStack(); }
                    if(whichAction == 4) { end_CardsToStack(); }
                    if(whichAction == 5) { end_InvalidMoveDragging(); }
                }
            } else if(currentGameState == GameState.INVALID_MOVE_TAPPING)
            {
                whichActionTimePassed++;

                if(whichActionTimePassed % 2 == 0)
                {
                    timer++;

                    for(int i = 0;i < MoveCards.size();i++)
                    {
                        if(timer % 2 == 0)
                        {
                            cards[MoveCards.get(i)].setX(cards[MoveCards.get(i)].getX() - moveCards_xSpeed[i]);
                        } else
                        {
                            cards[MoveCards.get(i)].setX(cards[MoveCards.get(i)].getX() + moveCards_xSpeed[i]);
                        }
                    }
                }

                if(whichActionTimePassed >= whichActionGoalTime)
                {
                    if(whichAction == 6) { end_InvalidMoveTapping(); }
                }
            }
        }
    }

    public void startFlipCard(String source, int card_ID)
    {
        if(source.equals("StockStack"))
        {
            SourceStack = 12;
        }

        if(assets.SOUND_ENABLED) { assets.sfx_FlipCard[rand(0, 2)].play(); }

        currentGameState = GameState.FLIP_CARD;
    }

    private void start_StockStackToRevealed()
    {
        addMove();  // TODO added 19.04

        if(assets.SOUND_ENABLED) { assets.sfx_moveCard[rand(0, 2)].play(); }

        isDragAble = false;
        isDragging = false;
        deltaX = 0;
        deltaY = 0;

        if(assets.DRAW_THREE) {
            int c = 0;
            int stockStackRevealedSize = StockStackRevealed.size();

            if(StockStack.size() >= 3) {
                if(StockStackRevealed.size() >= 3) {
                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 2));
                    StockStackRevealed.remove(StockStackRevealed.size() - 2);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;

                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;
                } else if(StockStackRevealed.size() == 2) {
                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;
                }

                cards[StockStack.get(StockStack.size()-1)].setRevealed(true);
                cards[StockStack.get(StockStack.size()-2)].setRevealed(true);
                cards[StockStack.get(StockStack.size()-3)].setRevealed(true);

                MoveCards.add(StockStack.get(StockStack.size() - 3));
                StockStack.remove(StockStack.size() - 3);
                MoveCards.add(StockStack.get(StockStack.size() - 2));
                StockStack.remove(StockStack.size() - 2);
                MoveCards.add(StockStack.get(StockStack.size() - 1));
                StockStack.remove(StockStack.size() - 1);

                moveCards_ySpeed[c] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_xSpeed[c] = ((stockStackX - stockStackRevealedX - 0 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                drawThreeXGoalPos[c] = stockStackRevealedX;

                c++;

                moveCards_ySpeed[c] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_xSpeed[c] = ((stockStackX - stockStackRevealedX - 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                drawThreeXGoalPos[c] = stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK;

                c++;

                moveCards_ySpeed[c] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_xSpeed[c] = ((stockStackX - stockStackRevealedX - 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                drawThreeXGoalPos[c] = stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK;
            } else if(StockStack.size() == 2) {
                if(StockStackRevealed.size() >= 3) {
                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 2));
                    StockStackRevealed.remove(StockStackRevealed.size() - 2);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;

                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;
                } else if(StockStackRevealed.size() == 2) {
                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;
                }

                cards[StockStack.get(StockStack.size()-1)].setRevealed(true);
                cards[StockStack.get(StockStack.size()-2)].setRevealed(true);

                MoveCards.add(StockStack.get(StockStack.size() - 2));
                StockStack.remove(StockStack.size() - 2);
                MoveCards.add(StockStack.get(StockStack.size() - 1));
                StockStack.remove(StockStack.size() - 1);

                int faktor = 0;
                if(stockStackRevealedSize >= 3) { faktor = 1; }
                else if(stockStackRevealedSize == 2) { faktor = 1; }

                moveCards_ySpeed[c] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_xSpeed[c] = ((stockStackX - stockStackRevealedX - faktor * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                drawThreeXGoalPos[c] = stockStackRevealedX + faktor * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK;

                c++;

                moveCards_ySpeed[c] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_xSpeed[c] = ((stockStackX - stockStackRevealedX - (faktor+1) * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                drawThreeXGoalPos[c] = stockStackRevealedX + (faktor+1) * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK;
            } else if(StockStack.size() == 1) {
                if(StockStackRevealed.size() >= 3) {
                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 2));
                    StockStackRevealed.remove(StockStackRevealed.size() - 2);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX;

                    c++;

                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                    moveCards_xSpeed[c] = (((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - (stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[c] = 0;
                    drawThreeXGoalPos[c] = stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK;

                    c++;
                }

                cards[StockStack.get(StockStack.size()-1)].setRevealed(true);

                MoveCards.add(StockStack.get(StockStack.size() - 1));
                StockStack.remove(StockStack.size() - 1);

                int faktor = 0;
                if(stockStackRevealedSize >= 3) { faktor = 2; }
                else if(stockStackRevealedSize == 2) { faktor = 1; }

                moveCards_ySpeed[c] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_xSpeed[c] = ((stockStackX - stockStackRevealedX - faktor * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                drawThreeXGoalPos[c] = stockStackRevealedX + faktor * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK;
            }
        } else {
            if (StockStackRevealed.size() >= 3)
            {
                int faktor = 0;

                if (StockStackRevealed.size() == 1) { faktor = 1; }
                else if (StockStackRevealed.size() == 2) { faktor = 2; }
                else if (StockStackRevealed.size() >= 3) { faktor = 2; }

                MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 2)); // TODO hier war 2

                moveCards_xSpeed[0] = (((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - stockStackRevealedX) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[0] = 0;

                MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));

                moveCards_xSpeed[1] = (((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) - (stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[1] = 0;

                MoveCards.add(StockStack.get(StockStack.size() - 1));

                moveCards_xSpeed[2] = ((stockStackX - stockStackRevealedX - faktor * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[2] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);

                StockStack.remove(StockStack.get(StockStack.size() - 1));
                StockStackRevealed.remove(StockStackRevealed.size() - 1);
                StockStackRevealed.remove(StockStackRevealed.size() - 1);
            } else
            {
                MoveCards.add(StockStack.get(StockStack.size() - 1));
                StockStack.remove(StockStack.size() - 1);

                int faktor = 0;

                if (StockStackRevealed.size() == 1) {
                    faktor = 1;
                } else if (StockStackRevealed.size() == 2) {
                    faktor = 2;
                } else if (StockStackRevealed.size() >= 3) {
                    faktor = 2;
                }

                moveCards_xSpeed[0] = ((stockStackX - stockStackRevealedX - faktor * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[0] = ((stockStackY - stockStackRevealedY) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        moves++;
        if(moves == 100) { assets.glyphLayout_moves.setText(assets.font, "Moves: 000"); }

        updateTxt();

        whichAction = 2;
        whichActionTimePassed = 0;
        whichActionGoalTime = assets.isActionGoalTimeMoveCards;

        currentGameState = GameState.MOVE_CARD;
    }

    private void end_StockStackToRevealed()
    {
        if(assets.DRAW_THREE == true) {
            for(int i = 0;i < MoveCards.size();i++) {
                cards[MoveCards.get(i)].setX(drawThreeXGoalPos[i]);
                cards[MoveCards.get(i)].setY(stockStackRevealedY);
                StockStackRevealed.add(MoveCards.get(i));
            }

            for(int i = MoveCards.size()-1;i >= 0;i--) {
                MoveCards.remove(i);
            }
        } else {
            if (MoveCards.size() >= 2) {
                StockStackRevealed.add(MoveCards.get(0));
                StockStackRevealed.add(MoveCards.get(1));
                if(MoveCards.size() >= 3) { StockStackRevealed.add(MoveCards.get(2)); }

                if(MoveCards.size() >= 3) {
                    cards[StockStackRevealed.get(StockStackRevealed.size() - 3)].setX(stockStackRevealedX);
                    cards[StockStackRevealed.get(StockStackRevealed.size() - 3)].setY(stockStackRevealedY);
                }

                cards[StockStackRevealed.get(StockStackRevealed.size() - 2)].setX((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK));
                cards[StockStackRevealed.get(StockStackRevealed.size() - 2)].setY(stockStackRevealedY);
                cards[StockStackRevealed.get(StockStackRevealed.size() - 1)].setX((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK));
                cards[StockStackRevealed.get(StockStackRevealed.size() - 1)].setY(stockStackRevealedY);

                if(MoveCards.size() >= 3) { MoveCards.remove(2); }
                MoveCards.remove(1);
                MoveCards.remove(0);
            } else
            {
                StockStackRevealed.add(MoveCards.get(0));
                if (StockStackRevealed.size() == 1) { cards[StockStackRevealed.get(StockStackRevealed.size() - 1)].setX(stockStackRevealedX); }
                else if (StockStackRevealed.size() == 2) { cards[StockStackRevealed.get(StockStackRevealed.size() - 1)].setX((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)); }
                else { cards[StockStackRevealed.get(StockStackRevealed.size() - 1)].setX((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)); }

                cards[StockStackRevealed.get(StockStackRevealed.size() - 1)].setY(stockStackRevealedY);

                MoveCards.remove(0);
            }
        }

        resetMoveSpeed();

        setMoveAbleCards();

        currentGameState = GameState.READY;
    }

    public void start_RevealedToStockStack()
    {
        addMove();  // TODO added 19.04

        if(assets.SOUND_ENABLED) { assets.sfx_moveCard[rand(0, 2)].play(); }

        isDragAble = false;
        isDragging = false;
        deltaX = 0;
        deltaY = 0;

        for(int i = 0;i < StockStackRevealed.size();i++)
        {
            moveCards_xSpeed[i] = ((cards[StockStackRevealed.get(i)].getX() - stockStackX) / assets.isActionGoalTimeRefillStockStack) * (-1);
            moveCards_ySpeed[i] = ((cards[StockStackRevealed.get(i)].getY() - stockStackY) / assets.isActionGoalTimeRefillStockStack) * (-1);
            cards[StockStackRevealed.get(i)].setRevealed(false);
            MoveCards.add(StockStackRevealed.get(i));
        }

        for(int i = StockStackRevealed.size()-1;i >= 0;i--)
        {
            StockStackRevealed.remove(i);
        }

        if(isVegasScoringSet == false) {
            score -= 100;

            if(score < 0 ) { score = 0; }
        }

        moves++;
        if(moves == 100) { assets.glyphLayout_moves.setText(assets.font, "Moves: 000"); }

        updateTxt();

        whichAction = 3;
        whichActionTimePassed = 0;
        whichActionGoalTime = assets.isActionGoalTimeRefillStockStack;

        currentGameState = GameState.MOVE_CARD;
    }

    public void end_RevealedToStockStack()
    {
        for(int i = MoveCards.size()-1;i >= 0;i--)
        {
            cards[MoveCards.get(i)].setX(stockStackX);
            cards[MoveCards.get(i)].setY(stockStackY);
            cards[MoveCards.get(i)].setRevealed(false);
            StockStack.add(MoveCards.get(i));
        }

        for(int i = MoveCards.size()-1;i >= 0;i--)
        {
            MoveCards.remove(i);
        }

        resetMoveSpeed();

        setMoveAbleCards();

        currentGameState = GameState.READY;
    }

    public void selectCardsDragging(float fingerX, float fingerY)
    {
        originalFingerX = fingerX;
        originalFingerY = fingerY;

        if(StockStackRevealed.size() >= 1)
        {
            if(cards[StockStackRevealed.get(StockStackRevealed.size()-1)].onTouch(fingerX, fingerY))
            {
                SourceStack = 13;

                deltaX = (cards[StockStackRevealed.get(StockStackRevealed.size()-1)].getX() - fingerX);
                deltaY = (cards[StockStackRevealed.get(StockStackRevealed.size()-1)].getY() - fingerY);
                isDragAble = true;
            }
        }

        for(int i = TableStack1.size()-1;i >= 0;i--)
        {
            if(cards[TableStack1.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 1;

                deltaX = (cards[TableStack1.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack1.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        for(int i = TableStack2.size()-1;i >= 0;i--)
        {
            if(cards[TableStack2.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 2;

                deltaX = (cards[TableStack2.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack2.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        for(int i = TableStack3.size()-1;i >= 0;i--)
        {
            if(cards[TableStack3.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 3;

                deltaX = (cards[TableStack3.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack3.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        for(int i = TableStack4.size()-1;i >= 0;i--)
        {
            if(cards[TableStack4.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 4;

                deltaX = (cards[TableStack4.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack4.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        for(int i = TableStack5.size()-1;i >= 0;i--)
        {
            if(cards[TableStack5.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 5;

                deltaX = (cards[TableStack5.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack5.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        for(int i = TableStack6.size()-1;i >= 0;i--)
        {
            if(cards[TableStack6.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 6;

                deltaX = (cards[TableStack6.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack6.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        for(int i = TableStack7.size()-1;i >= 0;i--)
        {
            if(cards[TableStack7.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 7;

                deltaX = (cards[TableStack7.get(i)].getX() - fingerX);
                deltaY = (cards[TableStack7.get(i)].getY() - fingerY);
                isDragAble = true;

                break;
            }
        }

        if(FoundationStack1.size() > 0) {
            for(int i = FoundationStack1.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack1.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 8;

                    deltaX = (cards[FoundationStack1.get(i)].getX() - fingerX);
                    deltaY = (cards[FoundationStack1.get(i)].getY() - fingerY);
                    isDragAble = true;

                    break;
                }
            }
        }

        if(FoundationStack2.size() > 0) {
            for(int i = FoundationStack2.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack2.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 9;

                    deltaX = (cards[FoundationStack2.get(i)].getX() - fingerX);
                    deltaY = (cards[FoundationStack2.get(i)].getY() - fingerY);
                    isDragAble = true;

                    break;
                }
            }
        }

        if(FoundationStack3.size() > 0) {
            for(int i = FoundationStack3.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack3.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 10;

                    deltaX = (cards[FoundationStack3.get(i)].getX() - fingerX);
                    deltaY = (cards[FoundationStack3.get(i)].getY() - fingerY);
                    isDragAble = true;

                    break;
                }
            }
        }

        if(FoundationStack4.size() > 0) {
            for(int i = FoundationStack4.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack4.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 11;

                    deltaX = (cards[FoundationStack4.get(i)].getX() - fingerX);
                    deltaY = (cards[FoundationStack4.get(i)].getY() - fingerY);
                    isDragAble = true;

                    break;
                }
            }
        }
    }

    public void selectCards(float fingerX, float fingerY)
    {
        SourceStack = -10;

        if(StockStackRevealed.size() >= 1)
        {
            if(cards[StockStackRevealed.get(StockStackRevealed.size()-1)].onTouch(fingerX, fingerY))
            {
                SourceStack = 13;
            }
        }

        for(int i = TableStack1.size()-1;i >= 0;i--)
        {
            if(cards[TableStack1.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 1;

                break;
            }
        }

        for(int i = TableStack2.size()-1;i >= 0;i--)
        {
            if(cards[TableStack2.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 2;

                break;
            }
        }

        for(int i = TableStack3.size()-1;i >= 0;i--)
        {
            if(cards[TableStack3.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 3;

                break;
            }
        }

        for(int i = TableStack4.size()-1;i >= 0;i--)
        {
            if(cards[TableStack4.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 4;

                break;
            }
        }

        for(int i = TableStack5.size()-1;i >= 0;i--)
        {
            if(cards[TableStack5.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 5;

                break;
            }
        }

        for(int i = TableStack6.size()-1;i >= 0;i--)
        {
            if(cards[TableStack6.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 6;

                break;
            }
        }

        for(int i = TableStack7.size()-1;i >= 0;i--)
        {
            if(cards[TableStack7.get(i)].onTouch(fingerX, fingerY))
            {
                StartingCardPosition = i;
                SourceStack = 7;

                break;
            }
        }

        if(FoundationStack1.size() > 0) {
            for(int i = FoundationStack1.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack1.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 8;

                    break;
                }
            }
        }

        if(FoundationStack2.size() > 0) {
            for(int i = FoundationStack2.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack2.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 9;

                    break;
                }
            }
        }

        if(FoundationStack3.size() > 0) {
            for(int i = FoundationStack3.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack3.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 10;

                    break;
                }
            }
        }

        if(FoundationStack4.size() > 0) {
            for(int i = FoundationStack4.size()-1;i >= 0;i--)
            {
                if(cards[FoundationStack4.get(i)].onTouch(fingerX, fingerY))
                {
                    StartingCardPosition = i;
                    SourceStack = 11;

                    break;
                }
            }
        }
    }

    public void setCardsToMoveCards()
    {
        if(SourceStack == 13 && StockStackRevealed.size() >= 1)
        {
            MoveCards.add(StockStackRevealed.get(StockStackRevealed.size()-1));
            StockStackRevealed.remove(StockStackRevealed.size()-1);
        }
        if(SourceStack == 1)
        {
            for(int i = StartingCardPosition;i < TableStack1.size();i++) { MoveCards.add(TableStack1.get(i)); }
            for(int i = TableStack1.size()-1;i >= StartingCardPosition;i--) { TableStack1.remove(i); }
        }
        if(SourceStack == 2)
        {
            for(int i = StartingCardPosition;i < TableStack2.size();i++) { MoveCards.add(TableStack2.get(i)); }
            for(int i = TableStack2.size()-1;i >= StartingCardPosition;i--) { TableStack2.remove(i); }
        }
        if(SourceStack == 3)
        {
            for(int i = StartingCardPosition;i < TableStack3.size();i++) { MoveCards.add(TableStack3.get(i)); }
            for(int i = TableStack3.size()-1;i >= StartingCardPosition;i--) { TableStack3.remove(i); }
        }
        if(SourceStack == 4)
        {
            for(int i = StartingCardPosition;i < TableStack4.size();i++) { MoveCards.add(TableStack4.get(i)); }
            for(int i = TableStack4.size()-1;i >= StartingCardPosition;i--) { TableStack4.remove(i); }
        }
        if(SourceStack == 5)
        {
            for(int i = StartingCardPosition;i < TableStack5.size();i++) { MoveCards.add(TableStack5.get(i)); }
            for(int i = TableStack5.size()-1;i >= StartingCardPosition;i--) { TableStack5.remove(i); }
        }
        if(SourceStack == 6)
        {
            for(int i = StartingCardPosition;i < TableStack6.size();i++) { MoveCards.add(TableStack6.get(i)); }
            for(int i = TableStack6.size()-1;i >= StartingCardPosition;i--) { TableStack6.remove(i); }
        }
        if(SourceStack == 7)
        {
            for(int i = StartingCardPosition;i < TableStack7.size();i++) { MoveCards.add(TableStack7.get(i)); }
            for(int i = TableStack7.size()-1;i >= StartingCardPosition;i--) { TableStack7.remove(i); }
        }
        if(SourceStack == 8 && FoundationStack1.size() >= 1)
        {
            MoveCards.add(FoundationStack1.get(FoundationStack1.size()-1));
            FoundationStack1.remove(FoundationStack1.size()-1);
        }
        if(SourceStack == 9 && FoundationStack2.size() >= 1)
        {
            MoveCards.add(FoundationStack2.get(FoundationStack2.size()-1));
            FoundationStack2.remove(FoundationStack2.size()-1);
        }
        if(SourceStack == 10 && FoundationStack3.size() >= 1)
        {
            MoveCards.add(FoundationStack3.get(FoundationStack3.size()-1));
            FoundationStack3.remove(FoundationStack3.size()-1);
        }
        if(SourceStack == 11 && FoundationStack4.size() >= 1)
        {
            MoveCards.add(FoundationStack4.get(FoundationStack4.size()-1));
            FoundationStack4.remove(FoundationStack4.size()-1);
        }
    }

    public void moveCards(float fingerX, float fingerY)
    {
        if(MoveCards.size() > 0)
        {
            boolean isMovePossible = false;
            int counter = 1;

            if(isDragging)
            {
                if(checkIfMovePossible(fingerX, fingerY, 0))
                {
                    isMovePossible = true;
                } else
                {
                    isMovePossible = false;
                }
            } else
            {
                for(int i = 0;i < 11;i++)
                {
                    int nextStack = SourceStack + (i+1);

                    if(isGameOver) { nextStack = 8 + i; }

                    if(nextStack > 11)
                    {
                        nextStack = counter;
                        counter++;
                    }

                    if(checkIfMovePossible(0, 0, nextStack)) { isMovePossible = true; break; }
                    else { isMovePossible = false; }
                }
            }

            if(isDragging)
            {
                if(isMovePossible)
                {
                    for(int i = 0;i < MoveCards.size();i++)
                    {
                        if(SourceStack == 1) { TableStack1.add(MoveCards.get(i)); }
                        if(SourceStack == 2) { TableStack2.add(MoveCards.get(i)); }
                        if(SourceStack == 3) { TableStack3.add(MoveCards.get(i)); }
                        if(SourceStack == 4) { TableStack4.add(MoveCards.get(i)); }
                        if(SourceStack == 5) { TableStack5.add(MoveCards.get(i)); }
                        if(SourceStack == 6) { TableStack6.add(MoveCards.get(i)); }
                        if(SourceStack == 7) { TableStack7.add(MoveCards.get(i)); }
                        if(SourceStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                        if(SourceStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                        if(SourceStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                        if(SourceStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
                        if(SourceStack == 13) { StockStackRevealed.add(MoveCards.get(i)); }
                    }

                    for(int i = MoveCards.size()-1;i >= 0;i--)
                    {
                        MoveCards.remove(i);
                    }
                }
            } else
            {
                if(isMovePossible)
                {
                    for(int i = 0;i < MoveCards.size();i++)
                    {
                        if(SourceStack == 1) { TableStack1.add(MoveCards.get(i)); }
                        if(SourceStack == 2) { TableStack2.add(MoveCards.get(i)); }
                        if(SourceStack == 3) { TableStack3.add(MoveCards.get(i)); }
                        if(SourceStack == 4) { TableStack4.add(MoveCards.get(i)); }
                        if(SourceStack == 5) { TableStack5.add(MoveCards.get(i)); }
                        if(SourceStack == 6) { TableStack6.add(MoveCards.get(i)); }
                        if(SourceStack == 7) { TableStack7.add(MoveCards.get(i)); }
                        if(SourceStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                        if(SourceStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                        if(SourceStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                        if(SourceStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
                        if(SourceStack == 13) { StockStackRevealed.add(MoveCards.get(i)); }
                    }

                    for(int i = MoveCards.size()-1;i >= 0;i--)
                    {
                        MoveCards.remove(i);
                    }
                }
            }

            if(isMovePossible)
            {
                start_CardsToStack();
            } else
            {
                if(isDragging)
                {
                    start_InvalidMoveDragging();
                } else
                {
                    start_InvalidMoveTapping();
                }
            }
        }
    }

    public void start_InvalidMoveDragging()
    {
        float von_X = 0;
        float zu_X = 0;
        float von_Y = 0;
        float zu_Y = 0;

        for(int i = 0;i < MoveCards.size();i++)
        {

            von_X = cards[MoveCards.get(i)].getX();
            von_Y = cards[MoveCards.get(i)].getY();

            if(SourceStack == 13)
            {
                if(StockStackRevealed.size() == 0) { zu_X = stockStackRevealedX; }
                else if(StockStackRevealed.size() == 1) { zu_X = (stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK); }
                else if(StockStackRevealed.size() >= 2) { zu_X = (stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK); }

                zu_Y = stockStackRevealedY;
            } else
            {
                if(SourceStack < 8)
                {
                    zu_X = tableStacksX[SourceStack-1];
                    zu_Y = 0;

                    if(SourceStack == 1) { zu_Y += getCardYAt(1) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[0]; }
                    if(SourceStack == 2) { zu_Y += getCardYAt(2) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[1]; }
                    if(SourceStack == 3) { zu_Y += getCardYAt(3) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[2]; }
                    if(SourceStack == 4) { zu_Y += getCardYAt(4) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[3]; }
                    if(SourceStack == 5) { zu_Y += getCardYAt(5) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[4]; }
                    if(SourceStack == 6) { zu_Y += getCardYAt(6) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[5]; }
                    if(SourceStack == 7) { zu_Y += getCardYAt(7) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[6]; }

                } else
                {
                    zu_X = foundationStacksX[SourceStack-8];
                    zu_Y = foundationStacksY[SourceStack-8];
                }
            }

            moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
            moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
        }

        whichAction = 5;
        whichActionTimePassed = 0;
        whichActionGoalTime = assets.isActionGoalTimeMoveCards;

        currentGameState = GameState.MOVE_CARD;
    }

    public void end_InvalidMoveDragging()
    {
        for(int i = 0;i < MoveCards.size();i++)
        {
            if(SourceStack == 13)
            {
                if(StockStackRevealed.size() == 0) { cards[MoveCards.get(i)].setX(stockStackRevealedX); cards[MoveCards.get(i)].setY(stockStackRevealedY); }
                if(StockStackRevealed.size() == 1) { cards[MoveCards.get(i)].setX((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)); cards[MoveCards.get(i)].setY(stockStackRevealedY); }
                if(StockStackRevealed.size() >= 2) { cards[MoveCards.get(i)].setX((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)); cards[MoveCards.get(i)].setY(stockStackRevealedY); }

                StockStackRevealed.add(MoveCards.get(i));
            }
            if(SourceStack == 1)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[0]);
                cards[MoveCards.get(i)].setY(getCardYAt(1));
                TableStack1.add(MoveCards.get(i));
            }
            if(SourceStack == 2)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[1]);
                cards[MoveCards.get(i)].setY(getCardYAt(2));
                TableStack2.add(MoveCards.get(i));
            }
            if(SourceStack == 3)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[2]);
                cards[MoveCards.get(i)].setY(getCardYAt(3));
                TableStack3.add(MoveCards.get(i));
            }
            if(SourceStack == 4)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[3]);
                cards[MoveCards.get(i)].setY(getCardYAt(4));
                TableStack4.add(MoveCards.get(i));
            }
            if(SourceStack == 5)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[4]);
                cards[MoveCards.get(i)].setY(getCardYAt(5));
                TableStack5.add(MoveCards.get(i));
            }
            if(SourceStack == 6)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[5]);
                cards[MoveCards.get(i)].setY(getCardYAt(6));
                TableStack6.add(MoveCards.get(i));
            }
            if(SourceStack == 7)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[6]);
                cards[MoveCards.get(i)].setY(getCardYAt(7));
                TableStack7.add(MoveCards.get(i));
            }
            if(SourceStack == 8)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[0]);
                cards[MoveCards.get(i)].setY(foundationStacksY[0]);
                FoundationStack1.add(MoveCards.get(i));
            }
            if(SourceStack == 9)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[1]);
                cards[MoveCards.get(i)].setY(foundationStacksY[1]);
                FoundationStack2.add(MoveCards.get(i));
            }
            if(SourceStack == 10)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[2]);
                cards[MoveCards.get(i)].setY(foundationStacksY[2]);
                FoundationStack3.add(MoveCards.get(i));
            }
            if(SourceStack == 11)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[3]);
                cards[MoveCards.get(i)].setY(foundationStacksY[3]);
                FoundationStack4.add(MoveCards.get(i));
            }
        }

        for(int i = MoveCards.size()-1;i >= 0;i--)
        {
            MoveCards.remove(i);
        }

        resetMoveSpeed();

        isDragAble = false;
        isDragging = false;
        deltaX = 0;
        deltaY = 0;

        currentGameState = GameState.READY;
    }

    public void start_InvalidMoveTapping()
    {
        for(int i = 0;i < MoveCards.size();i++)
        {
            moveCards_xSpeed[i] = assets.CARDWIDTH*0.15f;
        }

        whichAction = 6;
        whichActionTimePassed = 0;
        whichActionGoalTime = assets.isActionGoalTimeInvalidMoveTapping;

        isDragging = false;
        isDragAble = false;
        deltaX = 0;
        deltaY = 0;
        timer = 0;

        currentGameState = GameState.INVALID_MOVE_TAPPING;
    }

    public void end_InvalidMoveTapping()
    {
        for(int i = 0;i < MoveCards.size();i++)
        {
            if(SourceStack == 13)
            {
                if(StockStackRevealed.size() == 0) { cards[MoveCards.get(i)].setX(stockStackRevealedX); cards[MoveCards.get(i)].setY(stockStackRevealedY); }
                if(StockStackRevealed.size() == 1) { cards[MoveCards.get(i)].setX((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)); cards[MoveCards.get(i)].setY(stockStackRevealedY); }
                if(StockStackRevealed.size() >= 2) { cards[MoveCards.get(i)].setX((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK)); cards[MoveCards.get(i)].setY(stockStackRevealedY); }

                StockStackRevealed.add(MoveCards.get(i));
            }
            if(SourceStack == 1)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[0]);
                cards[MoveCards.get(i)].setY(getCardYAt(1));
                TableStack1.add(MoveCards.get(i));
            }
            if(SourceStack == 2)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[1]);
                cards[MoveCards.get(i)].setY(getCardYAt(2));
                TableStack2.add(MoveCards.get(i));
            }
            if(SourceStack == 3)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[2]);
                cards[MoveCards.get(i)].setY(getCardYAt(3));
                TableStack3.add(MoveCards.get(i));
            }
            if(SourceStack == 4)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[3]);
                cards[MoveCards.get(i)].setY(getCardYAt(4));
                TableStack4.add(MoveCards.get(i));
            }
            if(SourceStack == 5)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[4]);
                cards[MoveCards.get(i)].setY(getCardYAt(5));
                TableStack5.add(MoveCards.get(i));
            }
            if(SourceStack == 6)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[5]);
                cards[MoveCards.get(i)].setY(getCardYAt(6));
                TableStack6.add(MoveCards.get(i));
            }
            if(SourceStack == 7)
            {
                cards[MoveCards.get(i)].setX(tableStacksX[6]);
                cards[MoveCards.get(i)].setY(getCardYAt(7));
                TableStack7.add(MoveCards.get(i));
            }
            if(SourceStack == 8)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[0]);
                cards[MoveCards.get(i)].setY(foundationStacksY[0]);
                FoundationStack1.add(MoveCards.get(i));
            }
            if(SourceStack == 9)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[1]);
                cards[MoveCards.get(i)].setY(foundationStacksY[1]);
                FoundationStack2.add(MoveCards.get(i));
            }
            if(SourceStack == 10)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[2]);
                cards[MoveCards.get(i)].setY(foundationStacksY[2]);
                FoundationStack3.add(MoveCards.get(i));
            }
            if(SourceStack == 11)
            {
                cards[MoveCards.get(i)].setX(foundationStacksX[3]);
                cards[MoveCards.get(i)].setY(foundationStacksY[3]);
                FoundationStack4.add(MoveCards.get(i));
            }
        }

        for(int i = MoveCards.size()-1;i >= 0;i--)
        {
            MoveCards.remove(i);
        }

        resetMoveSpeed();

        isDragging = false;
        isDragAble = false;
        deltaX = 0;
        deltaY = 0;

        currentGameState = GameState.READY;
    }

    public boolean checkIfMovePossible(float fingerX, float fingerY, int stack)
    {
        DestinationStack = -10;

        if(!isDragging)
        {
            DestinationStack = stack;
        } else
        {
            if(fingerX >= tableStacksX[0] && fingerX < tableStacksX[0] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 1; }
            if(fingerX >= tableStacksX[1] && fingerX < tableStacksX[1] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 2; }
            if(fingerX >= tableStacksX[2] && fingerX < tableStacksX[2] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 3; }
            if(fingerX >= tableStacksX[3] && fingerX < tableStacksX[3] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 4; }
            if(fingerX >= tableStacksX[4] && fingerX < tableStacksX[4] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 5; }
            if(fingerX >= tableStacksX[5] && fingerX < tableStacksX[5] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 6; }
            if(fingerX >= tableStacksX[6] && fingerX < tableStacksX[6] + assets.CARDWIDTH && fingerY >= tableStacksY) { DestinationStack = 7; }
            if(fingerX >= foundationStacksX[0] && fingerX <= foundationStacksX[0] + assets.CARDWIDTH && fingerY >= foundationStacksY[0] && fingerY <= foundationStacksY[0] + assets.CARDHEIGHT) { DestinationStack = 8; }
            if(fingerX >= foundationStacksX[1] && fingerX <= foundationStacksX[1] + assets.CARDWIDTH && fingerY >= foundationStacksY[1] && fingerY <= foundationStacksY[1] + assets.CARDHEIGHT) { DestinationStack = 9; }
            if(fingerX >= foundationStacksX[2] && fingerX <= foundationStacksX[2] + assets.CARDWIDTH && fingerY >= foundationStacksY[2] && fingerY <= foundationStacksY[2] + assets.CARDHEIGHT) { DestinationStack= 10; }
            if(fingerX >= foundationStacksX[3] && fingerX <= foundationStacksX[3] + assets.CARDWIDTH && fingerY >= foundationStacksY[3] && fingerY <= foundationStacksY[3] + assets.CARDHEIGHT) { DestinationStack = 11; }
        }

        //Gdx.app.log("test2", "Source = " + SourceStack + ", Desti = " + DestinationStack + ", MoveCards.size = " + MoveCards.size());

        if(DestinationStack != -10 && SourceStack != -10)
        {
            if(DestinationStack == SourceStack) { return false; }

            if(DestinationStack == 1)
            {
                if(TableStack1.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack1.get(TableStack1.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack1.get(TableStack1.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 2)
            {
                if(TableStack2.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack2.get(TableStack2.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack2.get(TableStack2.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 3)
            {
                if(TableStack3.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack3.get(TableStack3.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack3.get(TableStack3.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 4)
            {
                if(TableStack4.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack4.get(TableStack4.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack4.get(TableStack4.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 5)
            {
                if(TableStack5.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack5.get(TableStack5.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack5.get(TableStack5.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 6)
            {
                if(TableStack6.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack6.get(TableStack6.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack6.get(TableStack6.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 7)
            {
                if(TableStack7.size() > 0)
                {
                    if(cards[MoveCards.get(0)].getValue() == cards[TableStack7.get(TableStack7.size()-1)].getValue()-1 && isColorOk(cards[MoveCards.get(0)].getColor(), cards[TableStack7.get(TableStack7.size()-1)].getColor())) { return true; }
                } else
                {
                    if(cards[MoveCards.get(0)].getValue() == 13) { return true; }
                }
            }
            if(DestinationStack == 8)
            {
                if(MoveCards.size() == 1)
                {
                    if(FoundationStack1.size() > 0)
                    {
                        if (cards[MoveCards.get(0)].getValue() == cards[FoundationStack1.get(FoundationStack1.size() - 1)].getValue() + 1 && cards[MoveCards.get(0)].getColor() == cards[FoundationStack1.get(FoundationStack1.size()-1)].getColor()) { return true; }
                    } else
                    {
                        if (cards[MoveCards.get(0)].getValue() == 1) { return true; }
                    }
                }
            }
            if(DestinationStack == 9)
            {
                if(MoveCards.size() == 1)
                {
                    if(FoundationStack2.size() > 0)
                    {
                        if (cards[MoveCards.get(0)].getValue() == cards[FoundationStack2.get(FoundationStack2.size() - 1)].getValue() + 1 && cards[MoveCards.get(0)].getColor() == cards[FoundationStack2.get(FoundationStack2.size()-1)].getColor()) { return true; }
                    } else{
                        if (cards[MoveCards.get(0)].getValue() == 1) { return true; }
                    }
                }
            }
            if(DestinationStack == 10)
            {
                if(MoveCards.size() == 1)
                {
                    if(FoundationStack3.size() > 0)
                    {
                        if (cards[MoveCards.get(0)].getValue() == cards[FoundationStack3.get(FoundationStack3.size() - 1)].getValue() + 1 && cards[MoveCards.get(0)].getColor() == cards[FoundationStack3.get(FoundationStack3.size()-1)].getColor()) { return true; }
                    } else
                    {
                        if (cards[MoveCards.get(0)].getValue() == 1) { return true; }
                    }
                }
            }
            if(DestinationStack == 11)
            {
                if(MoveCards.size() == 1)
                {
                    if(FoundationStack4.size() > 0)
                    {
                        if (cards[MoveCards.get(0)].getValue() == cards[FoundationStack4.get(FoundationStack4.size() - 1)].getValue() + 1 && cards[MoveCards.get(0)].getColor() == cards[FoundationStack4.get(FoundationStack4.size()-1)].getColor()) { return true; }
                    } else
                    {
                        if (cards[MoveCards.get(0)].getValue() == 1) { return true; }
                    }
                }
            }
        }

        return false;
    }

    public void showPossibleMove() {
        for(int i = 0;i < 32;i++) { PossibleMoveCards[i].setFree(true); }

        //Gdx.app.log("test", "called, size = " + PossibleMovesList.size());

        if(curMoveID < PossibleMovesList.size()-1) { curMoveID++; }
        else if(curMoveID >= PossibleMovesList.size()-1) { curMoveID = 0; }

        if(PossibleMovesList.size() > 0) {
            int rnd_move = curMoveID;//rand(0, PossibleMovesList.size()-1);

            float toX = 0;
            float toY = 0;
            boolean isRevealed = true;

            if(PossibleMovesList.get(rnd_move).getStackTo() < 8) {
                toX = tableStacksX[PossibleMovesList.get(rnd_move).getStackTo()-1];
            } else if(PossibleMovesList.get(rnd_move).getStackTo() >= 9 && PossibleMovesList.get(rnd_move).getStackTo() <= 11) {
                toX = foundationStacksX[PossibleMovesList.get(rnd_move).getStackTo() - 8];
            }

            if(PossibleMovesList.get(rnd_move).getStackTo() == 1) { if(TableStack1.size() > 0) { toY = cards[TableStack1.get(TableStack1.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[0] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 2) { if(TableStack2.size() > 0) { toY = cards[TableStack2.get(TableStack2.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[1] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 3) { if(TableStack3.size() > 0) { toY = cards[TableStack3.get(TableStack3.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[2] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 4) { if(TableStack4.size() > 0) { toY = cards[TableStack4.get(TableStack4.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[3] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 5) { if(TableStack5.size() > 0) { toY = cards[TableStack5.get(TableStack5.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[4] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 6) { if(TableStack6.size() > 0) { toY = cards[TableStack6.get(TableStack6.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[5] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 7) { if(TableStack7.size() > 0) { toY = cards[TableStack7.get(TableStack7.size()-1)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[6] * assets.CARDHEIGHT; } else { toY = tableStacksY; } }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 8) { toY = foundationStacksY[0]; }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 9) { toY = foundationStacksY[1]; }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 10) { toY = foundationStacksY[2]; }
            if(PossibleMovesList.get(rnd_move).getStackTo() == 11) { toY = foundationStacksY[3]; }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 1) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack1.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack1.get(i)].getId(), isRevealed, cards[TableStack1.get(i)].getX(), cards[TableStack1.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[0] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 2) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack2.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack2.get(i)].getId(), isRevealed, cards[TableStack2.get(i)].getX(), cards[TableStack2.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[1] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 3) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack3.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack3.get(i)].getId(), isRevealed, cards[TableStack3.get(i)].getX(), cards[TableStack3.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[2] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 4) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack4.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack4.get(i)].getId(), isRevealed, cards[TableStack4.get(i)].getX(), cards[TableStack4.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[3] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 5) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack5.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack5.get(i)].getId(), isRevealed, cards[TableStack5.get(i)].getX(), cards[TableStack5.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[4] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 6) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack6.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack6.get(i)].getId(), isRevealed, cards[TableStack6.get(i)].getX(), cards[TableStack6.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[5] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 7) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < TableStack7.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[TableStack7.get(i)].getId(), isRevealed, cards[TableStack7.get(i)].getX(), cards[TableStack7.get(i)].getY(), toX, toY, 0.05f);

                    toY += assets.CARD_SPACING_VERTICAL_REVEALED[6] * assets.CARDHEIGHT;
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 8) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < FoundationStack1.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[FoundationStack1.get(i)].getId(), isRevealed, cards[FoundationStack1.get(i)].getX(), cards[FoundationStack1.get(i)].getY(), toX, toY, 0.05f);
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 9) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < FoundationStack2.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[FoundationStack2.get(i)].getId(), isRevealed, cards[FoundationStack2.get(i)].getX(), cards[FoundationStack2.get(i)].getY(), toX, toY, 0.05f);
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 10) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < FoundationStack3.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[FoundationStack3.get(i)].getId(), isRevealed, cards[FoundationStack3.get(i)].getX(), cards[FoundationStack3.get(i)].getY(), toX, toY, 0.05f);
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 11) {
                for(int i = PossibleMovesList.get(rnd_move).getStartAtCard();i < FoundationStack4.size();i++) {
                    PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[FoundationStack4.get(i)].getId(), isRevealed, cards[FoundationStack4.get(i)].getX(), cards[FoundationStack4.get(i)].getY(), toX, toY, 0.05f);
                }
            }

            if(PossibleMovesList.get(rnd_move).getStackFrom() == 13) {
                int i = StockStackRevealed.size()-1;
                PossibleMoveCards[getFreePossibleMoveCard()].calculateSpeed(cards[StockStackRevealed.get(i)].getId(), isRevealed, cards[StockStackRevealed.get(i)].getX(), cards[StockStackRevealed.get(i)].getY(), toX, toY, 0.05f);
            }

            //Gdx.app.log("test", "possible, from = " + PossibleMovesList.get(rnd_move).getStackFrom() + ", to = " + PossibleMovesList.get(rnd_move).getStackTo());
        } else {
            //Gdx.app.log("test", "no Moves possible...");
            textNoMovesPossible.setAlpha(1);
            showNoMovesPossibleText = true;
        }
    }

    private int getFreePossibleMoveCard() {
        for(int i = 0;i < 32;i++) {
            if(PossibleMoveCards[i].isFree() == true) { return i; }
        }

        return 0;
    }

    private float getCardYAt(int stack) {

        if(stack == 1) {
            if(TableStack1.size() > 0) {
                if(cards[TableStack1.get(TableStack1.size()-1)].isRevealed() == false) {
                    return cards[TableStack1.get(TableStack1.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack1.get(TableStack1.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[0]*assets.CARDHEIGHT; }
            }
        }
        if(stack == 2) {
            if(TableStack2.size() > 0) {
                if(cards[TableStack2.get(TableStack2.size()-1)].isRevealed() == false) {
                    return cards[TableStack2.get(TableStack2.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack2.get(TableStack2.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[1]*assets.CARDHEIGHT; }
            }
        }
        if(stack == 3) {
            if(TableStack3.size() > 0) {
                if(cards[TableStack3.get(TableStack3.size()-1)].isRevealed() == false) {
                    return cards[TableStack3.get(TableStack3.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack3.get(TableStack3.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[2]*assets.CARDHEIGHT; }
            }
        }
        if(stack == 4) {
            if(TableStack4.size() > 0) {
                if(cards[TableStack4.get(TableStack4.size()-1)].isRevealed() == false) {
                    return cards[TableStack4.get(TableStack4.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack4.get(TableStack4.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[3]*assets.CARDHEIGHT; }
            }
        }
        if(stack == 5) {
            if(TableStack5.size() > 0) {
                if(cards[TableStack5.get(TableStack5.size()-1)].isRevealed() == false) {
                    return cards[TableStack5.get(TableStack5.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack5.get(TableStack5.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[4]*assets.CARDHEIGHT; }
            }
        }
        if(stack == 6) {
            if(TableStack6.size() > 0) {
                if(cards[TableStack6.get(TableStack6.size()-1)].isRevealed() == false) {
                    return cards[TableStack6.get(TableStack6.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack6.get(TableStack6.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[5]*assets.CARDHEIGHT; }
            }
        }
        if(stack == 7) {
            if(TableStack7.size() > 0) {
                if(cards[TableStack7.get(TableStack7.size()-1)].isRevealed() == false) {
                    return cards[TableStack7.get(TableStack7.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_UNREVEALED*assets.CARDHEIGHT;
                } else { return cards[TableStack7.get(TableStack7.size()-1)].getY()+assets.CARD_SPACING_VERTICAL_REVEALED[6]*assets.CARDHEIGHT; }
            }
        }

        return tableStacksY;
    }

    private void addMove() {
        // TODO added 19.04
        Move newMove = new Move();

        for(int i = 0;i < TableStack1.size();i++) { newMove.add((byte) (cards[TableStack1.get(i)].getId()), (byte) 1, cards[TableStack1.get(i)].isRevealed()); }
        for(int i = 0;i < TableStack2.size();i++) { newMove.add((byte) (cards[TableStack2.get(i)].getId()), (byte) 2, cards[TableStack2.get(i)].isRevealed()); }
        for(int i = 0;i < TableStack3.size();i++) { newMove.add((byte) (cards[TableStack3.get(i)].getId()), (byte) 3, cards[TableStack3.get(i)].isRevealed()); }
        for(int i = 0;i < TableStack4.size();i++) { newMove.add((byte) (cards[TableStack4.get(i)].getId()), (byte) 4, cards[TableStack4.get(i)].isRevealed()); }
        for(int i = 0;i < TableStack5.size();i++) { newMove.add((byte) (cards[TableStack5.get(i)].getId()), (byte) 5, cards[TableStack5.get(i)].isRevealed()); }
        for(int i = 0;i < TableStack6.size();i++) { newMove.add((byte) (cards[TableStack6.get(i)].getId()), (byte) 6, cards[TableStack6.get(i)].isRevealed()); }
        for(int i = 0;i < TableStack7.size();i++) { newMove.add((byte) (cards[TableStack7.get(i)].getId()), (byte) 7, cards[TableStack7.get(i)].isRevealed()); }
        for(int i = 0;i < FoundationStack1.size();i++) { newMove.add((byte) (cards[FoundationStack1.get(i)].getId()), (byte) 8, cards[FoundationStack1.get(i)].isRevealed()); }
        for(int i = 0;i < FoundationStack2.size();i++) { newMove.add((byte) (cards[FoundationStack2.get(i)].getId()), (byte) 9, cards[FoundationStack2.get(i)].isRevealed()); }
        for(int i = 0;i < FoundationStack3.size();i++) { newMove.add((byte) (cards[FoundationStack3.get(i)].getId()), (byte) 10, cards[FoundationStack3.get(i)].isRevealed()); }
        for(int i = 0;i < FoundationStack4.size();i++) { newMove.add((byte) (cards[FoundationStack4.get(i)].getId()), (byte) 11, cards[FoundationStack4.get(i)].isRevealed()); }
        for(int i = 0;i < StockStackRevealed.size();i++) { newMove.add((byte) (cards[StockStackRevealed.get(i)].getId()), (byte) 13, cards[StockStackRevealed.get(i)].isRevealed()); }
        for(int i = 0;i < StockStack.size();i++) { newMove.add((byte) (cards[StockStack.get(i)].getId()), (byte) 12, cards[StockStack.get(i)].isRevealed()); }

        MovesList.add(newMove);
    }

    public void undoMove() {
        if(MovesList.size() > 0) {
            for(int i = MoveCards.size()-1;i >= 0;i--) { MoveCards.remove(i); }

            int stockStackRevealedSize = StockStackRevealed.size();

            for(int i = TableStack1.size()-1;i >= 0;i--) { TableStack1.remove(i); }
            for(int i = TableStack2.size()-1;i >= 0;i--) { TableStack2.remove(i); }
            for(int i = TableStack3.size()-1;i >= 0;i--) { TableStack3.remove(i); }
            for(int i = TableStack4.size()-1;i >= 0;i--) { TableStack4.remove(i); }
            for(int i = TableStack5.size()-1;i >= 0;i--) { TableStack5.remove(i); }
            for(int i = TableStack6.size()-1;i >= 0;i--) { TableStack6.remove(i); }
            for(int i = TableStack7.size()-1;i >= 0;i--) { TableStack7.remove(i); }
            for(int i = FoundationStack1.size()-1;i >= 0;i--) { FoundationStack1.remove(i); }
            for(int i = FoundationStack2.size()-1;i >= 0;i--) { FoundationStack2.remove(i); }
            for(int i = FoundationStack3.size()-1;i >= 0;i--) { FoundationStack3.remove(i); }
            for(int i = FoundationStack4.size()-1;i >= 0;i--) { FoundationStack4.remove(i); }
            for(int i = StockStackRevealed.size()-1;i >= 0;i--) { StockStackRevealed.remove(i); }
            for(int i = StockStack.size()-1;i >= 0;i--) { StockStack.remove(i); }

            Move prevMove = MovesList.get(MovesList.size()-1);

            for(int i = 0;i < 52;i++) {

                MoveCards.add((int) prevMove.cardIDS[i]);

                cards[MoveCards.get(i)].setRevealed(prevMove.isRevealed(i));

                if(prevMove.stack[i] < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[prevMove.stack[i]-1]);
                    cards[MoveCards.get(i)].setY(tableStacksY + (getDestinationStackSize(prevMove.stack[i]) * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_STOCK));
                } else if(prevMove.stack[i] >= 8 && prevMove.stack[i] < 12) {
                    cards[MoveCards.get(i)].setX(foundationStacksX[prevMove.stack[i] - 8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[prevMove.stack[i] - 8]);
                } else if(prevMove.stack[i] == 13) {
                    cards[MoveCards.get(i)].setX(stockStackRevealedX);
                    cards[MoveCards.get(i)].setY(stockStackRevealedY);
                } else if(prevMove.stack[i] == 12) {
                    cards[MoveCards.get(i)].setX(stockStackX);
                    cards[MoveCards.get(i)].setY(stockStackY);

                    cards[MoveCards.get(i)].setRevealed(false);
                }

                if(prevMove.stack[i] == 1) { TableStack1.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 2) { TableStack2.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 3) { TableStack3.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 4) { TableStack4.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 5) { TableStack5.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 6) { TableStack6.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 7) { TableStack7.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 8) { FoundationStack1.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 9) { FoundationStack2.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 10) { FoundationStack3.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 11) { FoundationStack4.add((int) prevMove.cardIDS[i]); }
                if(prevMove.stack[i] == 13) { StockStackRevealed.add((int) prevMove.cardIDS[i]); }
                if (prevMove.stack[i] == 12) { StockStack.add((int) prevMove.cardIDS[i]); }
            }

            changeVerticalSpacing();

            for(int i = 52-1;i >= 0;i--) { MoveCards.remove(i); }

            if(StockStackRevealed.size() >= 3) {
                cards[StockStackRevealed.get(StockStackRevealed.size()-1)].setX(stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);
                cards[StockStackRevealed.get(StockStackRevealed.size()-2)].setX(stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);
            } else if(StockStackRevealed.size() == 2) { cards[StockStackRevealed.get(StockStackRevealed.size()-1)].setX(stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK); }

            MovesList.remove(MovesList.size() - 1);

            setMoveAbleCards();
        }
    }

    public void start_CardsToStack()
    {
        addMove();

        if(assets.SOUND_ENABLED) { assets.sfx_moveCard[rand(0, 2)].play(); }

        float von_X = 0;
        float zu_X = 0;

        float von_Y = 0;
        float zu_Y = 0;

        if(SourceStack == 13)
        {
            // From Revealed

            int id = 0;

            if(StockStackRevealed.size() >= 1)
            {
                // RevealedStack auch bewegen
                if(StockStackRevealed.size()-1 >= 3)
                {
                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 3));

                    von_X = cards[MoveCards.get(MoveCards.size()-1)].getX();
                    zu_X = (float) (stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);

                    moveCards_xSpeed[0] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[0]= 0;

                    MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 2));

                    von_X = cards[MoveCards.get(MoveCards.size()-1)].getX();
                    zu_X = (float) (stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);

                    moveCards_xSpeed[1] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                    moveCards_ySpeed[1]= 0;

                    id = 2;
                }

                MoveCards.add(StockStackRevealed.get(StockStackRevealed.size() - 1));

                if(id == 2) {
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                    StockStackRevealed.remove(StockStackRevealed.size() - 1);
                }

                StockStackRevealed.remove(StockStackRevealed.size()-1);

                von_X = cards[MoveCards.get(MoveCards.size()-1)].getX();
                von_Y = cards[MoveCards.get(MoveCards.size()-1)].getY();

                if(DestinationStack < 8)
                {
                    zu_X = tableStacksX[DestinationStack-1];
                    zu_Y = getCardYAt(DestinationStack);
                } else
                {
                    zu_X = foundationStacksX[DestinationStack-8];
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[id] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[id] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);

                //Gdx.app.log("", "MoveCardsSize bei StartCardsToStack = " + MoveCards.size());
            }
        }

        if(SourceStack == 1)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack1.size();i++)
            {
                MoveCards.add(TableStack1.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack1.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack1.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 2)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack2.size();i++)
            {
                MoveCards.add(TableStack2.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack2.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack2.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 3)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack3.size();i++)
            {
                MoveCards.add(TableStack3.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack3.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack3.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 4)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack4.size();i++)
            {
                MoveCards.add(TableStack4.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack4.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack4.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 5)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack5.size();i++)
            {
                MoveCards.add(TableStack5.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack5.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack5.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 6)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack6.size();i++)
            {
                MoveCards.add(TableStack6.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack6.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack6.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 7)
        {
            // Alle cards adden
            for(int i = StartingCardPosition;i < TableStack7.size();i++)
            {
                MoveCards.add(TableStack7.get(i));
            }
            // Cards von T1 entfernen
            for(int i = TableStack7.size()-1;i >= StartingCardPosition;i--)
            {
                TableStack7.remove(i);
            }

            for(int i = 0;i < MoveCards.size();i++)
            {
                if (DestinationStack < 8) {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = tableStacksX[DestinationStack - 1];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = getCardYAt(DestinationStack) + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[DestinationStack-1];
                } else
                {
                    von_X = cards[MoveCards.get(i)].getX();
                    zu_X = foundationStacksX[DestinationStack-8];
                    von_Y = cards[MoveCards.get(i)].getY();
                    zu_Y = foundationStacksY[DestinationStack-8];
                }

                moveCards_xSpeed[i] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
                moveCards_ySpeed[i] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
            }
        }

        if(SourceStack == 8)
        {
            MoveCards.add(FoundationStack1.get(FoundationStack1.size()-1));
            FoundationStack1.remove(FoundationStack1.size() - 1);

            von_X = cards[MoveCards.get(0)].getX();
            von_Y = cards[MoveCards.get(0)].getY();

            if(DestinationStack < 8)
            {
                zu_X = tableStacksX[DestinationStack-1];
                zu_Y = getCardYAt(DestinationStack);
            } else
            {
                zu_X = foundationStacksX[DestinationStack-8];
                zu_Y = foundationStacksY[DestinationStack-8];
            }

            moveCards_xSpeed[0] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
            moveCards_ySpeed[0] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
        }

        if(SourceStack == 9)
        {
            MoveCards.add(FoundationStack2.get(FoundationStack2.size()-1));
            FoundationStack2.remove(FoundationStack2.size() - 1);

            von_X = cards[MoveCards.get(0)].getX();
            von_Y = cards[MoveCards.get(0)].getY();

            if(DestinationStack < 8)
            {
                zu_X = tableStacksX[DestinationStack-1];
                zu_Y = getCardYAt(DestinationStack);
            } else
            {
                zu_X = foundationStacksX[DestinationStack-8];
                zu_Y = foundationStacksY[DestinationStack-8];
            }

            moveCards_xSpeed[0] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
            moveCards_ySpeed[0] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
        }

        if(SourceStack == 10)
        {
            MoveCards.add(FoundationStack3.get(FoundationStack3.size()-1));
            FoundationStack3.remove(FoundationStack3.size() - 1);

            von_X = cards[MoveCards.get(0)].getX();
            von_Y = cards[MoveCards.get(0)].getY();

            if(DestinationStack < 8)
            {
                zu_X = tableStacksX[DestinationStack-1];
                zu_Y = getCardYAt(DestinationStack);
            } else
            {
                zu_X = foundationStacksX[DestinationStack-8];
                zu_Y = foundationStacksY[DestinationStack-8];
            }

            moveCards_xSpeed[0] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
            moveCards_ySpeed[0] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
        }

        if(SourceStack == 11)
        {
            MoveCards.add(FoundationStack4.get(FoundationStack4.size()-1));
            FoundationStack4.remove(FoundationStack4.size() - 1);

            von_X = cards[MoveCards.get(0)].getX();
            von_Y = cards[MoveCards.get(0)].getY();

            if(DestinationStack < 8)
            {
                zu_X = tableStacksX[DestinationStack-1];
                zu_Y = getCardYAt(DestinationStack);
            } else
            {
                zu_X = foundationStacksX[DestinationStack-8];
                zu_Y = foundationStacksY[DestinationStack-8];
            }

            moveCards_xSpeed[0] = ((von_X-zu_X) / assets.isActionGoalTimeMoveCards) * (-1);
            moveCards_ySpeed[0] = ((von_Y-zu_Y) / assets.isActionGoalTimeMoveCards) * (-1);
        }

        if(isVegasScoringSet == false) {
            if(SourceStack == 13)
            {
                if(DestinationStack < 8) { score += 5; updateTxt(); }
                else { score += 10; updateTxt(); }
            }

            if(SourceStack < 8)
            {
                if(DestinationStack >= 8) { score += 10; updateTxt(); }
            }

            if(SourceStack >= 8 && SourceStack <= 11)
            {
                if(DestinationStack < 8) { score -= 15; updateTxt(); }
            }
        } else {
            if(SourceStack < 8) {
                if(DestinationStack >= 8) { score += 5; updateTxt(); }
            }

            if(SourceStack >= 8 && SourceStack <= 11)
            {
                if(DestinationStack < 8) { score -= 5; updateTxt(); }
            }
        }

        whichAction = 4;
        whichActionTimePassed = 0;
        whichActionGoalTime = assets.isActionGoalTimeMoveCards;



        currentGameState = GameState.MOVE_CARD;
    }

    public void end_CardsToStack()
    {
        boolean triggerFlipCardEvent = false;

        switch(SourceStack)
        {
            case 13:
                // Revealed StockStack
                int id = 0;

                if(MoveCards.size() >= 3) {
                    cards[MoveCards.get(MoveCards.size()-3)].setX((stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK));
                    cards[MoveCards.get(MoveCards.size()-2)].setX((stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK));
                    StockStackRevealed.add(MoveCards.get(MoveCards.size()-3));
                    StockStackRevealed.add(MoveCards.get(MoveCards.size()-2));
                    id = 2;
                }

                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(id)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(id)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(id)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(id)].setY(foundationStacksY[DestinationStack-8]);
                }

                if (DestinationStack == 1) { TableStack1.add(MoveCards.get(id)); }
                if (DestinationStack == 2) { TableStack2.add(MoveCards.get(id)); }
                if (DestinationStack == 3) { TableStack3.add(MoveCards.get(id)); }
                if (DestinationStack == 4) { TableStack4.add(MoveCards.get(id)); }
                if (DestinationStack == 5) { TableStack5.add(MoveCards.get(id)); }
                if (DestinationStack == 6) { TableStack6.add(MoveCards.get(id)); }
                if (DestinationStack == 7) { TableStack7.add(MoveCards.get(id)); }
                if (DestinationStack == 8) { FoundationStack1.add(MoveCards.get(id)); }
                if (DestinationStack == 9) { FoundationStack2.add(MoveCards.get(id)); }
                if (DestinationStack == 10) { FoundationStack3.add(MoveCards.get(id)); }
                if (DestinationStack == 11) { FoundationStack4.add(MoveCards.get(id)); }

                break;
        }

        if(SourceStack == 1)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack1.size() > 0)
            {
                if(cards[TableStack1.get(TableStack1.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 2)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack2.size() > 0)
            {
                if(cards[TableStack2.get(TableStack2.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 3)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack3.size() > 0)
            {
                if(cards[TableStack3.get(TableStack3.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 4)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack4.size() > 0)
            {
                if(cards[TableStack4.get(TableStack4.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 5)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack5.size() > 0)
            {
                if(cards[TableStack5.get(TableStack5.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 6)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack6.size() > 0)
            {
                if(cards[TableStack6.get(TableStack6.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 7)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }

            if(TableStack7.size() > 0)
            {
                if(cards[TableStack7.get(TableStack7.size()-1)].isRevealed() == false) { triggerFlipCardEvent = true; }
            }
        }

        if(SourceStack == 8)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }
        }

        if(SourceStack == 9)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }
        }

        if(SourceStack == 10)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 11) { FoundationStack4.add(MoveCards.get(i)); }
            }
        }

        if(SourceStack == 11)
        {
            for(int i = 0;i < MoveCards.size();i++)
            {
                if(DestinationStack < 8)
                {
                    cards[MoveCards.get(i)].setX(tableStacksX[DestinationStack-1]);
                    cards[MoveCards.get(i)].setY(getCardYAt(DestinationStack));
                } else
                {
                    cards[MoveCards.get(i)].setX(foundationStacksX[DestinationStack-8]);
                    cards[MoveCards.get(i)].setY(foundationStacksY[DestinationStack-8]);
                }

                if(DestinationStack == 1) { TableStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 2) { TableStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 3) { TableStack3.add(MoveCards.get(i)); }
                if(DestinationStack == 4) { TableStack4.add(MoveCards.get(i)); }
                if(DestinationStack == 5) { TableStack5.add(MoveCards.get(i)); }
                if(DestinationStack == 6) { TableStack6.add(MoveCards.get(i)); }
                if(DestinationStack == 7) { TableStack7.add(MoveCards.get(i)); }
                if(DestinationStack == 8) { FoundationStack1.add(MoveCards.get(i)); }
                if(DestinationStack == 9) { FoundationStack2.add(MoveCards.get(i)); }
                if(DestinationStack == 10) { FoundationStack3.add(MoveCards.get(i)); }
            }
        }

        for(int i = MoveCards.size()-1;i >= 0;i--)
        {
            MoveCards.remove(i);
        }

        resetMoveSpeed();

        changeVerticalSpacing();

        currentGameState = GameState.READY;

        if(triggerFlipCardEvent == true)
        {
            if(isVegasScoringSet == false) {
                score += 5;
            }

            whichActionTimePassed = 0;
            whichActionGoalTime = assets.FLIP_SPEED;
            whichAction = 0;

            if(assets.SOUND_ENABLED) { assets.sfx_FlipCard[rand(0, 2)].play(); }

            currentGameState = GameState.FLIP_CARD;
        } else {
            setMoveAbleCards();
        }

        moves++;
        if(moves == 100) { assets.glyphLayout_moves.setText(assets.font, "Moves: 000"); }

        updateTxt();

        isDragging = false;
        isDragAble = false;
        deltaX = 0;
        deltaY = 0;
    }

    public void checkIfGameOver()
    {
        if(!isGameOver)
        {
            int counter = 0;

            for(int i = 0;i < 52;i++)
            {
                if(cards[i].isRevealed())
                {
                    counter++;
                }
            }

            if(counter == 52 && StockStack.size() == 0 && StockStackRevealed.size() == 0 && MoveCards.size() == 0)
            {
                if(isRandomDeal) { save_this_game(); }

                isGameOver = true;
            }
        }
    }

    private void autoComplete() {
        if(isAutoCompletingFinished == false) {
            int nextStack = 0;
            int nextStackSize = 0;
            int lowestValue = 14;

            if(TableStack1.size() > 0) {
                if (cards[TableStack1.get(TableStack1.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack1.get(TableStack1.size() - 1)].getValue(); nextStackSize = TableStack1.size(); nextStack = 1; }
            }

            if(TableStack2.size() > 0) {
                if (cards[TableStack2.get(TableStack2.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack2.get(TableStack2.size() - 1)].getValue(); nextStackSize = TableStack2.size(); nextStack = 2; }
            }

            if(TableStack3.size() > 0) {
                if (cards[TableStack3.get(TableStack3.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack3.get(TableStack3.size() - 1)].getValue(); nextStackSize = TableStack3.size(); nextStack = 3; }
            }

            if(TableStack4.size() > 0) {
                if (cards[TableStack4.get(TableStack4.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack4.get(TableStack4.size() - 1)].getValue(); nextStackSize = TableStack4.size(); nextStack = 4; }
            }

            if(TableStack5.size() > 0) {
                if (cards[TableStack5.get(TableStack5.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack5.get(TableStack5.size() - 1)].getValue(); nextStackSize = TableStack5.size(); nextStack = 5; }
            }

            if(TableStack6.size() > 0) {
                if (cards[TableStack6.get(TableStack6.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack6.get(TableStack6.size() - 1)].getValue(); nextStackSize = TableStack6.size(); nextStack = 6; }
            }

            if(TableStack7.size() > 0) {
                if (cards[TableStack7.get(TableStack7.size() - 1)].getValue() < lowestValue) { lowestValue = cards[TableStack7.get(TableStack7.size() - 1)].getValue(); nextStackSize = TableStack7.size(); nextStack = 7; }
            }

            if(nextStack != 0) {
                selectCards(tableStacksX[nextStack-1] + 0.5f * assets.CARDWIDTH, tableStacksY + nextStackSize * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_REVEALED[nextStack] + 0.5f * assets.CARDHEIGHT);
                setCardsToMoveCards();
                moveCards(0, 0);
            }
        }

        if(FoundationStack1.size() >= 13 && FoundationStack2.size() >= 13 && FoundationStack3.size() >= 13 && FoundationStack4.size() >= 13) {
            isAutoCompletingFinished = true;
        }
    }

    private void save_this_game()
    {
    }

    private int getDestinationStackSize(int DestinationStack)
    {
        if(DestinationStack == 1) { return TableStack1.size(); }
        if(DestinationStack == 2) { return TableStack2.size(); }
        if(DestinationStack == 3) { return TableStack3.size(); }
        if(DestinationStack == 4) { return TableStack4.size(); }
        if(DestinationStack == 5) { return TableStack5.size(); }
        if(DestinationStack == 6) { return TableStack6.size(); }
        if(DestinationStack == 7) { return TableStack7.size(); }

        return 0;
    }

    private void resetMoveSpeed()
    {
        for(int i = 0;i < 25;i++)
        {
            moveCards_xSpeed[i] = 0;
            moveCards_ySpeed[i] = 0;
            drawThreeXGoalPos[i] = 0;
        }

        isDragAble = false;
        isDragging = false;
        deltaX = 0;
        deltaY = 0;
    }

    private void initiateCards_random()
    {
        // TableStack 1
        for(int i = 0;i < 1;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[0]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack1.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack1.get(TableStack1.size()-1)].setRevealed(true);

        // TableStack 2
        for(int i = 0;i < 2;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[1]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack2.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack2.get(TableStack2.size()-1)].setRevealed(true);

        // TableStack 3
        for(int i = 0;i < 3;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[2]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack3.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack3.get(TableStack3.size()-1)].setRevealed(true);

        // TableStack 4
        for(int i = 0;i < 4;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[3]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack4.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack4.get(TableStack4.size()-1)].setRevealed(true);

        // TableStack 5
        for(int i = 0;i < 5;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[4]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack5.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack5.get(TableStack5.size()-1)].setRevealed(true);

        // TableStack 6
        for(int i = 0;i < 6;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[5]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack6.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack6.get(TableStack6.size()-1)].setRevealed(true);

        // TableStack 7
        for(int i = 0;i < 7;i++)
        {
            boolean initTableStack_Done = false;

            while (initTableStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initTableStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(tableStacksX[6]);
                    cards[rnd_id].setY((tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    TableStack7.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }
        cards[TableStack7.get(TableStack7.size()-1)].setRevealed(true);

        // StockStack
        for(int i = 0;i < 24;i++)
        {
            boolean initStockStack_Done = false;

            while (initStockStack_Done == false)
            {
                int rnd_id = rand(0, 51);
                boolean rnd_id_AlreadyAssigned = false;

                for (int j = cardsAlreadyAssigned.size() - 1; j >= 0; j--)
                {
                    if (cardsAlreadyAssigned.get(j) == rnd_id)
                    {
                        // Karte ist bereits zugewiesen

                        rnd_id_AlreadyAssigned = true;

                        break;
                    }
                }

                if (rnd_id_AlreadyAssigned == false)
                {
                    // Karte ist noch nicht zugewiesen

                    initStockStack_Done = true;

                    cards[rnd_id].setId(rnd_id);
                    cards[rnd_id].setX(stockStackX);
                    cards[rnd_id].setY(stockStackY);
                    cards[rnd_id].setValue(getValue(rnd_id));
                    cards[rnd_id].setColor(getColor(rnd_id));
                    cards[rnd_id].setRevealed(false);
                    StockStack.add(rnd_id);
                    cardsAlreadyAssigned.add(rnd_id);
                }
            }
        }

        setupAnimationCards(0);

        isRandomDeal = true;
    }

    public void initiateCards_redeal()
    {
        int rnd_winID = rand(1, 300);

        int j = 0;
        int counter = 0;

        while (true)   {
            int id = cardsAlreadyAssigned.get(j);

            cards[id].setId(id);
            cards[id].setValue(getValue(id));
            cards[id].setColor(getColor(id));
            cards[id].setRevealed(false);

            if(j == 0)
            {
                TableStack1.add(cards[id].getId());
                cards[id].setX(tableStacksX[0]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                cards[id].setRevealed(true);
            }

            if(j >= 1 && j <= 2)
            {
                TableStack2.add(cards[id].getId());
                cards[id].setX(tableStacksX[1]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                counter++;

                if(j == 2) {
                    cards[id].setRevealed(true);
                    counter = 0;
                }
            }

            if(j >= 3 && j <= 5)
            {
                TableStack3.add(cards[id].getId());
                cards[id].setX(tableStacksX[2]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                counter++;

                if(j == 5) {
                    cards[id].setRevealed(true);
                    counter = 0;
                }
            }

            if(j >= 6 && j <= 9)
            {
                TableStack4.add(cards[id].getId());
                cards[id].setX(tableStacksX[3]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                counter++;

                if(j == 9) {
                    cards[id].setRevealed(true);
                    counter = 0;
                }
            }

            if(j >= 10 && j <= 14)
            {
                TableStack5.add(cards[id].getId());
                cards[id].setX(tableStacksX[4]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                counter++;

                if(j == 14) {
                    cards[id].setRevealed(true);
                    counter = 0;
                }
            }

            if(j >= 15 && j <= 20)
            {
                TableStack6.add(cards[id].getId());
                cards[id].setX(tableStacksX[5]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                counter++;

                if(j == 20) {
                    cards[id].setRevealed(true);
                    counter = 0;
                }
            }

            if(j >= 21 && j <= 27)
            {
                TableStack7.add(cards[id].getId());
                cards[id].setX(tableStacksX[6]);
                cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                counter++;

                if(j == 27) {
                    cards[id].setRevealed(true);
                    counter = 0;
                }
            }

            if(j >= 28)
            {
                StockStack.add(cards[id].getId());
                cards[id].setX(stockStackX);
                cards[id].setY(stockStackY);
            }

            j++;

            if(j == 52) { break; }
        }


        setupAnimationCards(0);

        isRandomDeal = false;
    }

    public void initiateCards_win()
    {
        int rnd_winID = rand(1, 300);

        int j = 0;
        int counter = 0;

        FileHandle file = Gdx.files.internal("data/winablegames/solitaire"+rnd_winID+".txt");
        BufferedReader br = new BufferedReader(file.reader());

        String strLine;

        //Read File Line By Line
        try {
            while ((strLine = br.readLine()) != null)   {
                // Print the content on the console
                int id = Integer.parseInt(strLine);

                //Log.i("", String.valueOf(id));

                cards[id].setId(id);
                cards[id].setValue(getValue(id));
                cards[id].setColor(getColor(id));
                cards[id].setRevealed(false);

                if(j == 0)
                {
                    TableStack1.add(cards[id].getId());
                    cards[id].setX(tableStacksX[0]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));
                    cards[id].setRevealed(true);
                }

                if(j >= 1 && j <= 2)
                {
                    TableStack2.add(cards[id].getId());
                    cards[id].setX(tableStacksX[1]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                    counter++;

                    if(j == 2) {
                        cards[id].setRevealed(true);
                        counter = 0;
                    }
                }

                if(j >= 3 && j <= 5)
                {
                    TableStack3.add(cards[id].getId());
                    cards[id].setX(tableStacksX[2]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                    counter++;

                    if(j == 5) {
                        cards[id].setRevealed(true);
                        counter = 0;
                    }
                }

                if(j >= 6 && j <= 9)
                {
                    TableStack4.add(cards[id].getId());
                    cards[id].setX(tableStacksX[3]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                    counter++;

                    if(j == 9) {
                        cards[id].setRevealed(true);
                        counter = 0;
                    }
                }

                if(j >= 10 && j <= 14)
                {
                    TableStack5.add(cards[id].getId());
                    cards[id].setX(tableStacksX[4]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                    counter++;

                    if(j == 14) {
                        cards[id].setRevealed(true);
                        counter = 0;
                    }
                }

                if(j >= 15 && j <= 20)
                {
                    TableStack6.add(cards[id].getId());
                    cards[id].setX(tableStacksX[5]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                    counter++;

                    if(j == 20) {
                        cards[id].setRevealed(true);
                        counter = 0;
                    }
                }

                if(j >= 21 && j <= 27)
                {
                    TableStack7.add(cards[id].getId());
                    cards[id].setX(tableStacksX[6]);
                    cards[id].setY((tableStacksY + counter * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED));

                    counter++;

                    if(j == 27) {
                        cards[id].setRevealed(true);
                        counter = 0;
                    }
                }

                if(j >= 28)
                {
                    StockStack.add(cards[id].getId());
                    cards[id].setX(stockStackX);
                    cards[id].setY(stockStackY);
                }

                cardsAlreadyAssigned.add(id);

                j++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Close the input stream
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        setupAnimationCards(0);

        isRandomDeal = false;
    }

    private void setupAnimationCards(int mode) {
        if(mode == 0) {
            // Initialising
            float x = stockStackX;
            float y = stockStackY;

            AnimationCards[0].calculateSpeed2(cards[TableStack1.get(0)].getId(), cards[TableStack1.get(0)].isRevealed(), x, y, cards[TableStack1.get(0)].getX(), cards[TableStack1.get(0)].getY(), 0);
            AnimationCards[1].calculateSpeed2(cards[TableStack2.get(0)].getId(), cards[TableStack2.get(0)].isRevealed(), x, y, cards[TableStack2.get(0)].getX(), cards[TableStack2.get(0)].getY(), 0.01f);
            AnimationCards[2].calculateSpeed2(cards[TableStack3.get(0)].getId(), cards[TableStack3.get(0)].isRevealed(), x, y, cards[TableStack3.get(0)].getX(), cards[TableStack3.get(0)].getY(), 0.02f);
            AnimationCards[3].calculateSpeed2(cards[TableStack2.get(1)].getId(), cards[TableStack2.get(1)].isRevealed(), x, y, cards[TableStack2.get(1)].getX(), cards[TableStack2.get(1)].getY(), 0.03f);

            AnimationCards[4].calculateSpeed2(cards[TableStack4.get(0)].getId(), cards[TableStack4.get(0)].isRevealed(), x, y, cards[TableStack4.get(0)].getX(), cards[TableStack4.get(0)].getY(), 0.04f);

            AnimationCards[5].calculateSpeed2(cards[TableStack3.get(1)].getId(), cards[TableStack3.get(1)].isRevealed(), x, y, cards[TableStack3.get(1)].getX(), cards[TableStack3.get(1)].getY(), 0.05f);

            AnimationCards[6].calculateSpeed2(cards[TableStack5.get(0)].getId(), cards[TableStack5.get(0)].isRevealed(), x, y, cards[TableStack5.get(0)].getX(), cards[TableStack5.get(0)].getY(), 0.06f);

            AnimationCards[7].calculateSpeed2(cards[TableStack4.get(1)].getId(), cards[TableStack4.get(1)].isRevealed(), x, y, cards[TableStack4.get(1)].getX(), cards[TableStack4.get(1)].getY(), 0.07f);

            AnimationCards[8].calculateSpeed2(cards[TableStack3.get(2)].getId(), cards[TableStack3.get(2)].isRevealed(), x, y, cards[TableStack3.get(2)].getX(), cards[TableStack3.get(2)].getY(), 0.08f);// jack


            AnimationCards[9].calculateSpeed2(cards[TableStack6.get(0)].getId(), cards[TableStack6.get(0)].isRevealed(), x, y, cards[TableStack6.get(0)].getX(), cards[TableStack6.get(0)].getY(), 0.09f);

            AnimationCards[10].calculateSpeed2(cards[TableStack5.get(1)].getId(), cards[TableStack5.get(1)].isRevealed(), x, y, cards[TableStack5.get(1)].getX(), cards[TableStack5.get(1)].getY(), 0.10f);

            AnimationCards[11].calculateSpeed2(cards[TableStack7.get(0)].getId(), cards[TableStack7.get(0)].isRevealed(), x, y, cards[TableStack7.get(0)].getX(), cards[TableStack7.get(0)].getY(), 0.11f);

            AnimationCards[12].calculateSpeed2(cards[TableStack4.get(2)].getId(), cards[TableStack4.get(2)].isRevealed(), x, y, cards[TableStack4.get(2)].getX(), cards[TableStack4.get(2)].getY(), 0.12f);

            AnimationCards[13].calculateSpeed2(cards[TableStack6.get(1)].getId(), cards[TableStack6.get(1)].isRevealed(), x, y, cards[TableStack6.get(1)].getX(), cards[TableStack6.get(1)].getY(), 0.13f);

            AnimationCards[14].calculateSpeed2(cards[TableStack5.get(2)].getId(), cards[TableStack5.get(2)].isRevealed(), x, y, cards[TableStack5.get(2)].getX(), cards[TableStack5.get(2)].getY(), 0.14f);

            AnimationCards[15].calculateSpeed2(cards[TableStack7.get(1)].getId(), cards[TableStack7.get(1)].isRevealed(), x, y, cards[TableStack7.get(1)].getX(), cards[TableStack7.get(1)].getY(), 0.15f);

            AnimationCards[16].calculateSpeed2(cards[TableStack4.get(3)].getId(), cards[TableStack4.get(3)].isRevealed(), x, y, cards[TableStack4.get(3)].getX(), cards[TableStack4.get(3)].getY(), 0.16f);

            AnimationCards[17].calculateSpeed2(cards[TableStack6.get(2)].getId(), cards[TableStack6.get(2)].isRevealed(), x, y, cards[TableStack6.get(2)].getX(), cards[TableStack6.get(2)].getY(), 0.17f);

            AnimationCards[18].calculateSpeed2(cards[TableStack5.get(3)].getId(), cards[TableStack5.get(3)].isRevealed(), x, y, cards[TableStack5.get(3)].getX(), cards[TableStack5.get(3)].getY(), 0.18f);

            AnimationCards[19].calculateSpeed2(cards[TableStack7.get(2)].getId(), cards[TableStack7.get(2)].isRevealed(), x, y, cards[TableStack7.get(2)].getX(), cards[TableStack7.get(2)].getY(), 0.19f);

            AnimationCards[20].calculateSpeed2(cards[TableStack6.get(3)].getId(), cards[TableStack6.get(3)].isRevealed(), x, y, cards[TableStack6.get(3)].getX(), cards[TableStack6.get(3)].getY(), 0.20f);

            AnimationCards[21].calculateSpeed2(cards[TableStack5.get(4)].getId(), cards[TableStack5.get(4)].isRevealed(), x, y, cards[TableStack5.get(4)].getX(), cards[TableStack5.get(4)].getY(), 0.21f);

            AnimationCards[22].calculateSpeed2(cards[TableStack7.get(3)].getId(), cards[TableStack7.get(3)].isRevealed(), x, y, cards[TableStack7.get(3)].getX(), cards[TableStack7.get(3)].getY(), 0.22f);

            AnimationCards[23].calculateSpeed2(cards[TableStack6.get(4)].getId(), cards[TableStack6.get(4)].isRevealed(), x, y, cards[TableStack6.get(4)].getX(), cards[TableStack6.get(4)].getY(), 0.23f);

            AnimationCards[24].calculateSpeed2(cards[TableStack7.get(4)].getId(), cards[TableStack7.get(4)].isRevealed(), x, y, cards[TableStack7.get(4)].getX(), cards[TableStack7.get(4)].getY(), 0.24f);

            AnimationCards[25].calculateSpeed2(cards[TableStack6.get(5)].getId(), cards[TableStack6.get(5)].isRevealed(), x, y, cards[TableStack6.get(5)].getX(), cards[TableStack6.get(5)].getY(), 0.25f);

            AnimationCards[26].calculateSpeed2(cards[TableStack7.get(5)].getId(), cards[TableStack7.get(5)].isRevealed(), x, y, cards[TableStack7.get(5)].getX(), cards[TableStack7.get(5)].getY(), 0.26f);

            AnimationCards[27].calculateSpeed2(cards[TableStack7.get(6)].getId(), cards[TableStack7.get(6)].isRevealed(), x, y, cards[TableStack7.get(6)].getX(), cards[TableStack7.get(6)].getY(), 0.27f);

            if(isFirstGame == false) { assets.sfx_cardBridge[rand(0, 1)].play(); }
            else {
                for(int i = 0;i < 28;i++) { AnimationCards[i].setAnimationFinished(true); }
            }
        }
    }

    private void countTime(float delta)
    {
        if(allCardsDealtToFoundations == false && isNewGameAnimation == false)
        {
            milliseconds += delta;

            if(milliseconds >= 1f) {
                milliseconds = 0;
                seconds++;
                completeTimeInSeconds++;
                updateTxtTime();

                if(seconds == 10 || seconds == 20 || seconds == 30 || seconds == 40 || seconds == 50 || seconds == 60) {
                    assets.setTotalTimeInSeconds(assets.getTotalTimeInSeconds() + 10);

                    statisticScreen.setTextTotalTime(assets.getTotalTimeInSeconds());
                }
            }

            if(seconds >= 60) { seconds = 0; minutes++; updateTxtTime();}

            if(FoundationStack1.size() >= 13 && FoundationStack2.size() >= 13 && FoundationStack3.size() >= 13 && FoundationStack4.size() >= 13) {
                allCardsDealtToFoundations = true;
            }
        }

        if(isGameOver && allCardsDealtToFoundations) {
            if(lastGameWasAWinSet == false) {
                lastGameWasAWinSet = true;

                consecutiveGameCounter++;

                if(consecutiveGameCounter >= assets.getNumLongestStreak()) { assets.increaseNumLongestStreak(); }

                assets.increaseNumWinsOverAll();

                unlockAchievments = true;

                if(score > assets.getBestScore()) { assets.setBestScore(score); }
                if(completeTimeInSeconds < assets.getBestTimeInSeconds()) { assets.setBestTimeInSeconds(completeTimeInSeconds); }

                assets.setTotalWins(assets.getTotalWins() + 1);
                assets.setTotalGames(assets.getTotalGames() + 1);

                statisticScreen.setTextBestScore(assets.getBestScore());
                statisticScreen.setTextBestTime(assets.getBestTimeInSeconds());
                statisticScreen.setTextTotalTime(assets.getTotalTimeInSeconds());
                statisticScreen.setStreakLenght(assets.getNumLongestStreak());
                statisticScreen.setTotalGames(assets.getTotalGames());
                statisticScreen.setGamesWon(assets.getTotalGames(), assets.getTotalWins());
            }

            isGameOverAnimation = true;
        }
    }

    private boolean helper() {
        int counter = 0;
        boolean isMovePossible = false;

        for(int i = 0;i < 11;i++)
        {
            int nextStack = SourceStack + (i+1);

            if(isGameOver) { nextStack = 8 + i; }

            if(nextStack > 11)
            {
                nextStack = counter;
                counter++;
            }

            if(checkIfMovePossible(0, 0, nextStack)) { isMovePossible = true; break; }
            else { isMovePossible = false; }
        }

        if(isMovePossible) {
            PossibleMove newPossibleMove = new PossibleMove((byte) (SourceStack), (byte) (DestinationStack), (byte) (StartingCardPosition));

            PossibleMovesList.add(newPossibleMove);
        }

        for(int i = 0;i < MoveCards.size();i++)
        {
            if(SourceStack == 1) { TableStack1.add(MoveCards.get(i)); }
            if(SourceStack == 2) { TableStack2.add(MoveCards.get(i)); }
            if(SourceStack == 3) { TableStack3.add(MoveCards.get(i)); }
            if(SourceStack == 4) { TableStack4.add(MoveCards.get(i)); }
            if(SourceStack == 5) { TableStack5.add(MoveCards.get(i)); }
            if(SourceStack == 6) { TableStack6.add(MoveCards.get(i)); }
            if(SourceStack == 7) { TableStack7.add(MoveCards.get(i)); }
            if(SourceStack == 9) { FoundationStack1.add(MoveCards.get(i)); }
            if(SourceStack == 10) { FoundationStack2.add(MoveCards.get(i)); }
            if(SourceStack == 11) { FoundationStack3.add(MoveCards.get(i)); }
            if(SourceStack == 12) { FoundationStack4.add(MoveCards.get(i)); }
            if(SourceStack == 13) { StockStackRevealed.add(MoveCards.get(i)); }
        }

        for(int i = MoveCards.size()-1;i >= 0;i--)
        {
            MoveCards.remove(i);
        }

        return isMovePossible;
    }

    private void setMoveAbleCards() {
        Gdx.app.log("test", "setMoveAbleCards() called!");

        for(int i = PossibleMovesList.size()-1;i >= 0;i--) { PossibleMovesList.remove(i); }

        for(int i = 0;i < 52;i++) { cards[i].setMoveAble(false); }

        if(TableStack1.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack1.size() >= 1) {
                for(int i = TableStack1.size()-1;i >= 0;i--) {
                    if(i == TableStack1.size()-1) {
                        selectCards(tableStacksX[0] + 0.5f * assets.CARDWIDTH, cards[TableStack1.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[0]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack1.size();j++) { cards[TableStack1.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack1.get(i)].getValue() && isColorOk(lastColor, cards[TableStack1.get(i)].getColor())) {
                            selectCards(tableStacksX[0] + 0.5f * assets.CARDWIDTH, cards[TableStack1.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[0]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack1.size();j++) { cards[TableStack1.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack1.get(i)].getColor();
                    lastValue = cards[TableStack1.get(i)].getValue();
                }
            }
        }

        if(TableStack2.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack2.size() >= 1) {
                for(int i = TableStack2.size()-1;i >= 0;i--) {
                    if(i == TableStack2.size()-1) {
                        selectCards(tableStacksX[1] + 0.5f * assets.CARDWIDTH, cards[TableStack2.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[1]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack2.size();j++) { cards[TableStack2.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack2.get(i)].getValue() && isColorOk(lastColor, cards[TableStack2.get(i)].getColor())) {
                            selectCards(tableStacksX[1] + 0.5f * assets.CARDWIDTH, cards[TableStack2.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[1]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack2.size();j++) { cards[TableStack2.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack2.get(i)].getColor();
                    lastValue = cards[TableStack2.get(i)].getValue();
                }
            }
        }

        if(TableStack3.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack3.size() >= 1) {
                for(int i = TableStack3.size()-1;i >= 0;i--) {
                    if(i == TableStack3.size()-1) {
                        selectCards(tableStacksX[2] + 0.5f * assets.CARDWIDTH, cards[TableStack3.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[2]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack3.size();j++) { cards[TableStack3.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack3.get(i)].getValue() && isColorOk(lastColor, cards[TableStack3.get(i)].getColor())) {
                            selectCards(tableStacksX[2] + 0.5f * assets.CARDWIDTH, cards[TableStack3.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[2]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack3.size();j++) { cards[TableStack3.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack3.get(i)].getColor();
                    lastValue = cards[TableStack3.get(i)].getValue();
                }
            }
        }

        if(TableStack4.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack4.size() >= 1) {
                for(int i = TableStack4.size()-1;i >= 0;i--) {
                    if(i == TableStack4.size()-1) {
                        selectCards(tableStacksX[3] + 0.5f * assets.CARDWIDTH, cards[TableStack4.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[3]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack4.size();j++) { cards[TableStack4.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack4.get(i)].getValue() && isColorOk(lastColor, cards[TableStack4.get(i)].getColor())) {
                            selectCards(tableStacksX[3] + 0.5f * assets.CARDWIDTH, cards[TableStack4.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[3]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack4.size();j++) { cards[TableStack4.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack4.get(i)].getColor();
                    lastValue = cards[TableStack4.get(i)].getValue();
                }
            }
        }

        if(TableStack5.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack5.size() >= 1) {
                for(int i = TableStack5.size()-1;i >= 0;i--) {
                    if(i == TableStack5.size()-1) {
                        selectCards(tableStacksX[4] + 0.5f * assets.CARDWIDTH, cards[TableStack5.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[4]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack5.size();j++) { cards[TableStack5.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack5.get(i)].getValue() && isColorOk(lastColor, cards[TableStack5.get(i)].getColor())) {
                            selectCards(tableStacksX[4] + 0.5f * assets.CARDWIDTH, cards[TableStack5.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[4]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack5.size();j++) { cards[TableStack5.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack5.get(i)].getColor();
                    lastValue = cards[TableStack5.get(i)].getValue();
                }
            }
        }

        if(TableStack6.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack6.size() >= 1) {
                for(int i = TableStack6.size()-1;i >= 0;i--) {
                    if(i == TableStack6.size()-1) {
                        selectCards(tableStacksX[5] + 0.5f * assets.CARDWIDTH, cards[TableStack6.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[5]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack6.size();j++) { cards[TableStack6.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack6.get(i)].getValue() && isColorOk(lastColor, cards[TableStack6.get(i)].getColor())) {
                            selectCards(tableStacksX[5] + 0.5f * assets.CARDWIDTH, cards[TableStack6.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[5]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack6.size();j++) { cards[TableStack6.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack6.get(i)].getColor();
                    lastValue = cards[TableStack6.get(i)].getValue();
                }
            }
        }

        if(TableStack7.size() > 0) {
            char lastColor = 't';
            int lastValue = -10;

            if(TableStack7.size() >= 1) {
                for(int i = TableStack7.size()-1;i >= 0;i--) {
                    if(i == TableStack7.size()-1) {
                        selectCards(tableStacksX[6] + 0.5f * assets.CARDWIDTH, cards[TableStack7.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[6]*0.5f);
                        setCardsToMoveCards();

                        if(helper() == true) { for(int j = i;j < TableStack7.size();j++) { cards[TableStack7.get(j)].setMoveAble(true); } }
                        //else { break; }
                    } else {
                        if((lastValue + 1) == cards[TableStack7.get(i)].getValue() && isColorOk(lastColor, cards[TableStack7.get(i)].getColor())) {
                            selectCards(tableStacksX[6] + 0.5f * assets.CARDWIDTH, cards[TableStack7.get(i)].getY() + assets.CARD_SPACING_VERTICAL_REVEALED[6]*0.5f);
                            setCardsToMoveCards();
                            if(helper() == true) { for(int j = i;j < TableStack7.size();j++) { cards[TableStack7.get(j)].setMoveAble(true); } }
                        } else { break; }
                    }

                    lastColor = cards[TableStack7.get(i)].getColor();
                    lastValue = cards[TableStack7.get(i)].getValue();
                }
            }
        }

        if(StockStackRevealed.size() > 0) {
            selectCards(cards[StockStackRevealed.get(StockStackRevealed.size()-1)].getX() + 0.5f * assets.CARDWIDTH, cards[StockStackRevealed.get(StockStackRevealed.size()-1)].getY() + assets.CARDHEIGHT*0.5f);
            setCardsToMoveCards();

            if(helper() == true) { cards[StockStackRevealed.get(StockStackRevealed.size()-1)].setMoveAble(true); }
            else { cards[StockStackRevealed.get(StockStackRevealed.size()-1)].setMoveAble(false); }
        }
    }

    public void resetGame(String gameType)
    {
        for(int i = TableStack1.size()-1;i >= 0;i--) { TableStack1.remove(i); }
        for(int i = TableStack2.size()-1;i >= 0;i--) { TableStack2.remove(i); }
        for(int i = TableStack3.size()-1;i >= 0;i--) { TableStack3.remove(i); }
        for(int i = TableStack4.size()-1;i >= 0;i--) { TableStack4.remove(i); }
        for(int i = TableStack5.size()-1;i >= 0;i--) { TableStack5.remove(i); }
        for(int i = TableStack6.size()-1;i >= 0;i--) { TableStack6.remove(i); }
        for(int i = TableStack7.size()-1;i >= 0;i--) { TableStack7.remove(i); }
        for(int i = FoundationStack1.size()-1;i >= 0;i--) { FoundationStack1.remove(i); }
        for(int i = FoundationStack2.size()-1;i >= 0;i--) { FoundationStack2.remove(i); }
        for(int i = FoundationStack3.size()-1;i >= 0;i--) { FoundationStack3.remove(i); }
        for(int i = FoundationStack4.size()-1;i >= 0;i--) { FoundationStack4.remove(i); }
        for(int i = StockStack.size()-1;i >= 0;i--) { StockStack.remove(i); }
        for(int i = StockStackRevealed.size()-1;i >= 0;i--) { StockStackRevealed.remove(i); }
        for(int i = MoveCards.size()-1;i >= 0;i--) { MoveCards.remove(i); }

        if(gameType.equals("Redeal") == false) {
            for(int i = cardsAlreadyAssigned.size()-1;i >= 0;i--) { cardsAlreadyAssigned.remove(i); }
        }

        for(int i = MovesList.size()-1;i >= 0;i--) { MovesList.remove(i); }
        for(int i = PossibleMovesList.size()-1;i >= 0;i--) { PossibleMovesList.remove(i); }

        resetMoveSpeed();

        if(!isGameOver && !allCardsDealtToFoundations && !isFirstGame && moves > 0 && gameType.equals("Redeal") == false) {
            consecutiveGameCounter = 0;
            //lastGameWasAWin = false;

            assets.resetNumLogestStreak();

            assets.setTotalGames(assets.getTotalGames() + 1);

            //Gdx.app.log("test", "resetNumLongestStreak Called");
        }

        completeTimeInSeconds = 0;
        score = 0;
        moves = 0;
        milliseconds = 0;
        seconds = 0;
        minutes = 0;
        isAutoCompletingTriggered = false;
        isAutoCompletingFinished = false;
        allCardsDealtToFoundations = false;
        isGameOver = false;
        isVegasScoringSet = false;

        GAME_COUNTER++;

        isNewGame = !isNewGame;

        if(assets.VEGAS_SCORING == true) {
            isVegasScoringSet = true;
            score = -52;
        }

        isNewGameAnimation = true;
        isGameOverAnimation = false;

        changeVerticalSpacing();
        updateTxt();
        updateTxtTime();

        if(gameType.equals("Random")) { initiateCards_random(); }
        else if(gameType.equals("Winning")){ initiateCards_win(); }
        else if(gameType.equals("Redeal")) {
            initiateCards_redeal();
        }

        labelGameOver1.reset();
        labelGameOver2.reset();
        dialog.reset();
        menu.reset();
        btnArrowDown.setPressed(true);
        btnArrowUp.setPressed(false);

        lastGameWasAWinSet = false;

        isFirstGame = false;

        //Gdx.app.log("test", "numLongestStreak = " + consecutiveGameCounter);

        statisticScreen.setTextBestScore(assets.getBestScore());
        statisticScreen.setTextBestTime(assets.getBestTimeInSeconds());
        statisticScreen.setTextTotalTime(assets.getTotalTimeInSeconds());
        statisticScreen.setStreakLenght(assets.getNumLongestStreak());
        statisticScreen.setTotalGames(assets.getTotalGames());
        statisticScreen.setGamesWon(assets.getTotalGames(), assets.getTotalWins());

        setMoveAbleCards();

        currentGameState = GameState.READY;
    }

    public void setUnlockAchievments(boolean bool) { unlockAchievments = bool; }

    public boolean isUnlockAchievments() { return unlockAchievments; }

    public int getConsecutiveGameCounter() {
        return assets.getNumLongestStreak(); }

    private int getValue(int id)
    {
        if(id <= 3) { return 1; }
        if(id >= 4 && id <= 7) { return 2; }
        if(id >= 8 && id <= 11) { return 3; }
        if(id >= 12 && id <= 15) { return 4; }
        if(id >= 16 && id <= 19) { return 5; }
        if(id >= 20 && id <= 23) { return 6; }
        if(id >= 24 && id <= 27) { return 7; }
        if(id >= 28 && id <= 31) { return 8; }
        if(id >= 32 && id <= 35) { return 9; }
        if(id >= 36 && id <= 39) { return 10; }
        if(id >= 40 && id <= 43) { return 11; }
        if(id >= 44 && id <= 47) { return 12; }
        if(id >= 48 && id <= 51) { return 13; }

        return 0;
    }

    private char getColor(int id)
    {
        if(id == 0 || id == 4 || id == 8 || id == 12 || id == 16 || id == 20 || id == 24 || id == 28 || id == 32 || id == 36 || id == 40 || id == 44 || id == 48)
        {
            return 'a';
        }

        if(id == 1 || id == 5 || id == 9 || id == 13 || id == 17 || id == 21 || id == 25 || id == 29 || id == 33 || id == 37 || id == 41 || id == 45 || id == 49)
        {
            return 'b';
        }

        if(id == 2 || id == 6 || id == 10 || id == 14 || id == 18 || id == 22 || id == 26 || id == 30 || id == 34 || id == 38 || id == 42 || id == 46 || id == 50)
        {
            return 'c';
        }

        if(id == 3 || id == 7 || id == 11 || id == 15 || id == 19 || id == 23 || id == 27 || id == 31 || id == 35 || id == 39 || id == 43 || id == 47 || id == 51)
        {
            return 'd';
        }

        return 'x';
    }

    private boolean isColorOk(char colorFrom, char colorTo)
    {
        if(colorFrom == 'a' && (colorTo == 'b' || colorTo == 'd')) { return true; }
        if(colorFrom == 'b' && (colorTo == 'a' || colorTo == 'c')) { return true; }
        if(colorFrom == 'c' && (colorTo == 'b' || colorTo == 'd')) { return  true; }
        if(colorFrom == 'd' && (colorTo == 'a' || colorTo == 'c')) { return  true; }

        return false;
    }

    private int rand(int a, int b)
    {
        double zahl = Math.random();
        zahl *= (b - a + 1);
        zahl += a;
        zahl = Math.floor(zahl);

        return (int) zahl;
    }

    public boolean isGameOver() { return isGameOver; }

    public boolean isAutoCompletingTriggered() { return isAutoCompletingTriggered; }

    public boolean isAutoCompletingFinished() { return isAutoCompletingFinished; }

    public boolean isDragging() { return isDragging; }

    public boolean isDragAble() { return isDragAble; }

    public void setDragging(boolean bool) { isDragging = bool; }

    public void setDragAble(boolean bool) { isDragAble = bool; }

    public float getDeltaX() { return deltaX; }

    public float getDeltaY() { return deltaY; }

    public void setOriginalFingerX(float val) { originalFingerX = val; }

    public void setOriginalFingerY(float val) { originalFingerY = val; }

    public float getOriginalFingerX() { return originalFingerX; }

    public float getOriginalFingerY() { return originalFingerY; }

    public int getMoves() { return moves; }

    public String getStrScore() { return strScore; }

    public String getStrMoves() { return strMoves; }

    public String getStrTime() { return strTime; }

    private void updateTxt()
    {
        strScore = "Score: " + score;
        strMoves = "Moves: " + moves;
    }

    private void updateTxtTime()
    {
        strTime = "Time: " + minutes;

        if(seconds < 10) { strTime += ":0" + seconds; }
        else { strTime += ":" + seconds; }

        //assets.glyphLayout_time.setText(assets.font, strTime);
    }

    private void changeVerticalSpacing() {
        if(assets.ORIENTATION == 2) {
            int max = 9;

            if(TableStack1.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[0] = 0.35f + (max - TableStack1.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[0] = 0.35f; }
            if(TableStack2.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[1] = 0.35f + (max - TableStack2.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[1] = 0.35f; }
            if(TableStack3.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[2] = 0.35f + (max - TableStack3.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[2] = 0.35f; }
            if(TableStack4.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[3] = 0.35f + (max - TableStack4.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[3] = 0.35f; }
            if(TableStack5.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[4] = 0.35f + (max - TableStack5.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[4] = 0.35f; }
            if(TableStack6.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[5] = 0.35f + (max - TableStack6.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[5] = 0.35f; }
            if(TableStack7.size() >= max) { assets.CARD_SPACING_VERTICAL_REVEALED[6] = 0.35f + (max - TableStack7.size()) * 0.015f; }
            else { assets.CARD_SPACING_VERTICAL_REVEALED[6] = 0.35f; }
        }

        setPos();
    }

    private void setPos() {
        float SPACING = 0;

        for(int i = 0;i < TableStack1.size();i++) {
            if(i > 0) {
                if(cards[TableStack1.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[0]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack1.get(i)].setY(cards[TableStack1.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }

        for(int i = 0;i < TableStack2.size();i++) {
            if(i > 0) {
                if(cards[TableStack2.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[1]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack2.get(i)].setY(cards[TableStack2.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }

        for(int i = 0;i < TableStack3.size();i++) {
            if(i > 0) {
                if(cards[TableStack3.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[2]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack3.get(i)].setY(cards[TableStack3.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }

        for(int i = 0;i < TableStack4.size();i++) {
            if(i > 0) {
                if(cards[TableStack4.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[3]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack4.get(i)].setY(cards[TableStack4.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }

        for(int i = 0;i < TableStack5.size();i++) {
            if(i > 0) {
                if(cards[TableStack5.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[4]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack5.get(i)].setY(cards[TableStack5.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }

        for(int i = 0;i < TableStack6.size();i++) {
            if(i > 0) {
                if(cards[TableStack6.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[5]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack6.get(i)].setY(cards[TableStack6.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }

        for(int i = 0;i < TableStack7.size();i++) {
            if(i > 0) {
                if(cards[TableStack7.get(i-1)].isRevealed()) { SPACING = assets.CARD_SPACING_VERTICAL_REVEALED[6]; }
                else { SPACING = assets.CARD_SPACING_VERTICAL_UNREVEALED; }
                cards[TableStack7.get(i)].setY(cards[TableStack7.get(i-1)].getY() + SPACING * assets.CARDHEIGHT);
            }
        }
    }

    public void changeOrientation(int newOrientation) {
        if (newOrientation == 1) {
            changeOrientation_Portrait();
        } else if (newOrientation == 2) {
            changeOrientation_LandScape();
        }

        for(int i = 0;i < TableStack1.size();i++) { cards[TableStack1.get(i)].setX(tableStacksX[0]); cards[TableStack1.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }
        for(int i = 0;i < TableStack2.size();i++) { cards[TableStack2.get(i)].setX(tableStacksX[1]); cards[TableStack2.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }
        for(int i = 0;i < TableStack3.size();i++) { cards[TableStack3.get(i)].setX(tableStacksX[2]); cards[TableStack3.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }
        for(int i = 0;i < TableStack4.size();i++) { cards[TableStack4.get(i)].setX(tableStacksX[3]); cards[TableStack4.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }
        for(int i = 0;i < TableStack5.size();i++) { cards[TableStack5.get(i)].setX(tableStacksX[4]); cards[TableStack5.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }
        for(int i = 0;i < TableStack6.size();i++) { cards[TableStack6.get(i)].setX(tableStacksX[5]); cards[TableStack6.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }
        for(int i = 0;i < TableStack7.size();i++) { cards[TableStack7.get(i)].setX(tableStacksX[6]); cards[TableStack7.get(i)].setY(tableStacksY + i * assets.CARDHEIGHT * assets.CARD_SPACING_VERTICAL_UNREVEALED); }

        for(int i = 0;i < FoundationStack1.size();i++) { cards[FoundationStack1.get(i)].setX(foundationStacksX[0]); cards[FoundationStack1.get(i)].setY(foundationStacksY[0]); }
        for(int i = 0;i < FoundationStack2.size();i++) { cards[FoundationStack2.get(i)].setX(foundationStacksX[1]); cards[FoundationStack2.get(i)].setY(foundationStacksY[1]); }
        for(int i = 0;i < FoundationStack3.size();i++) { cards[FoundationStack3.get(i)].setX(foundationStacksX[2]); cards[FoundationStack3.get(i)].setY(foundationStacksY[2]); }
        for(int i = 0;i < FoundationStack4.size();i++) { cards[FoundationStack4.get(i)].setX(foundationStacksX[3]); cards[FoundationStack4.get(i)].setY(foundationStacksY[3]); }

        for(int i = 0;i < StockStack.size();i++) { cards[StockStack.get(i)].setX(stockStackX); cards[StockStack.get(i)].setY(stockStackY); }
        for(int i = 0;i < StockStackRevealed.size();i++)
        {
            cards[StockStackRevealed.get(i)].setX(stockStackRevealedX);
            cards[StockStackRevealed.get(i)].setY(stockStackRevealedY);
        }

        if(StockStackRevealed.size() >= 3) {
            cards[StockStackRevealed.get(StockStackRevealed.size()-2)].setX(stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);
            cards[StockStackRevealed.get(StockStackRevealed.size()-1)].setX(stockStackRevealedX + 2 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);
        } else if(StockStackRevealed.size() == 2) {
            cards[StockStackRevealed.get(StockStackRevealed.size()-1)].setX(stockStackRevealedX + 1 * assets.CARDWIDTH * assets.CARD_SPACING_VERTICAL_STOCK);
        }

        for(int i = 0;i < 28;i++) { AnimationCards[i].setWidth(assets.CARDWIDTH); AnimationCards[i].setHeight(assets.CARDHEIGHT); }
        for(int i = 0;i < 32;i++) { PossibleMoveCards[i].setWidth(assets.CARDWIDTH); PossibleMoveCards[i].setHeight(assets.CARDHEIGHT); }

        assets.changeOrientation(newOrientation, moves);

        if(newOrientation == 1) {
            textPressAgainToExit.setFont(assets.font);
            textPressAgainToExit.setX(0);
            textPressAgainToExit.setY(assets.V_GAMEHEIGHT - 100);
            textPressAgainToExit.setWidth(assets.V_GAMEWIDTH);
            textPressAgainToExit.setHeight(50);
            textPressAgainToExit.setText("Press again to exit...");

            textNoMovesPossible.setFont(assets.font);
            textNoMovesPossible.setX(0);
            textNoMovesPossible.setY(assets.V_GAMEHEIGHT - 100);
            textNoMovesPossible.setWidth(assets.V_GAMEWIDTH);
            textNoMovesPossible.setHeight(50);
            textNoMovesPossible.setText("No moves detected...");

            labelGameOver1.changeOrientation(1);
            labelGameOver2.changeOrientation(1);
        } else {
            textPressAgainToExit.setFont(assets.font);
            textPressAgainToExit.setX(0);
            textPressAgainToExit.setY(assets.V_GAMEHEIGHT - 100);
            textPressAgainToExit.setWidth(assets.V_GAMEWIDTH);
            textPressAgainToExit.setHeight(50);
            textPressAgainToExit.setText("Press again to exit...");

            textNoMovesPossible.setFont(assets.font);
            textNoMovesPossible.setX(0);
            textNoMovesPossible.setY(assets.V_GAMEHEIGHT - 100);
            textNoMovesPossible.setWidth(assets.V_GAMEWIDTH);
            textNoMovesPossible.setHeight(50);
            textNoMovesPossible.setText("No moves detected...");

            labelGameOver1.changeOrientation(2);
            labelGameOver2.changeOrientation(2);
        }

        menu.changeOrientation(newOrientation);
        dialog.changeOrientation(newOrientation);
        exitDialog.changeOrientation(newOrientation);
        btnAutoComplete.changeOrientation(newOrientation);

        if(newOrientation == 1) {
            btnHideGooglePlayScreen.changeOrientation(1);
            btnGooglePlaySignOut.changeOrientation(1);
        } else {
            btnHideGooglePlayScreen.changeOrientation2((assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f - 10 - 60);
            btnGooglePlaySignOut.changeOrientation2((assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f + 60 + 10 + 60 + 10);
        }

        statisticScreen.changeOrientation(newOrientation);

        menu.reset();
        dialog.reset();

        btnArrowDown.setPressed(true);
        btnArrowUp.setPressed(false);

        setPos();
    }

    private void changeOrientation_Portrait() {
        if(Variables.USES_BANNER == true) {
            startY = 50;
        } else { startY = 0; }

        assets.V_GAMEWIDTH = 360f;
        assets.V_GAMEHEIGHT = assets.SCREENHEIGHT / (assets.SCREENWIDTH / assets.V_GAMEWIDTH);
        assets.CARD_SPACING_HORIZONTAL = 1;
        for(int i = 0;i < 7;i++) { assets.CARD_SPACING_VERTICAL_REVEALED[i] = 0.35f; }
        assets.CARD_SPACING_VERTICAL_UNREVEALED = 0.15f;
        assets.CARDWIDTH = (assets.V_GAMEWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) / 7;//45f;
        assets.CARDHEIGHT = 1.5f * assets.CARDWIDTH;//70f;

        assets.FLIP_SPEED = 260f;

        for(int i = 0;i < 52;i++) { cards[i].setWidth(assets.CARDWIDTH); cards[i].setHeight(assets.CARDHEIGHT); }

        foundationStacksY[0] = startY + (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksY[1] = startY + (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksY[2] = startY + (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksY[3] = startY + (float) (assets.CARDHEIGHT * 0.5);
        foundationStacksX[0] = 0;
        foundationStacksX[1] = 0 + assets.CARDWIDTH * 1;
        foundationStacksX[2] = 0 + assets.CARDWIDTH * 2;
        foundationStacksX[3] = 0 + assets.CARDWIDTH * 3;

        tableStacksY = startY + (int) (0.5 * assets.CARDHEIGHT + assets.CARDHEIGHT * 1.5);
        tableStacksX[0] = 0;// + assets.CARD_SPACING_HORIZONTAL;
        tableStacksX[1] = 0 + assets.CARDWIDTH * 1 + assets.CARD_SPACING_HORIZONTAL * 1;
        tableStacksX[2] = 0 + assets.CARDWIDTH * 2 + assets.CARD_SPACING_HORIZONTAL * 2;
        tableStacksX[3] = 0 + assets.CARDWIDTH * 3 + assets.CARD_SPACING_HORIZONTAL * 3;
        tableStacksX[4] = 0 + assets.CARDWIDTH * 4 + assets.CARD_SPACING_HORIZONTAL * 4;
        tableStacksX[5] = 0 + assets.CARDWIDTH * 5 + assets.CARD_SPACING_HORIZONTAL * 5;
        tableStacksX[6] = 0 + assets.CARDWIDTH * 6 + assets.CARD_SPACING_HORIZONTAL * 6;

        stockStackX = tableStacksX[6];
        stockStackY = foundationStacksY[0];

        stockStackRevealedX = (int) (0 + tableStacksX[3] + 1.15 * assets.CARDWIDTH);
        stockStackRevealedY = foundationStacksY[0];

        btnArrowDown.setX((assets.V_GAMEWIDTH - 95) * 0.5f);
        btnArrowDown.setWidth(95);
        btnArrowDown.setY(assets.V_GAMEHEIGHT - 85);
        btnArrowDown.setHeight(35);

        btnArrowUp.setX((assets.V_GAMEWIDTH - 95) * 0.5f);
        btnArrowUp.setWidth(95);
        btnArrowUp.setY((assets.V_GAMEHEIGHT - 50));
        btnArrowUp.setHeight(35);

        btnShowGooglePlayLeaderBoards.setX((assets.V_GAMEWIDTH - 235) * 0.5f);
        btnShowGooglePlayLeaderBoards.setWidth(235);
        btnShowGooglePlayLeaderBoards.setY((assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f - 10);
        btnShowGooglePlayLeaderBoards.setHeight(60);

        btnShowGooglePlayAchievments.setX((assets.V_GAMEWIDTH - 235) * 0.5f);
        btnShowGooglePlayAchievments.setWidth(235);
        btnShowGooglePlayAchievments.setY((assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f + 60 + 10);
        btnShowGooglePlayAchievments.setHeight(60);
    }

    private void changeOrientation_LandScape() {
        startY = 20;

        assets.V_GAMEHEIGHT = 360f;
        assets.V_GAMEWIDTH = assets.SCREENWIDTH / (assets.SCREENHEIGHT / assets.V_GAMEHEIGHT);

        assets.CARD_SPACING_HORIZONTAL = 1;
        for(int i = 0;i < 7;i++) { assets.CARD_SPACING_VERTICAL_REVEALED[i] = 0.35f; }
        assets.CARD_SPACING_VERTICAL_UNREVEALED = 0.15f;
        assets.CARDWIDTH = (assets.V_GAMEWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL - assets.V_GAMEWIDTH * 0.38f) / 7;//45f;
        assets.CARDHEIGHT = (assets.V_GAMEHEIGHT - 19 - 0.5f - assets.CARDWIDTH*2) / 4;//4.4f * assets.CARDWIDTH;

        assets.FLIP_SPEED = 95f;

        for(int i = 0;i < 52;i++) { cards[i].setWidth(assets.CARDWIDTH); cards[i].setHeight(assets.CARDHEIGHT); }

        foundationStacksY[0] = (float) (assets.CARDHEIGHT * 0.3 - assets.CARD_SPACING_VERTICAL_STOCK * 0);
        foundationStacksY[1] = (float) (assets.CARDHEIGHT * 1.3 - assets.CARD_SPACING_VERTICAL_STOCK * 1);
        foundationStacksY[2] = (float) (assets.CARDHEIGHT * 2.3 - assets.CARD_SPACING_VERTICAL_STOCK * 2);
        foundationStacksY[3] = (float) (assets.CARDHEIGHT * 3.3 - assets.CARD_SPACING_VERTICAL_STOCK * 3);
        foundationStacksX[0] = assets.V_GAMEWIDTH - assets.CARDWIDTH - assets.CARD_SPACING_HORIZONTAL;
        foundationStacksX[1] = assets.V_GAMEWIDTH - assets.CARDWIDTH - assets.CARD_SPACING_HORIZONTAL;
        foundationStacksX[2] = assets.V_GAMEWIDTH - assets.CARDWIDTH - assets.CARD_SPACING_HORIZONTAL;
        foundationStacksX[3] = assets.V_GAMEWIDTH - assets.CARDWIDTH - assets.CARD_SPACING_HORIZONTAL;

        tableStacksY = startY + (int) (0.1 * assets.CARDHEIGHT);
        tableStacksX[0] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 0 + assets.CARD_SPACING_HORIZONTAL * 0;
        tableStacksX[1] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 1 + assets.CARD_SPACING_HORIZONTAL * 1;
        tableStacksX[2] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 2 + assets.CARD_SPACING_HORIZONTAL * 2;
        tableStacksX[3] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 3 + assets.CARD_SPACING_HORIZONTAL * 3;
        tableStacksX[4] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 4 + assets.CARD_SPACING_HORIZONTAL * 4;
        tableStacksX[5] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 5 + assets.CARD_SPACING_HORIZONTAL * 5;
        tableStacksX[6] = (assets.V_GAMEWIDTH - 7 * assets.CARDWIDTH - 6 * assets.CARD_SPACING_HORIZONTAL) * 0.5f + assets.CARDWIDTH * 6 + assets.CARD_SPACING_HORIZONTAL * 6;

        stockStackX = 0 + assets.CARD_SPACING_HORIZONTAL + assets.CARDWIDTH * 0.55f;
        stockStackY = foundationStacksY[2];

        stockStackRevealedX = 0 + assets.CARD_SPACING_HORIZONTAL + assets.CARDWIDTH * 0.25f;
        stockStackRevealedY = foundationStacksY[0] + assets.CARDHEIGHT * 0.5f;

        btnArrowDown.setX((assets.V_GAMEWIDTH - 40) * 0.5f);
        btnArrowDown.setWidth(40);
        btnArrowDown.setY(assets.V_GAMEHEIGHT - 85);
        btnArrowDown.setHeight(35);

        btnArrowUp.setX((assets.V_GAMEWIDTH - 40) * 0.5f);
        btnArrowUp.setWidth(40);
        btnArrowUp.setY((assets.V_GAMEHEIGHT - 40));
        btnArrowUp.setHeight(35);

        btnShowGooglePlayLeaderBoards.setX((assets.V_GAMEWIDTH - 100) * 0.5f);
        btnShowGooglePlayLeaderBoards.setWidth(100);
        btnShowGooglePlayLeaderBoards.setY((assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f - 10);
        btnShowGooglePlayLeaderBoards.setHeight(60);

        btnShowGooglePlayAchievments.setX((assets.V_GAMEWIDTH - 100) * 0.5f);
        btnShowGooglePlayAchievments.setWidth(100);
        btnShowGooglePlayAchievments.setY((assets.V_GAMEHEIGHT - 60 * 2 - 10) * 0.5f + 60 + 10);
        btnShowGooglePlayAchievments.setHeight(60);
    }

    public Card[] getCards() { return cards; }

    public ArrayList<Integer> getTableStack1() { return TableStack1; }

    public ArrayList<Integer> getTableStack2() { return TableStack2; }

    public ArrayList<Integer> getTableStack3() { return TableStack3; }

    public ArrayList<Integer> getTableStack4() { return TableStack4; }

    public ArrayList<Integer> getTableStack5() { return TableStack5; }

    public ArrayList<Integer> getTableStack6() { return TableStack6; }

    public ArrayList<Integer> getTableStack7() { return TableStack7; }

    public ArrayList<Integer> getFoundationStack1() { return FoundationStack1; }

    public ArrayList<Integer> getFoundationStack2() { return FoundationStack2; }

    public ArrayList<Integer> getFoundationStack3() { return FoundationStack3; }

    public ArrayList<Integer> getFoundationStack4() { return FoundationStack4; }

    public ArrayList<Integer> getStockStack() { return StockStack; }

    public ArrayList<Integer> getStockStackRevealed() { return StockStackRevealed; }

    public ArrayList<Integer> getMoveCards() { return MoveCards; }

    public AnimationCard[] getAnimationCards() { return AnimationCards; }

    public AnimationCard[] getPossibleMoveCards() { return PossibleMoveCards; }

    public ArrowButton getBtnArrowUp() { return btnArrowUp; }

    public ArrowButton getBtnArrowDown() { return btnArrowDown; }

    public Menu getMenu() { return menu; }

    public Dialog getDialog() { return dialog; }

    public AutoCompleteButton getBtnAutoComplete() { return btnAutoComplete; }

    public GameState getGameState() { return currentGameState; }

    public boolean isNewGameAnimation() { return isNewGameAnimation; }

    public boolean isGameOverAnimation() { return isGameOverAnimation; }

    public void setGameState(GameState gameState) { currentGameState = gameState; }

    public void setShowSettingsScreen(boolean bool) { showSettingsScreen = bool; }

    public boolean showSettingsScreen() { return showSettingsScreen; }

    public void setShowInfoScreen(boolean bool) { showInfoScreen = bool; }

    public boolean showInfoScreen() { return showInfoScreen; }

    public void setShowGooglePlayUIScreen(boolean bool) {
        //if(USES_GOOGLE_PLAY_ACHIEVMENT_AND_LEADERBOARDS) {
            if(bool == true) {
                saveGameState = currentGameState;

                currentGameState = GameState.GOOGLE_PLAY_SCREEN;
            } else {
                currentGameState = saveGameState;
            }
        //}
    }

    public void setShowGoogleAchievments(boolean bool) { showGooglePlayAchievments = bool; }

    public boolean showGooglePlayAchievments() { return showGooglePlayAchievments; }

    public void setShowGoogleLeaderBoards(boolean bool) { showGooglePlayLeaderBoards = bool; }

    public boolean showGooglePlayLeaderBoards() { return showGooglePlayLeaderBoards; }

    public void setGooglePlaySignOut(boolean bool) { GooglePlaySignOut = bool; }

    public boolean googlePlaySignOut() { return GooglePlaySignOut; }

    public boolean isADCached() { return isADCached; }

    public void setIsADCached(boolean bool) { isADCached = bool; }

    public boolean isNewGame() { return isNewGame; }

    public void setIsNewGame(boolean bool) { isNewGame = bool; }
}