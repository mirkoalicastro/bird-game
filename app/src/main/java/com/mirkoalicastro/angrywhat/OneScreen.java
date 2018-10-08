package com.mirkoalicastro.angrywhat;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;
import com.google.fpl.liquidfun.World;

public class OneScreen extends Screen {
    private final World world;
    OneScreen(Game game) {
        super(game);
        world = new World(0, 9.8f);

    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public void present(float deltaTime) {
        game.getGraphics().drawText("CIAO", 10, 10, 10, Color.BLACK);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void back() {

    }
}
