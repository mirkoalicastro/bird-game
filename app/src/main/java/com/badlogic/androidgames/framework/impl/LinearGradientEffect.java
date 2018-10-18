package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public abstract class LinearGradientEffect extends AndroidEffect {
    protected final Shader shader;

    public LinearGradientEffect(float x0, float y0, float x1, float y1, int[] colors, float[] stops, Shader.TileMode tileMode) {
        shader = new LinearGradient(x0, y0, x1, y1, colors, stops, tileMode);
    }
}