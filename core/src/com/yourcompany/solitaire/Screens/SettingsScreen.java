package com.yourcompany.solitaire.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.Solitaire;

import static com.yourcompany.solitaire.Helper.Variables.*;

public class SettingsScreen implements Screen {
    private CustomAssetManager assets;
    Solitaire solitaire;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    CheckBox ckBoxOrientation1;
    CheckBox ckBoxOrientation2;
    final CheckBox ckBoxBackground1;
    final CheckBox ckBoxBackground2;
    final CheckBox ckBoxBackground3;
    final CheckBox ckBoxBackground4;
    final CheckBox ckBoxBack1;
    final CheckBox ckBoxBack2;
    final CheckBox ckBoxBack3;
    final CheckBox ckBoxBack4;
    final CheckBox ckBoxFace1;
    final CheckBox ckBoxFace2;

    final CheckBox ckBoxSound;
    final CheckBox ckBoxMoves;
    final CheckBox ckBoxTime;
    final CheckBox ckBoxScore;
    final CheckBox ckBoxVegasScoring;
    final CheckBox ckBoxDrawThree;

    boolean soundEnabled;
    boolean showMoves;
    boolean showTime;
    boolean showScore;
    boolean vegasScoring;
    boolean drawThree;

    private float V_GAMEWIDTH_PORTRAIT;
    private float V_GAMEHEIGHT_PORTRAIT;

    public boolean orientationChanged;

    private boolean showGameScreen;

    public SettingsScreen(Solitaire solitaire) {
        this.solitaire = solitaire;
        this.assets = solitaire.assets;

        V_GAMEWIDTH_PORTRAIT = assets.V_GAMEWIDTH;
        V_GAMEHEIGHT_PORTRAIT = assets.V_GAMEHEIGHT;

        viewport = new FitViewport(V_GAMEWIDTH_PORTRAIT, V_GAMEHEIGHT_PORTRAIT);

        skin = new Skin(Gdx.files.internal("data/tex/uiskin.json"));

        skin.getFont("default-font").getData().setScale(0.2f, 0.2f);

        stage = new Stage(viewport);

        soundEnabled = assets.SOUND_ENABLED;
        showTime = assets.SHOW_TIME;
        showScore = assets.SHOW_SCORE;
        showMoves = assets.SHOW_MOVES;
        vegasScoring = assets.VEGAS_SCORING;
        drawThree = assets.DRAW_THREE;

        Table rootTable = new Table().top();

        TextureRegion line = new TextureRegion(assets.UI_Line);
        line.flip(false, true);
        final Image imgLine = new Image(line);

        Table table1 = new Table();
        Label lblSettings = new Label("Settings", skin);
        lblSettings.setAlignment(Align.center);
        lblSettings.setFontScale(skin.getFont("default-font").getScaleX() * 1.8f, skin.getFont("default-font").getScaleY() * 1.8f);
        table1.add(lblSettings).width(V_GAMEWIDTH_PORTRAIT).padTop(30);
        table1.row().height(30);
        table1.add().width(V_GAMEWIDTH_PORTRAIT);
        table1.row().height(1);
        table1.add().width(V_GAMEWIDTH_PORTRAIT);
        table1.row().height(1);
        table1.add(imgLine).width(V_GAMEWIDTH_PORTRAIT);
        table1.row().height(10);
        table1.add().width(V_GAMEWIDTH_PORTRAIT);

        Table table3 = new Table();
        Label lblSound = new Label("Sounds", skin);
        Label lblSoundInfo = new Label("Select-/deselect to enable-/disable sounds!", skin);
        ckBoxSound = new CheckBox("", skin);
        ckBoxSound.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                soundEnabled = ckBoxSound.isChecked();

                assets.setSoundEnabled(soundEnabled);
            }
        });
        ckBoxSound.setChecked(soundEnabled);
        lblSoundInfo.setWrap(true);
        table3.add(lblSound).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table3.add().width(V_GAMEWIDTH_PORTRAIT * 0.60f);
        table3.add().width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table3.row();
        table3.add(lblSoundInfo).width(V_GAMEWIDTH_PORTRAIT * 0.80f).colspan(2);
        table3.add(ckBoxSound).width(V_GAMEWIDTH_PORTRAIT * 0.20f).align(Align.center);
        table3.row().height(10);
        table3.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table3.row().height(1);
        table3.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table3.row().height(10);
        table3.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table4 = new Table();
        Label lblShowMoves = new Label("Show moves", skin);
        Label lblShowMovesInfo = new Label("Select-/deselect to show the amount of moves!", skin);
        lblShowMovesInfo.setWrap(true);
        ckBoxMoves = new CheckBox("", skin);
        ckBoxMoves.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                showMoves = ckBoxMoves.isChecked();

                assets.setShowMoves(showMoves);
            }
        });
        ckBoxMoves.setChecked(showMoves);
        table4.add(lblShowMoves).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table4.add().width(V_GAMEWIDTH_PORTRAIT * 0.60f);
        table4.add().width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table4.row();
        table4.add(lblShowMovesInfo).width(V_GAMEWIDTH_PORTRAIT * 0.80f).colspan(2);
        table4.add(ckBoxMoves).width(V_GAMEWIDTH_PORTRAIT * 0.20f).align(Align.center);
        table4.row().height(10);
        table4.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table4.row().height(1);
        table4.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table4.row().height(10);
        table4.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table5 = new Table();
        Label lblShowTime = new Label("Show time", skin);
        Label lblShowTimeInfo = new Label("Select-/deselect to show the current gametime!", skin);
        lblShowTimeInfo.setWrap(true);
        ckBoxTime = new CheckBox("", skin);
        ckBoxTime.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                showTime = ckBoxTime.isChecked();

                assets.setShowTime(showTime);
            }
        });
        ckBoxTime.setChecked(showTime);
        table5.add(lblShowTime).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table5.add().width(V_GAMEWIDTH_PORTRAIT * 0.60f);
        table5.add().width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table5.row();
        table5.add(lblShowTimeInfo).width(V_GAMEWIDTH_PORTRAIT * 0.80f).colspan(2);
        table5.add(ckBoxTime).width(V_GAMEWIDTH_PORTRAIT * 0.20f).align(Align.center);
        table5.row().height(10);
        table5.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table5.row().height(1);
        table5.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table5.row().height(10);
        table5.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table6 = new Table();
        Label lblShowScore = new Label("Show Score", skin);
        Label lblShowScoreInfo = new Label("Select-/deselect to show the current score!", skin);
        lblShowScoreInfo.setWrap(true);
        ckBoxScore = new CheckBox("", skin);
        ckBoxScore.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                showScore = ckBoxScore.isChecked();

                assets.setShowScore(showScore);
            }
        });
        ckBoxScore.setChecked(showScore);
        table6.add(lblShowScore).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table6.add().width(V_GAMEWIDTH_PORTRAIT * 0.60f);
        table6.add().width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table6.row();
        table6.add(lblShowScoreInfo).width(V_GAMEWIDTH_PORTRAIT * 0.80f).colspan(2);
        table6.add(ckBoxScore).width(V_GAMEWIDTH_PORTRAIT * 0.20f).align(Align.center);
        table6.row().height(10);
        table6.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table6.row().height(1);
        table6.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table6.row().height(10);
        table6.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table7 = new Table();
        Label lblVegasScoring = new Label("Vegas Scoring", skin);
        Label lblVegasScoringInfo = new Label("Select-/deselect to enable-/disable Vegas-scoring!", skin);
        lblVegasScoringInfo.setWrap(true);
        ckBoxVegasScoring = new CheckBox("", skin);
        ckBoxVegasScoring.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                vegasScoring = ckBoxVegasScoring.isChecked();

                assets.setVegasScoring(vegasScoring);
            }
        });
        ckBoxVegasScoring.setChecked(vegasScoring);
        table7.add(lblVegasScoring).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table7.add().width(V_GAMEWIDTH_PORTRAIT * 0.60f);
        table7.add().width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table7.row();
        table7.add(lblVegasScoringInfo).width(V_GAMEWIDTH_PORTRAIT * 0.80f).colspan(2);
        table7.add(ckBoxVegasScoring).width(V_GAMEWIDTH_PORTRAIT * 0.20f).align(Align.center);
        table7.row().height(10);
        table7.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table7.row().height(1);
        table7.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table7.row().height(10);
        table7.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table8 = new Table();
        Label lblDrawThree = new Label("Draw Three", skin);
        Label lblDrawThreeInfo = new Label("Select-/deselect to enable-/disable draw three mode!", skin);
        lblDrawThreeInfo.setWrap(true);
        ckBoxDrawThree = new CheckBox("", skin);
        ckBoxDrawThree.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                drawThree = ckBoxDrawThree.isChecked();

                assets.setDrawThree(drawThree);
            }
        });
        ckBoxDrawThree.setChecked(drawThree);
        table8.add(lblDrawThree).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table8.add().width(V_GAMEWIDTH_PORTRAIT * 0.60f);
        table8.add().width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table8.row();
        table8.add(lblDrawThreeInfo).width(V_GAMEWIDTH_PORTRAIT * 0.80f).colspan(2);
        table8.add(ckBoxDrawThree).width(V_GAMEWIDTH_PORTRAIT * 0.20f).align(Align.center);
        table8.row().height(10);
        table8.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table8.row().height(1);
        table8.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(3);
        table8.row().height(10);
        table8.add().width(V_GAMEWIDTH_PORTRAIT).colspan(3);

        Table table9 = new Table();
        Label lblCardBack = new Label("Card Back", skin);
        Label lblCardBackInfo = new Label("Change the back of your cards!", skin);
        lblCardBackInfo.setWrap(true);
        table9.add(lblCardBack).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table9.add().width(V_GAMEWIDTH_PORTRAIT * 0.80f);
        table9.row();
        table9.add(lblCardBackInfo).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table9.row().height(10);
        table9.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table9.row().height(1);
        table9.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table9.row().height(10);
        table9.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);

        final Dialog dlgCardBack = new Dialog("Select a card back!", skin);
        TextureRegion region = new TextureRegion(assets.img_CardBack[0]);
        region.flip(false, true);
        final Image imgCardBack1 = new Image(region);
        ckBoxBack1 = new CheckBox("", skin);
        ckBoxBack1.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if(ckBoxBack1.isChecked()) { assets.BACK_CARD_ID = 0; }

                assets.setBackCardID(assets.BACK_CARD_ID);
            }
        });
        ckBoxBack1.setChecked(true);
        ckBoxBack1.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        region = new TextureRegion(assets.img_CardBack[1]);
        region.flip(false, true);
        final Image imgCardBack2 = new Image(region);
        ckBoxBack2 = new CheckBox("", skin);
        ckBoxBack2.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if(ckBoxBack2.isChecked()) { assets.BACK_CARD_ID = 1; }

                assets.setBackCardID(assets.BACK_CARD_ID);
            }
        });
        ckBoxBack2.setChecked(false);
        ckBoxBack2.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        region = new TextureRegion(assets.img_CardBack[2]);
        region.flip(false, true);
        final Image imgCardBack3 = new Image(region);
        ckBoxBack3 = new CheckBox("", skin);
        ckBoxBack3.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxBack3.isChecked()) { assets.BACK_CARD_ID = 2; }

                assets.setBackCardID(assets.BACK_CARD_ID);
            }
        });
        ckBoxBack3.setChecked(false);
        ckBoxBack3.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);

        region = new TextureRegion(assets.img_CardBack[3]);
        region.flip(false, true);
        final Image imgCardBack4 = new Image(region);
        ckBoxBack4 = new CheckBox("", skin);
        ckBoxBack4.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxBack4.isChecked()) { assets.BACK_CARD_ID = 3; }

                assets.setBackCardID(assets.BACK_CARD_ID);
            }
        });
        ckBoxBack4.setChecked(false);
        ckBoxBack4.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);

        final TextButton btnExitDialogBack = new TextButton("Ok", skin);
        btnExitDialogBack.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgCardBack.hide();
            }
        });

        Table dlgContentTable = new Table().padTop(V_GAMEWIDTH_PORTRAIT*0.18f);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable.add(imgCardBack1).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable.add(ckBoxBack1).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable.row().height(10);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable.row();
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable.add(imgCardBack2).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable.add(ckBoxBack2).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable.row().height(10);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable.row();
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable.add(imgCardBack3).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable.add(ckBoxBack3).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable.row().height(10);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable.row();
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable.add(imgCardBack4).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable.add(ckBoxBack4).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable.row().height(10);
        dlgContentTable.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable.row();

        ScrollPane dlgScroller = new ScrollPane(dlgContentTable);

        Table tablex = new Table();
        tablex.setFillParent(true);
        tablex.add(dlgScroller).fill().expand();

        dlgCardBack.getContentTable().add(tablex);
        dlgCardBack.getContentTable().row();
        dlgCardBack.getContentTable().add(btnExitDialogBack).size(V_GAMEWIDTH_PORTRAIT * 0.63f, V_GAMEWIDTH_PORTRAIT * 0.14f);

        dlgScroller.setOverscroll(false, false);

        table9.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgCardBack.show(stage);

                dlgCardBack.setBounds((V_GAMEWIDTH_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.65f) * 0.5f, (V_GAMEHEIGHT_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.80f) * 0.5f, V_GAMEWIDTH_PORTRAIT * 0.65f, V_GAMEWIDTH_PORTRAIT * 0.80f);
            }
        });

        Table table10 = new Table();
        Label lblCardFace = new Label("Card Face", skin);
        Label lblCardFaceInfo = new Label("Change the face of your cards!", skin);
        lblCardFaceInfo.setWrap(true);
        table10.add(lblCardFace).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table10.add().width(V_GAMEWIDTH_PORTRAIT * 0.80f);
        table10.row();
        table10.add(lblCardFaceInfo).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table10.row().height(10);
        table10.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table10.row().height(1);
        table10.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table10.row().height(10);
        table10.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);

        final Dialog dlgCardFace = new Dialog("Select a card face!", skin);
        region = new TextureRegion(assets.img_Cards.get(50));
        region.flip(false, true);
        final Image imgCardFace1 = new Image(region);
        ckBoxFace1 = new CheckBox("", skin);
        ckBoxFace1.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxFace1.isChecked()) {
                    assets.FACE_CARD_ID = 0;
                }

                assets.setFaceCardID(assets.FACE_CARD_ID);
            }
        });
        ckBoxFace1.setChecked(true);
        ckBoxFace1.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        region = new TextureRegion(assets.img_Cards.get(100));
        region.flip(false, true);
        final Image imgCardFace2 = new Image(region);
        ckBoxFace2 = new CheckBox("", skin);
        ckBoxFace2.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if(ckBoxFace2.isChecked()) { assets.FACE_CARD_ID = 1; }

                assets.setFaceCardID(assets.FACE_CARD_ID);
            }
        });
        ckBoxFace2.setChecked(false);
        ckBoxFace2.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        final TextButton btnExitDialogFace = new TextButton("Ok", skin);
        btnExitDialogFace.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgCardFace.hide();
            }
        });

        Table dlgContentTable2 = new Table().padTop(V_GAMEWIDTH_PORTRAIT*0.18f);
        dlgContentTable2.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable2.add(imgCardFace1).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable2.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable2.add(ckBoxFace1).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable2.row().height(10);
        dlgContentTable2.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable2.row();
        dlgContentTable2.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable2.add(imgCardFace2).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable2.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable2.add(ckBoxFace2).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable2.row().height(10);
        dlgContentTable2.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable2.row();

        ScrollPane dlgScroller2 = new ScrollPane(dlgContentTable2);

        Table tablex2 = new Table();
        tablex2.setFillParent(true);
        tablex2.add(dlgScroller2).fill().expand();

        dlgCardFace.getContentTable().add(tablex2);
        dlgCardFace.getContentTable().row();
        dlgCardFace.getContentTable().add(btnExitDialogFace).size(V_GAMEWIDTH_PORTRAIT * 0.63f, V_GAMEWIDTH_PORTRAIT * 0.14f);

        dlgScroller2.setOverscroll(false, false);

        table10.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgCardFace.show(stage);

                dlgCardFace.setBounds((V_GAMEWIDTH_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.65f) * 0.5f, (V_GAMEHEIGHT_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.80f) * 0.5f, V_GAMEWIDTH_PORTRAIT * 0.65f, V_GAMEWIDTH_PORTRAIT * 0.80f);
            }
        });

        Table table11 = new Table();
        Label lblBackground = new Label("Background", skin);
        Label lblBackgroundInfo = new Label("Change the background of the tableau!", skin);
        lblBackgroundInfo.setWrap(true);
        table11.add(lblBackground).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table11.add().width(V_GAMEWIDTH_PORTRAIT * 0.80f);
        table11.row();
        table11.add(lblBackgroundInfo).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table11.row().height(10);
        table11.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table11.row().height(1);
        table11.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table11.row().height(10);
        table11.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);

        final Dialog dlgBackground = new Dialog("Select a background!", skin);
        region = new TextureRegion(assets.img_Background[0]);
        region.flip(false, true);
        final Image imgBackground1 = new Image(region);
        ckBoxBackground1 = new CheckBox("", skin);
        ckBoxBackground1.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxBackground1.isChecked()) {
                    assets.BACKGROUND_ID = 0;
                }

                assets.setBackgroundID(assets.BACKGROUND_ID);
            }
        });
        ckBoxBackground1.setChecked(true);
        ckBoxBackground1.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        region = new TextureRegion(assets.img_Background[1]);
        region.flip(false, true);
        final Image imgBackground2 = new Image(region);
        ckBoxBackground2 = new CheckBox("", skin);
        ckBoxBackground2.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxBackground2.isChecked()) {
                    assets.BACKGROUND_ID = 1;
                }

                assets.setBackgroundID(assets.BACKGROUND_ID);
            }
        });
        ckBoxBackground2.setChecked(false);
        ckBoxBackground2.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        region = new TextureRegion(assets.img_Background[2]);
        region.flip(false, true);
        final Image imgBackground3 = new Image(region);
        ckBoxBackground3 = new CheckBox("", skin);
        ckBoxBackground3.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxBackground3.isChecked()) {
                    assets.BACKGROUND_ID = 2;
                }

                assets.setBackgroundID(assets.BACKGROUND_ID);
            }
        });
        ckBoxBackground3.setChecked(false);
        ckBoxBackground3.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);

        region = new TextureRegion(assets.img_Background[3]);
        region.flip(false, true);
        final Image imgBackground4 = new Image(region);
        ckBoxBackground4 = new CheckBox("", skin);
        ckBoxBackground4.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxBackground4.isChecked()) {
                    assets.BACKGROUND_ID = 3;
                }

                assets.setBackgroundID(assets.BACKGROUND_ID);
            }
        });
        ckBoxBackground4.setChecked(false);
        ckBoxBackground4.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);

        final TextButton btnExitDialogBackround = new TextButton("Ok", skin);
        btnExitDialogBackround.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgBackground.hide();
            }
        });

        Table dlgContentTable3 = new Table().padTop(V_GAMEWIDTH_PORTRAIT*0.18f);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable3.add(imgBackground1).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable3.add(ckBoxBackground1).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable3.row().height(10);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable3.row();
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable3.add(imgBackground2).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable3.add(ckBoxBackground2).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable3.row().height(10);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable3.row();
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable3.add(imgBackground3).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable3.add(ckBoxBackground3).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable3.row().height(10);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable3.row();
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable3.add(imgBackground4).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable3.add(ckBoxBackground4).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable3.row().height(10);
        dlgContentTable3.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable3.row();

        ScrollPane dlgScroller3 = new ScrollPane(dlgContentTable3);

        Table tablex3 = new Table();
        tablex3.setFillParent(true);
        tablex3.add(dlgScroller3).fill().expand();

        dlgBackground.getContentTable().add(tablex3);
        dlgBackground.getContentTable().row();
        dlgBackground.getContentTable().add(btnExitDialogBackround).size(V_GAMEWIDTH_PORTRAIT * 0.63f, V_GAMEWIDTH_PORTRAIT * 0.14f);

        dlgScroller3.setOverscroll(false, false);

        table11.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgBackground.show(stage);

                dlgBackground.setBounds((V_GAMEWIDTH_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.65f) * 0.5f, (V_GAMEHEIGHT_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.80f) * 0.5f, V_GAMEWIDTH_PORTRAIT * 0.65f, V_GAMEWIDTH_PORTRAIT * 0.80f);
            }
        });

        Table table12 = new Table(); // TODO
        Label lblOrientation = new Label("Orientation", skin);
        Label lblOrientationInfo = new Label("Change the orientation of the game!", skin);
        lblOrientationInfo.setWrap(true);
        table12.add(lblOrientation).width(V_GAMEWIDTH_PORTRAIT * 0.20f);
        table12.add().width(V_GAMEWIDTH_PORTRAIT * 0.80f);
        table12.row();
        table12.add(lblOrientationInfo).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table12.row().height(10);
        table12.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table12.row().height(1);
        table12.add(new Image(line)).width(V_GAMEWIDTH_PORTRAIT).colspan(2);
        table12.row().height(10);
        table12.add().width(V_GAMEWIDTH_PORTRAIT).colspan(2);

        final Dialog dlgOrientation = new Dialog("Select an orientation", skin);
        region = new TextureRegion(assets.img_Phone_Portrait);
        region.flip(false, true);
        final Image imgOrientation1 = new Image(region);
        ckBoxOrientation1 = new CheckBox("", skin);
        ckBoxOrientation1.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (ckBoxOrientation1.isChecked()) { assets.ORIENTATION = 1; orientationChanged = true; assets.setOrientation(assets.ORIENTATION); }
            }
        });
        ckBoxOrientation1.setChecked(true);
        ckBoxOrientation1.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        region = new TextureRegion(assets.img_Phone_Landscape);
        region.flip(false, true);
        final Image imgOrientation2 = new Image(region);
        ckBoxOrientation2 = new CheckBox("", skin);
        ckBoxOrientation2.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if(ckBoxOrientation2.isChecked()) { assets.ORIENTATION = 2; orientationChanged = true; assets.setOrientation(assets.ORIENTATION); }
            }
        });
        ckBoxOrientation2.setChecked(false);
        ckBoxOrientation2.getCells().get(0).size(V_GAMEWIDTH_PORTRAIT * 0.06f, V_GAMEWIDTH_PORTRAIT * 0.06f);
        final TextButton btnExitDialogOrientation = new TextButton("Ok", skin);
        btnExitDialogOrientation.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgOrientation.hide();
            }
        });

        Table dlgContentTable4 = new Table().padTop(V_GAMEWIDTH_PORTRAIT*0.18f);
        dlgContentTable4.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable4.add(imgOrientation1).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable4.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable4.add(ckBoxOrientation1).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable4.row().height(10);
        dlgContentTable4.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable4.row();
        dlgContentTable4.add().width(V_GAMEWIDTH_PORTRAIT * 0.10f);
        dlgContentTable4.add(imgOrientation2).size(V_GAMEWIDTH_PORTRAIT * 0.20f, V_GAMEWIDTH_PORTRAIT * 0.25f);
        dlgContentTable4.add().width(V_GAMEWIDTH_PORTRAIT * 0.05f);
        dlgContentTable4.add(ckBoxOrientation2).width(V_GAMEWIDTH_PORTRAIT * 0.28f);
        dlgContentTable4.row().height(10);
        dlgContentTable4.add().width(V_GAMEWIDTH_PORTRAIT * 0.63f).colspan(4);
        dlgContentTable4.row();

        ScrollPane dlgScroller4 = new ScrollPane(dlgContentTable4);

        Table tablex4 = new Table();
        tablex4.setFillParent(true);
        tablex4.add(dlgScroller4).fill().expand();

        dlgOrientation.getContentTable().add(tablex4);
        dlgOrientation.getContentTable().row();
        dlgOrientation.getContentTable().add(btnExitDialogOrientation).size(V_GAMEWIDTH_PORTRAIT * 0.63f, V_GAMEWIDTH_PORTRAIT * 0.14f);

        dlgScroller4.setOverscroll(false, false);

        table12.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                dlgOrientation.show(stage);

                dlgOrientation.setBounds((V_GAMEWIDTH_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.65f) * 0.5f, (V_GAMEHEIGHT_PORTRAIT - V_GAMEWIDTH_PORTRAIT * 0.80f) * 0.5f, V_GAMEWIDTH_PORTRAIT * 0.65f, V_GAMEWIDTH_PORTRAIT * 0.80f);
            }
        });//TODO

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
        tableEnd.row().height(10);
        tableEnd.add().width(V_GAMEWIDTH_PORTRAIT);

        rootTable.add(table1);
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
        rootTable.add(table12);
        rootTable.row();
        rootTable.add(table11);
        rootTable.row();
        rootTable.add(table9);
        rootTable.row();
        rootTable.add(table10);
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

        soundEnabled = assets.soundEnabled();
        showTime = assets.showTime();
        showScore = assets.showScore();
        showMoves = assets.showMoves();
        vegasScoring = assets.vegasScoring();
        drawThree = assets.drawThree();

        ckBoxSound.setChecked(soundEnabled);
        ckBoxTime.setChecked(showTime);
        ckBoxScore.setChecked(showScore);
        ckBoxMoves.setChecked(showMoves);
        ckBoxVegasScoring.setChecked(vegasScoring);
        ckBoxDrawThree.setChecked(drawThree);

        orientationChanged = false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(assets.ORIENTATION == 1) { ckBoxOrientation1.setChecked(true); ckBoxOrientation2.setChecked(false); }
        else if(assets.ORIENTATION == 2) { ckBoxOrientation1.setChecked(false); ckBoxOrientation2.setChecked(true); }

        if(assets.BACKGROUND_ID == 0) { ckBoxBackground1.setChecked(true); ckBoxBackground2.setChecked(false); ckBoxBackground3.setChecked(false); ckBoxBackground4.setChecked(false); }
        else if(assets.BACKGROUND_ID == 1) { ckBoxBackground1.setChecked(false); ckBoxBackground2.setChecked(true); ckBoxBackground3.setChecked(false); ckBoxBackground4.setChecked(false); }
        else if(assets.BACKGROUND_ID == 2) { ckBoxBackground1.setChecked(false); ckBoxBackground2.setChecked(false); ckBoxBackground3.setChecked(true); ckBoxBackground4.setChecked(false); }
        else if(assets.BACKGROUND_ID == 3) { ckBoxBackground1.setChecked(false); ckBoxBackground2.setChecked(false); ckBoxBackground3.setChecked(false); ckBoxBackground4.setChecked(true); }

        if(assets.BACK_CARD_ID == 0) { ckBoxBack1.setChecked(true); ckBoxBack2.setChecked(false); ckBoxBack3.setChecked(false); ckBoxBack4.setChecked(false); }
        else if(assets.BACK_CARD_ID == 1) { ckBoxBack1.setChecked(false); ckBoxBack2.setChecked(true); ckBoxBack3.setChecked(false); ckBoxBack4.setChecked(false); }
        else if(assets.BACK_CARD_ID == 2) { ckBoxBack1.setChecked(false); ckBoxBack2.setChecked(false); ckBoxBack3.setChecked(true); ckBoxBack4.setChecked(false); }
        else if(assets.BACK_CARD_ID == 3) { ckBoxBack1.setChecked(false); ckBoxBack2.setChecked(false); ckBoxBack3.setChecked(false); ckBoxBack4.setChecked(true); }

        if(assets.FACE_CARD_ID == 0) { ckBoxFace1.setChecked(true); ckBoxFace2.setChecked(false); }
        else if(assets.FACE_CARD_ID == 1) { ckBoxFace1.setChecked(false); ckBoxFace2.setChecked(true); }

        stage.act(delta);
        stage.draw();

        if(showGameScreen == true) { setVariables(); solitaire.setGameScreen(); showGameScreen = false; }
    }

    private void setVariables() {
        assets.SOUND_ENABLED = soundEnabled;
        assets.SHOW_MOVES = showMoves;
        assets.SHOW_TIME = showTime;
        assets.SHOW_SCORE = showScore;
        assets.VEGAS_SCORING = vegasScoring;
        assets.DRAW_THREE = drawThree;
    }

    public void changeOrientation(int newOrientation) {

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
