package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Effect;

public class ComposerEffect implements Effect {
    private final Effect[] effects;
    private final int lengthFixed;
    public ComposerEffect(Effect[] effects) {
        this.effects = effects;
        if(effects == null)
            lengthFixed = 0;
        else
            lengthFixed = effects.length;
    }

    @Override
    public void apply(int x, int y, int width, int height) {
        for(int i=0; i<lengthFixed; i++)
            effects[i].apply(x, y, width, height);
    }
}
