package com.mirkoalicastro.angrywhat;

import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidGame;

public class AngryWhatGame extends AndroidGame {
    @Override
    public Screen getStartScreen() {
        return new StartScreen(this);
    }
}
