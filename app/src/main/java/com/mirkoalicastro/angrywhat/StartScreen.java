package com.mirkoalicastro.angrywhat;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Graphics.PixmapFormat;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.LoadingScreen;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.CircularAndroidTileEffect;
import com.badlogic.androidgames.framework.impl.RectangularAndroidTileEffect;

public class StartScreen extends LoadingScreen {

    public StartScreen(Game game) {
        super(game);
    }

    @Override
    public void onProgress(int progress) {
        final Graphics graphics = game.getGraphics();

        int x = (graphics.getWidth()-Assets.loading.getWidth())/2;
        int y = (graphics.getHeight()-Assets.loading.getHeight())/2;
        //x: 13, y: 254, width: 290, height: 19
        graphics.drawPixmap(Assets.loading, x,y);
        int val = 473;
        float vals = (float)progress*(float)val/100f;
        graphics.drawRect((int)vals+12+x,y+12,val-(int)vals,31, Color.BLACK);
        game.display(); //force the canvas rendering :'(
    }

    @Override
    public void update(float deltaTime) {
        setProgress(100);
        game.setScreen(new OneScreen(game));
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void back() {
        ((AndroidGame)game).finish();
    }

}