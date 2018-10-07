package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

import com.badlogic.androidgames.framework.Effect;

public abstract class AndroidEffect implements Effect {
    protected Shader shader;
    abstract void apply(Canvas canvas, Paint paint, int x, int y, int width, int height);
}
