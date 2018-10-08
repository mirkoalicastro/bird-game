package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

import com.badlogic.androidgames.framework.Effect;

public class ComposerEffect extends AndroidEffect {
    private final AndroidEffect[] effects;
    private final int lengthFixed;
    public ComposerEffect(AndroidEffect[] effects) {
        this.effects = effects;
        if(effects == null)
            lengthFixed = 0;
        else
            lengthFixed = effects.length;
    }

    @Override
    public void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        for(int i=0; i<lengthFixed; i++)
            effects[i].apply(canvas, paint, x, y, width, height);
    }
}
