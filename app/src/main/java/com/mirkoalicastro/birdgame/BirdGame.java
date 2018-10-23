package com.mirkoalicastro.birdgame;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class BirdGame extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
