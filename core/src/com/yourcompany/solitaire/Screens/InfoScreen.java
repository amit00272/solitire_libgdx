package com.yourcompany.solitaire.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.Solitaire;

public class InfoScreen implements Screen {
    private CustomAssetManager assets;
    Solitaire solitaire;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    private boolean showGameScreen;

    private String about = "Klondike Solitaire is a card game played with a single deck of 52 playing cards. After the game started, 28 cards are dealt face down to form the tableau which consists of 7 piles. Each of those piles contains one more card than the previous and the last card is always dealt face up. The 24 remaining are dealt face down to form the stock.";
    private String howtowin = "The aim of the game is to move all 4 aces to the foundations and build each up in suit from ace to king.";
    private String howtoplay = "Turn cards face-up from the stock-pile onto the waste-pile. The top card of the waste-pile may be played onto one of the 7 tableau-piles or directly to one of the 4 foundation-piles. The top card of each tableau-pile is available for play onto the foundation-piles or another tableau-pile. Cards within the tableau may be placed down in sequence and alternating color. A sequence of cards may also be shifted from one tableau-pile to another. If one of the tableau-piles is empty it may only be filled with a king. The game ends when either all foundation-piles are filled (You win) or when no more moves are possible (You Loose).";
    private String stdScoring = "Standard Scoring is turned on by default, the rules are as follows:\n" +
            "\n" +
            "Waste to tableau = +5 Points\n" +
            "Waste to foundation = +10 Points\n" +
            "Tableau to foundation = +10 Points\n" +
            "Turn over tableau card = +5 Points\n" +
            "Foundation to tableau = -15 Points\n" +
            "Recycle Stock = -100 Points";
    private String vegasScoring = "Vegas Scoring can be enabled in the option menu. Vegas Scoring rules are as follows:\n" +
            "\n" +
            "You start with -52 Points, -1 Point for every card in the deck. You get +5 Points for every card you shift to the foundation-piles. You must move atleast 11 cards to the foundations to get a score above 0. The maximum score is 208.";

    private float V_GAMEWIDTH_PORTRAIT;
    private float V_GAMEHEIGHT_PORTRAIT;

    public InfoScreen(Solitaire solitaire) {
        this.solitaire = solitaire;
        this.assets = solitaire.assets;

        V_GAMEWIDTH_PORTRAIT = assets.V_GAMEWIDTH;
        V_GAMEHEIGHT_PORTRAIT = assets.V_GAMEHEIGHT;

        viewport = new FitViewport(V_GAMEWIDTH_PORTRAIT, V_GAMEHEIGHT_PORTRAIT);

        skin = new Skin(Gdx.files.internal("data/tex/uiskin.json"));

        skin.getFont("default-font").getData().setScale(0.2f, 0.2f);

        stage = new Stage(viewport);

        Table rootTable = new Table().top();

        Table table1 = new Table();
        Label lblSettings = new Label("Information", skin);
        lblSettings.setAlignment(Align.center);
        lblSettings.setFontScale(skin.getFont("default-font").getScaleX() * 1.8f, skin.getFont("default-font").getScaleY() * 1.8f);
        table1.add(lblSettings).width(V_GAMEWIDTH_PORTRAIT).padTop(30);
        table1.row().height(30);
        table1.add().width(V_GAMEWIDTH_PORTRAIT);

        Table table2 = new Table();
        Label lblAbout = new Label("About", skin);
        lblAbout.setFontScale(skin.getFont("default-font").getScaleX() * 1.5f, skin.getFont("default-font").getScaleY() * 1.5f);
        table2.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table2.add(lblAbout).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table2.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table2.row().height(25);
        table2.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table3 = new Table();
        TextureRegion texInfo = new TextureRegion(assets.info);
        texInfo.flip(false, true);
        Image imgInfo = new Image(texInfo);
        table3.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table3.add(imgInfo).size(V_GAMEWIDTH_PORTRAIT * 0.75f, V_GAMEWIDTH_PORTRAIT * 0.55f);
        table3.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table3.row().height(25);
        table3.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table4 = new Table();
        Label lblAboutInfo = new Label(about, skin);
        lblAboutInfo.setWrap(true);
        lblAboutInfo.setFontScale(skin.getFont("default-font").getScaleX() * 1.1f, skin.getFont("default-font").getScaleY() * 1.1f);
        table4.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table4.add(lblAboutInfo).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table4.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table4.row().height(25);
        table4.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table5 = new Table();
        Label lblHowToWin = new Label("How to win?", skin);
        lblHowToWin.setWrap(true);
        lblHowToWin.setFontScale(skin.getFont("default-font").getScaleX() * 1.5f, skin.getFont("default-font").getScaleY() * 1.5f);
        table5.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table5.add(lblHowToWin).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table5.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table5.row().height(25);
        table5.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table6 = new Table();
        Label lblHowToWinInfo = new Label(howtowin, skin);
        lblHowToWinInfo.setWrap(true);
        lblHowToWinInfo.setFontScale(skin.getFont("default-font").getScaleX() * 1.1f, skin.getFont("default-font").getScaleY() * 1.1f);
        table6.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table6.add(lblHowToWinInfo).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table6.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table6.row().height(25);
        table6.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table7 = new Table();
        Label lblHowToPlay = new Label("How to play?", skin);
        lblHowToPlay.setWrap(true);
        lblHowToPlay.setFontScale(skin.getFont("default-font").getScaleX() * 1.5f, skin.getFont("default-font").getScaleY() * 1.5f);
        table7.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table7.add(lblHowToPlay).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table7.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table7.row().height(25);
        table7.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table8 = new Table();
        Label lblHowToPlayInfo = new Label(howtoplay, skin);
        lblHowToPlayInfo.setWrap(true);
        lblHowToPlayInfo.setFontScale(skin.getFont("default-font").getScaleX() * 1.1f, skin.getFont("default-font").getScaleY() * 1.1f);
        table8.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table8.add(lblHowToPlayInfo).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table8.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table8.row().height(25);
        table8.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table9 = new Table();
        Label lblStdScoring = new Label("Standard Scoring", skin);
        lblStdScoring.setWrap(true);
        lblStdScoring.setFontScale(skin.getFont("default-font").getScaleX() * 1.5f, skin.getFont("default-font").getScaleY() * 1.5f);
        table9.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table9.add(lblStdScoring).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table9.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table9.row().height(25);
        table9.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table10 = new Table();
        Label lblStdScoringInfo = new Label(stdScoring, skin);
        lblStdScoringInfo.setWrap(true);
        lblStdScoringInfo.setFontScale(skin.getFont("default-font").getScaleX() * 1.1f, skin.getFont("default-font").getScaleY() * 1.1f);
        table10.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table10.add(lblStdScoringInfo).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table10.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table10.row().height(25);
        table10.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table11 = new Table();
        Label lblVegasScoring = new Label("Vegas Scoring", skin);
        lblVegasScoring.setWrap(true);
        lblVegasScoring.setFontScale(skin.getFont("default-font").getScaleX() * 1.5f, skin.getFont("default-font").getScaleY() * 1.5f);
        table11.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table11.add(lblVegasScoring).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table11.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table11.row().height(25);
        table11.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table12 = new Table();
        Label lblVegasScoringInfo = new Label(vegasScoring, skin);
        lblVegasScoringInfo.setWrap(true);
        lblVegasScoringInfo.setFontScale(skin.getFont("default-font").getScaleX() * 1.1f, skin.getFont("default-font").getScaleY() * 1.1f);
        table12.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table12.add(lblVegasScoringInfo).width(V_GAMEWIDTH_PORTRAIT * 0.90f);
        table12.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        table12.row().height(25);
        table12.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table tableEnd = new Table();
        TextButton btnBack = new TextButton("Done", skin);
        btnBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                showGameScreen = true;
            }
        });
        tableEnd.add(btnBack).size(V_GAMEWIDTH_PORTRAIT, V_GAMEWIDTH_PORTRAIT * 0.15f);
        tableEnd.row().height(15);
        tableEnd.add().width(V_GAMEWIDTH_PORTRAIT);

        rootTable.add(table1);
        rootTable.row();
        rootTable.add(table2);
        rootTable.row();
        rootTable.add(table3);
        rootTable.row();
        rootTable.add(table4);
        rootTable.row();
        rootTable.add(table5);
        rootTable.row();
        rootTable.add(table6);
        rootTable.row();
        rootTable.add(table7);
        rootTable.row();
        rootTable.add(table8);
        rootTable.row();
        rootTable.add(table9);
        rootTable.row();
        rootTable.add(table10);
        rootTable.row();
        rootTable.add(table11);
        rootTable.row();
        rootTable.add(table12);
        rootTable.row();

        rootTable.add(tableEnd);
        rootTable.row();

        ScrollPane scroller = new ScrollPane(rootTable);

        Table table = new Table();
        table.setFillParent(true);
        table.add(scroller).fill().expand();

        scroller.setOverscroll(false, false);

        stage.addActor(table);
    }

    @Override
    public void show() {

        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        if(showGameScreen == true) { solitaire.setGameScreen(); showGameScreen = false; }
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}