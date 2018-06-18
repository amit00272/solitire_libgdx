package com.yourcompany.solitaire.Helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;

public class CustomAssetManager {
    public final int ORIENTATION_PORTRAIT = 1;
    public final int ORIENTATION_LANDSCAPE = 2;

    public int BACKGROUND_ID = 2;
    public int FACE_CARD_ID = 0;
    public int BACK_CARD_ID = 0;
    public int ORIENTATION = 1;

    public boolean SHOW_SCORE = true;
    public boolean SHOW_TIME = false;
    public boolean SHOW_MOVES = true;
    public boolean SOUND_ENABLED = true;
    public boolean VEGAS_SCORING = false;
    public boolean DRAW_THREE = false;
    public boolean SHOW_AVAILABLE_MOVES = false;

    public boolean SHOW_EXIT_DIALOG = false;
    public boolean IS_BACK_BUTTONPRESSED = false;

    public float SCREENWIDTH = Gdx.graphics.getWidth();
    public float SCREENHEIGHT = Gdx.graphics.getHeight();
    public float V_GAMEWIDTH = 360f;
    public float V_GAMEHEIGHT = SCREENHEIGHT / (SCREENWIDTH / V_GAMEWIDTH);

    public float CARD_SPACING_HORIZONTAL = 1;
    public float[] CARD_SPACING_VERTICAL_REVEALED = { 0.35f, 0.35f, 0.35f, 0.35f, 0.35f, 0.35f, 0.35f, 0.35f, 0.35f, 0.35f };
    public float CARD_SPACING_VERTICAL_UNREVEALED = 0.15f;

    public float CARD_SPACING_VERTICAL_STOCK = 0.35f;

    public float CARDWIDTH = (V_GAMEWIDTH - 6 * CARD_SPACING_HORIZONTAL) / 7;
    public float CARDHEIGHT = 1.5f * CARDWIDTH;

    public float FLIP_SPEED = 280f;

    public final float isActionGoalTimeMoveCards = 0.20f;//10f;
    public final float isActionGoalTimeInvalidMoveTapping = 20f;
    public final float isActionGoalTimeRefillStockStack = 0.20f;//12f;

    public float GAMEOVER_TEXT_ANIMATION = 0.25f;

    public TextureAtlas atlas_ui, atlas_cardsFace1, atlas_cardsFace2;
    public Texture tex_Info;
    public TextureRegion info;
    public TextureRegion img_Phone_Portrait, img_Phone_Landscape;
    public TextureRegion[] img_Background = new TextureRegion[4];
    public TextureRegion[] img_CardBack = new TextureRegion[4];
    public TextureRegion img_CardFreeCell, img_CardFoundation, img_CardFoundationRefresh;
    public TextureRegion UI_Black_rounded, UI_Black, UI_White, UI_Orange, UI_Frame, UI_Frame2, UI_Line, UI_ArrowDown, UI_ArrowUp, UI_Hint, UI_Undo, UI_Play, UI_Statistic, UI_Settings, UI_Info, UI_GooglePlay, UI_Achievments, UI_Leaderboards;
    public TextureRegion UI_StatisticsBackground, UI_BTN_StatisticsClose;
    public ArrayList<TextureRegion> img_Cards = new ArrayList<TextureRegion>();
    public BitmapFont font, shadow, font2, dialogFont, dialogButtonFont, gameOverLabelFont, gameOverLabelFont2;
    public BitmapFont fntStrStatistics, fntStatisticsEntrys;
    public GlyphLayout glyphLayout_time, glyphLayout_moves;

    public Sound[] sfx_cardBridge = new Sound[2];
    public Sound[] sfx_FlipCard = new Sound[3];
    public Sound[] sfx_moveCard = new Sound[3];

    private AssetManager assetManager;

    private Preferences prefs;

    private boolean hasFinishedLoading;

    public CustomAssetManager() {
        assetManager = new AssetManager();

        prefs = Gdx.app.getPreferences("SOLITAIRE324324332");

        SHOW_SCORE = showScore();
        SHOW_TIME = showTime();
        SHOW_MOVES = showMoves();
        SOUND_ENABLED = soundEnabled();
        VEGAS_SCORING = vegasScoring();
        DRAW_THREE = drawThree();

        FACE_CARD_ID = getFaceCardID();
        BACK_CARD_ID = getBackCardID();
        BACKGROUND_ID = getBackgroundID();
        ORIENTATION = getOrientation();

        loadAssets();
    }

    public void update() {
        if(assetManager.update() == true) {
            setAssets();

            hasFinishedLoading = true;
        } else {
            //Gdx.app.log("test", assetManager.getProgress() + "");
        }
    }

    private void loadAssets() {
        assetManager.load("data/tex/misc.atlas", TextureAtlas.class);
        assetManager.load("data/tex/cardsFace1.atlas", TextureAtlas.class);
        assetManager.load("data/tex/cardsFace2.atlas", TextureAtlas.class);
        assetManager.load("data/tex/info.png", Texture.class);

        assetManager.load("data/sound/cardBridge1.wav", Sound.class);
        assetManager.load("data/sound/cardBridge2.wav", Sound.class);
        assetManager.load("data/sound/cardShove2.wav", Sound.class);
        assetManager.load("data/sound/cardShove3.wav", Sound.class);
        assetManager.load("data/sound/cardShove4.wav", Sound.class);
        assetManager.load("data/sound/cardSlide3.wav", Sound.class);
        assetManager.load("data/sound/cardSlide4.wav", Sound.class);
        assetManager.load("data/sound/cardSlide5.wav", Sound.class);

        hasFinishedLoading = false;
    }

    private void setAssets() {
        sfx_cardBridge[0] = assetManager.get("data/sound/cardBridge1.wav", Sound.class);
        sfx_cardBridge[1] = assetManager.get("data/sound/cardBridge2.wav", Sound.class);
        sfx_FlipCard[0] = assetManager.get("data/sound/cardSlide3.wav", Sound.class);
        sfx_FlipCard[1] = assetManager.get("data/sound/cardSlide4.wav", Sound.class);
        sfx_FlipCard[2] = assetManager.get("data/sound/cardSlide5.wav", Sound.class);
        sfx_moveCard[0] = assetManager.get("data/sound/cardShove2.wav", Sound.class);
        sfx_moveCard[1] = assetManager.get("data/sound/cardShove3.wav", Sound.class);
        sfx_moveCard[2] = assetManager.get("data/sound/cardShove4.wav", Sound.class);

        atlas_ui = assetManager.get("data/tex/misc.atlas", TextureAtlas.class);
        atlas_cardsFace1 = assetManager.get("data/tex/cardsFace1.atlas", TextureAtlas.class);
        atlas_cardsFace2 = assetManager.get("data/tex/cardsFace2.atlas", TextureAtlas.class);

        tex_Info = assetManager.get("data/tex/info.png", Texture.class);
        info = new TextureRegion(tex_Info);
        info.flip(false, true);

        img_Phone_Portrait = atlas_ui.findRegion("phone_portrait");
        img_Phone_Portrait.flip(false, true);
        img_Phone_Landscape = atlas_ui.findRegion("phone_landscape");
        img_Phone_Landscape.flip(false, true);

        for(int i = 0;i < 4;i++) { img_Background[i] = atlas_ui.findRegion("background" + (i+1)); img_Background[i].flip(false, true); }
        for(int i = 0;i < 4;i++) { img_CardBack[i] = atlas_ui.findRegion("back" + (i+1)); img_CardBack[i].flip(false, true); }

        img_CardFreeCell = atlas_ui.findRegion("freecell");
        img_CardFreeCell.flip(false, true);
        img_CardFoundation = atlas_ui.findRegion("foundation");
        img_CardFoundation.flip(false, true);
        img_CardFoundationRefresh = atlas_ui.findRegion("foundation_refresh");
        img_CardFoundationRefresh.flip(false, true);

        for(int i = 0;i < 13;i++)
        {
            img_Cards.add(atlas_cardsFace1.findRegion("a" + (i + 1)));
            img_Cards.add(atlas_cardsFace1.findRegion("b" + (i + 1)));
            img_Cards.add(atlas_cardsFace1.findRegion("c" + (i + 1)));
            img_Cards.add(atlas_cardsFace1.findRegion("d" + (i + 1)));
        }

        for(int i = 0;i < 13;i++)
        {
            img_Cards.add(atlas_cardsFace2.findRegion("a" + (i + 1)));
            img_Cards.add(atlas_cardsFace2.findRegion("b" + (i + 1)));
            img_Cards.add(atlas_cardsFace2.findRegion("c" + (i + 1)));
            img_Cards.add(atlas_cardsFace2.findRegion("d" + (i + 1)));
        }

        for(int i = 0;i < img_Cards.size();i++)
        {
            img_Cards.get(i).flip(false, true);
        }

        UI_BTN_StatisticsClose = atlas_ui.findRegion("btnStatisticsClose");
        UI_BTN_StatisticsClose.flip(false, true);

        UI_StatisticsBackground = atlas_ui.findRegion("statisticsbackground");
        UI_StatisticsBackground.flip(false, true);

        UI_Black_rounded = atlas_ui.findRegion("black_rounded");
        UI_Black_rounded.flip(false, true);
        UI_Black = atlas_ui.findRegion("black");
        UI_Black.flip(false, true);
        UI_White = atlas_ui.findRegion("white");
        UI_White.flip(false, true);
        UI_Orange = atlas_ui.findRegion("orange");
        UI_Orange.flip(false, true);
        UI_Frame = atlas_ui.findRegion("frame");
        UI_Frame.flip(false, true);
        UI_Frame2 = atlas_ui.findRegion("frame2");
        UI_Frame2.flip(false, true);
        UI_Line = atlas_ui.findRegion("line");
        UI_Line.flip(false, true);
        UI_ArrowDown = atlas_ui.findRegion("arrow_down");
        UI_ArrowDown.flip(false, true);
        UI_ArrowUp = atlas_ui.findRegion("arrow_up");
        UI_ArrowUp.flip(false, true);
        UI_Play = atlas_ui.findRegion("ui_play");
        UI_Play.flip(false, true);
        UI_Statistic = atlas_ui.findRegion("ui_statistics");
        UI_Statistic.flip(false, true);
        UI_Settings = atlas_ui.findRegion("ui_settings");
        UI_Settings.flip(false, true);
        UI_Undo = atlas_ui.findRegion("ui_undo");
        UI_Undo.flip(false, true);
        UI_Hint = atlas_ui.findRegion("ui_hint");
        UI_Hint.flip(false, true);
        UI_Info = atlas_ui.findRegion("ui_info");
        UI_Info.flip(false, true);
        UI_GooglePlay = atlas_ui.findRegion("ui_googleplay");
        UI_GooglePlay.flip(false, true);
        UI_Achievments = atlas_ui.findRegion("btnArchievments");
        UI_Achievments.flip(false, true);
        UI_Leaderboards = atlas_ui.findRegion("btnLeaderBoards");
        UI_Leaderboards.flip(false, true);

        fntStrStatistics = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        fntStrStatistics.setColor(1, 1, 1, 0.9f);
        fntStrStatistics.getData().setScale(.33f, -.33f);

        fntStatisticsEntrys = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        fntStatisticsEntrys.setColor(1, 1, 1, 0.9f);
        fntStatisticsEntrys.getData().setScale(.23f, -.23f);

        font = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        font.setColor(1, 1, 1, 0.9f);
        font.getData().setScale(.23f, -.23f);

        shadow = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        shadow.setColor(0, 0, 0, 0.9f);
        shadow.getData().setScale(.23f, -.23f);

        font2 = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        font2.getData().setScale(.53f, -.53f);

        dialogFont = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        dialogFont.setColor(1, 1, 1, 1f);
        dialogFont.getData().setScale(.32f, -.32f);

        dialogButtonFont = new BitmapFont(Gdx.files.internal("data/font/gillsans.fnt"));
        dialogButtonFont.setColor(0, 0, 0, 1f);
        dialogButtonFont.getData().setScale(.28f, -.28f);

        gameOverLabelFont = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        gameOverLabelFont.setColor(1, 1, 1, 1f);
        gameOverLabelFont.getData().setScale(.15f, -.15f);

        gameOverLabelFont2 = new BitmapFont(Gdx.files.internal("data/font/gillsans_bold.fnt"));
        gameOverLabelFont2.setColor(1, 1, 1, 1f);
        gameOverLabelFont2.getData().setScale(.15f, -.15f);

        glyphLayout_time = new GlyphLayout();
        glyphLayout_time.setText(font, "Time: 0:12");

        glyphLayout_moves = new GlyphLayout();
        glyphLayout_moves.setText(font, "Moves: 00");
    }

    public void changeOrientation(int newOrientation, int MOVES) {
        if(newOrientation == 1) {
            font.setColor(1, 1, 1, 0.9f);
            font.getData().setScale(.23f, -.23f);

            shadow.setColor(0, 0, 0, 0.9f);
            shadow.getData().setScale(.23f, -.23f);

            dialogFont.getData().setScale(.32f, -.32f);
            dialogButtonFont.getData().setScale(.28f, -.28f);

            gameOverLabelFont.getData().setScale(.15f, -.15f);
            gameOverLabelFont2.getData().setScale(.15f, -.15f);

            fntStrStatistics.setColor(1, 1, 1, 0.9f);
            fntStrStatistics.getData().setScale(.33f, -.33f);

            fntStatisticsEntrys.setColor(1, 1, 1, 0.9f);
            fntStatisticsEntrys.getData().setScale(.23f, -.23f);

            glyphLayout_time.setText(font, "Time: 0:12");
            if(MOVES >= 100) { glyphLayout_moves.setText(font, "Moves: 000"); }
            else { glyphLayout_moves.setText(font, "Moves: 00"); }
        } else {
            font.setColor(1, 1, 1, 0.9f);
            font.getData().setScale(.07f, -.16f);

            shadow.setColor(0, 0, 0, 0.9f);
            shadow.getData().setScale(.07f, -.16f);

            dialogFont.getData().setScale(.10f, -.20f);
            dialogButtonFont.getData().setScale(.10f, -.20f);

            gameOverLabelFont.getData().setScale(.10f, -.20f);
            gameOverLabelFont2.getData().setScale(.10f, -.20f);

            fntStrStatistics.setColor(1, 1, 1, 0.9f);
            fntStrStatistics.getData().setScale(.17f, -.26f);

            fntStatisticsEntrys.setColor(1, 1, 1, 0.9f);
            fntStatisticsEntrys.getData().setScale(.07f, -.16f);

            glyphLayout_time.setText(font, "Time: 0:12");
            if(MOVES >= 100) { glyphLayout_moves.setText(font, "Moves: 000"); }
            else { glyphLayout_moves.setText(font, "Moves: 00"); }
        }
    }

    public int getHighestWinID()
    {
        return prefs.getInteger("ID", 345);
    }

    public void setNewHighestWinID(int id)
    {
        id++;
        prefs.putInteger("ID", id);
        prefs.flush();
    }

    public int getNumWinsOverAll() { return prefs.getInteger("WINS", 0); }

    public void increaseNumWinsOverAll() { prefs.putInteger("WINS", getNumWinsOverAll()+1); prefs.flush(); }

    public int getNumLongestStreak() { return prefs.getInteger("STREAK", 0); }

    public void increaseNumLongestStreak() { prefs.putInteger("STREAK", getNumLongestStreak()+1); prefs.flush();
    }

    public void resetNumLogestStreak() { prefs.putInteger("STREAK", 0); prefs.flush(); }

    public void setShowScore(boolean bool) { prefs.putBoolean("SHOW_SCORE", bool); prefs.flush(); }
    public void setShowTime(boolean bool) { prefs.putBoolean("SHOW_TIME", bool); prefs.flush(); }
    public void setShowMoves(boolean bool) { prefs.putBoolean("SHOW_MOVES", bool); prefs.flush(); }
    public void setSoundEnabled(boolean bool) { prefs.putBoolean("SOUND_ENABLED", bool); prefs.flush(); }
    public void setDrawThree(boolean bool) { prefs.putBoolean("DRAW_THREE", bool); prefs.flush();
    }

    public void setVegasScoring(boolean bool) { prefs.putBoolean("VEGAS_SCORING", bool); prefs.flush(); }
    public void setFaceCardID(int val) { prefs.putInteger("FACE_CARD_ID", val); prefs.flush(); }
    public void setBackCardID(int val) { prefs.putInteger("BACK_CARD_ID", val); prefs.flush(); }
    public void setBackgroundID(int val) { prefs.putInteger("BACKGROUND_ID", val); prefs.flush(); }
    public void setOrientation(int val) { prefs.putInteger("ORIENTATION", val); prefs.flush(); }

    public boolean showScore() { return prefs.getBoolean("SHOW_SCORE", true); }
    public boolean showTime() { return prefs.getBoolean("SHOW_TIME", false); }
    public boolean showMoves() { return prefs.getBoolean("SHOW_MOVES", true); }
    public boolean soundEnabled() { return prefs.getBoolean("SOUND_ENABLED", true); }
    public boolean drawThree() { return prefs.getBoolean("DRAW_THREE", false);
    }

    public boolean vegasScoring() { return prefs.getBoolean("VEGAS_SCORING", false); }
    public int getFaceCardID() { return prefs.getInteger("FACE_CARD_ID", 0); }
    public int getBackCardID() { return prefs.getInteger("BACK_CARD_ID", 0); }
    public int getBackgroundID() { return prefs.getInteger("BACKGROUND_ID", 2); }
    public int getOrientation() { return prefs.getInteger("ORIENTATION", 1); }

    public void setBestScore(int val) { prefs.putInteger("BEST_SCORE", val); prefs.flush(); }
    public void setBestTimeInSeconds(int val) { prefs.putInteger("BEST_TIME", val); prefs.flush(); }
    public void setTotalTimeInSeconds(int val) { prefs.putInteger("TOTAL_TIME", val); prefs.flush(); }
    public void setTotalGames(int val) { prefs.putInteger("TOTAL_GAMES", val); prefs.flush(); }
    public void setTotalWins(int val) { prefs.putInteger("TOTAL_WINS", val); prefs.flush(); }

    public int getBestScore() { return prefs.getInteger("BEST_SCORE", 0); }
    public int getBestTimeInSeconds() { return prefs.getInteger("BEST_TIME", 99999); }
    public int getTotalTimeInSeconds() { return prefs.getInteger("TOTAL_TIME", 0); }
    public int getTotalGames() { return prefs.getInteger("TOTAL_GAMES", 0); }
    public int getTotalWins() { return prefs.getInteger("TOTAL_WINS", 0); }

    public boolean hasFinishedLoading() {
        return hasFinishedLoading;
    }
}