package com.mirkoalicastro.birdgame;

import android.graphics.Color;
import android.graphics.Shader;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.impl.AndroidGame;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.LoadingScreen;
import com.badlogic.androidgames.framework.impl.RectangularTileEffect;
import com.badlogic.androidgames.framework.impl.RectangularLinearGradientEffect;

public class StartScreen extends LoadingScreen {

    StartScreen(Game game) {
        super(game);
    }

    @Override
    public void onProgress(int progress) {
        if(true)
            return;
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
        Graphics graphics = game.getGraphics();
        Assets.avatar = graphics.newPixmap("avatar.png", Graphics.PixmapFormat.ARGB8888);
        Assets.backgroundGradient = new RectangularLinearGradientEffect(0,0, 0, graphics.getHeight(), new int[]{BACKGROUND_TOP_COLOR, BACKGROUND_BOTTOM_COLOR}, new float[]{0,1}, Shader.TileMode.REPEAT);
        Assets.obstacle = graphics.newPixmap("obstacle.png", Graphics.PixmapFormat.ARGB8888);
        Assets.obstacleTile = new RectangularTileEffect((AndroidPixmap)Assets.obstacle);
        setProgress(100);
        game.setScreen(new GameScreen(game));
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


    private final static int BACKGROUND_TOP_COLOR = Color.parseColor("#E8FFFF");
    private final static int BACKGROUND_BOTTOM_COLOR = Color.parseColor("#A8D5F4");

}