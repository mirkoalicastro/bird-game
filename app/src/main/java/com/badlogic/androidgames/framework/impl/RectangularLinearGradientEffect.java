package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;

public class RectangularLinearGradientEffect extends LinearGradientEffect {

    public RectangularLinearGradientEffect(float x0, float y0, float x1, float y1, int[] colors, float[] stops, Shader.TileMode tileMode) {
        super(x0, y0, x1, y1, colors, stops, tileMode);
    }

    @Override
    void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        paint.setShader(shader);
        canvas.drawRect(x,y,x+width,y+height,paint);
        paint.setShader(null);
    }
}
