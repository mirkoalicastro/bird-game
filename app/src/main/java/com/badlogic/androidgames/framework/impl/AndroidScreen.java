package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Screen;

public abstract class AndroidScreen extends Screen {
    protected final AndroidGame androidGame;
    public AndroidScreen(AndroidGame androidGame) {
        super(androidGame);
        this.androidGame = androidGame;
    }
}
