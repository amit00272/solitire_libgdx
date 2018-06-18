package com.yourcompany.solitaire.Screens;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.yourcompany.solitaire.Helper.CustomAssetManager;
import com.yourcompany.solitaire.UI.CenteredText;
import com.yourcompany.solitaire.UI.SimpleButton;

public class StatisticsScreen {

    public CenteredText txtStrStatistics;
    public CenteredText txtBestScore;
    public CenteredText txtBestTime;
    public CenteredText txtTotalTime;
    public CenteredText txtGamesWon;
    public CenteredText txtTotalGames;
    public CenteredText txtCurrentStreak;
    public SimpleButton btnClose;
    private float bgX;
    private float bgY;
    private float bgWidth;
    private float bgHeight;

    public CustomAssetManager assets;

    public boolean isShowing;

    public StatisticsScreen(CustomAssetManager assets) {
        this.assets = assets;

        txtStrStatistics = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100, 200, 50, assets.fntStrStatistics);
        txtStrStatistics.setText("Statistics");

        txtBestScore = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100 + 50, 200, 30, assets.fntStatisticsEntrys);
        txtBestScore.setText("Best score: 2393");

        txtBestTime = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100 + 50 + 30, 200, 30, assets.fntStatisticsEntrys);
        txtBestTime.setText("Best time: 2:30");

        txtTotalTime = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100 + 50 + 30 + 30, 200, 30, assets.fntStatisticsEntrys);
        txtTotalTime.setText("Total time: 28:49");

        txtGamesWon = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100 + 50 + 30 + 30 + 30, 200, 30, assets.fntStatisticsEntrys);
        txtGamesWon.setText("Games won: 21 (37%)");

        txtTotalGames = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100 + 50 + 30 + 30 + 30 + 30, 200, 30, assets.fntStatisticsEntrys);
        txtTotalGames.setText("Total games: 191");

        txtCurrentStreak = new CenteredText(assets, (assets.V_GAMEWIDTH - 200) * 0.5f, 0 + 100 + 50 + 30 + 30 + 30 + 30 + 30, 200, 30, assets.fntStatisticsEntrys);
        txtCurrentStreak.setText("Streak lenght: 10");

        btnClose = new SimpleButton(new Sprite(assets.UI_BTN_StatisticsClose), (assets.V_GAMEWIDTH - 100) * 0.5f, 0 + 100 + 50 + 50 + 50 + 50 + 50, 100, 50);

        bgWidth = 250;
        bgX = (assets.V_GAMEWIDTH - bgWidth) * 0.5f;
        bgHeight = 340;
        bgY = 100-20;

        isShowing = false;
    }

    public void draw(SpriteBatch batch) {

        if(isShowing) {
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 0.4f);
            batch.draw(assets.UI_Black, 0, 0, assets.V_GAMEWIDTH, assets.V_GAMEHEIGHT);
            batch.setColor(batch.getColor().r, batch.getColor().g, batch.getColor().b, 1f);

            batch.draw(assets.UI_StatisticsBackground, bgX, bgY, bgWidth, bgHeight);

            txtStrStatistics.draw(batch);

            txtBestScore.draw(batch);

            txtBestTime.draw(batch);

            txtTotalTime.draw(batch);

            txtGamesWon.draw(batch);

            txtTotalGames.draw(batch);

            txtCurrentStreak.draw(batch);

            btnClose.draw(batch);
        }
    }

    public void changeOrientation(int orientation) {
        if(orientation == 1) {
            txtStrStatistics.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtStrStatistics.setY(0 + 100);
            txtStrStatistics.setWidth(200);
            txtStrStatistics.setHeight(50);

            txtBestScore.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtBestScore.setY(0 + 100 + 50);
            txtBestScore.setWidth(200);
            txtBestScore.setHeight(30);

            txtBestTime.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtBestTime.setY(0 + 100 + 50 + 30);
            txtBestTime.setWidth(200);
            txtBestTime.setHeight(30);

            txtTotalTime.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtTotalTime.setY(0 + 100 + 50 + 30 + 30);
            txtTotalTime.setWidth(200);
            txtTotalTime.setHeight(30);

            txtGamesWon.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtGamesWon.setY(0 + 100 + 50 + 30 + 30 + 30);
            txtGamesWon.setWidth(200);
            txtGamesWon.setHeight(30);

            txtTotalGames.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtTotalGames.setY(0 + 100 + 50 + 30 + 30 + 30 + 30);
            txtTotalGames.setWidth(200);
            txtTotalGames.setHeight(30);

            txtCurrentStreak.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtCurrentStreak.setY(0 + 100 + 50 + 30 + 30 + 30 + 30 + 30);
            txtCurrentStreak.setWidth(200);
            txtCurrentStreak.setHeight(30);

            btnClose.setX((assets.V_GAMEWIDTH - 100) * 0.5f);
            btnClose.setY(0 + 100 + 50 + 50 + 50 + 50 + 50);
            btnClose.setWidth(100);
            btnClose.setHeight(50);

            bgWidth = 250;
            bgX = (assets.V_GAMEWIDTH - bgWidth) * 0.5f;
            bgHeight = 340;
            bgY = 100-20;
        } else {
            txtStrStatistics.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtStrStatistics.setY(0 + 40);
            txtStrStatistics.setWidth(200);
            txtStrStatistics.setHeight(50);

            txtBestScore.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtBestScore.setY(0 + 40 + 40);
            txtBestScore.setWidth(200);
            txtBestScore.setHeight(30);

            txtBestTime.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtBestTime.setY(0 + 40 + 40 + 30);
            txtBestTime.setWidth(200);
            txtBestTime.setHeight(30);

            txtTotalTime.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtTotalTime.setY(0 + 40 + 40 + 30 + 30);
            txtTotalTime.setWidth(200);
            txtTotalTime.setHeight(30);

            txtGamesWon.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtGamesWon.setY(0 + 40 + 40 + 30 + 30 + 30);
            txtGamesWon.setWidth(200);
            txtGamesWon.setHeight(30);

            txtTotalGames.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtTotalGames.setY(0 + 40 + 40 + 30 + 30 + 30 + 30);
            txtTotalGames.setWidth(200);
            txtTotalGames.setHeight(30);

            txtCurrentStreak.setX((assets.V_GAMEWIDTH - 200) * 0.5f);
            txtCurrentStreak.setY(0 + 40 + 40 + 30 + 30 + 30 + 30 + 30);
            txtCurrentStreak.setWidth(200);
            txtCurrentStreak.setHeight(30);

            btnClose.setX((assets.V_GAMEWIDTH - 55) * 0.5f);
            btnClose.setY(0 + 40 + 40 + 30 + 30 + 30 + 30 + 30 + 35);
            btnClose.setWidth(55);
            btnClose.setHeight(45);

            bgWidth = 80;
            bgX = (assets.V_GAMEWIDTH - bgWidth) * 0.5f;
            bgHeight = 300;
            bgY = 50-20;
        }
    }

    public void setTextBestScore(int score) {
        if(score == 0) {
            txtBestScore.setText("Best score: none");
        } else {
            txtBestScore.setText("Best score: " + score);
        }
    }

    public void setStreakLenght(int val) {
        txtCurrentStreak.setText("Streak Lenght: " + val);
    }

    public void setTotalGames(int val) {
        txtTotalGames.setText("Total games: " + val);
    }

    public void setGamesWon(int totalgames, int gameswon) {
        if(totalgames > 0) {
            int percentage = (int) ( ( ( float) gameswon / (float) totalgames) * 100f);

            txtGamesWon.setText("Games won: "+ gameswon + " (" + percentage + "%)");
        } else {
            txtGamesWon.setText("Games won: "+ gameswon );
        }

    }

    public void setTextBestTime(int AllSeconds) {
        if(AllSeconds == 99999) {
            txtBestTime.setText("Best time: none");
        } else {

            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            if(AllSeconds < 60) {
                txtBestTime.setText("Total time: 0:" + AllSeconds);
            }

            if(AllSeconds >= 60) {
                hours = AllSeconds / 3600;

                AllSeconds -= hours * 3600;

                minutes = AllSeconds / 60;

                AllSeconds -= minutes * 60;

                seconds = AllSeconds;

                String time = "Best time: ";

                if(hours > 0) {
                    time += hours + ":";
                } else {
                    time += "0:";
                }

                if(minutes > 0) {
                    if(minutes >= 10) {
                        time += minutes + ":";
                    } else {
                        time += "0" + minutes + ":";
                    }
                } else {
                    time += "00:";
                }

                if(seconds > 0) {
                    if(seconds >= 10) {
                        time += seconds + "";
                    } else {
                        time += "0" + seconds;
                    }
                } else {
                    time += "00";
                }

                txtBestTime.setText(time);
            }
        }
    }

    public void setTextTotalTime(int AllSeconds) {
        if(AllSeconds == 0) {
            txtTotalTime.setText("Total time: none");
        } else {

            int hours = 0;
            int minutes = 0;
            int seconds = 0;

            if(AllSeconds < 60) {
                txtTotalTime.setText("Total time: 0:" + AllSeconds);
            }

            if(AllSeconds >= 60) {
                hours = AllSeconds / 3600;

                AllSeconds -= hours * 3600;

                minutes = AllSeconds / 60;

                AllSeconds -= minutes * 60;

                seconds = AllSeconds;

                String time = "Total time: ";

                if(hours > 0) {
                    time += hours + ":";
                } else {
                    time += "0:";
                }

                if(minutes > 0) {
                    if(minutes >= 10) {
                        time += minutes + ":";
                    } else {
                        time += "0" + minutes + ":";
                    }
                } else {
                    time += "00:";
                }

                if(seconds > 0) {
                    if(seconds >= 10) {
                        time += seconds + "";
                    } else {
                        time += "0" + seconds;
                    }
                } else {
                    time += "00";
                }

                txtTotalTime.setText(time);
            }
        }
    }

    public void show() {
        isShowing = true;
    }

    public void hide() {
        isShowing = false;
    }
}