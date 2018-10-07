package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

public class RadialGradientEffect extends AndroidEffect {
    public RadialGradientEffect(float centerX, float centerY, float radius, int[] colors, float[] stops, Shader.TileMode tileMode) {
        shader = new RadialGradient(centerX, centerY, radius, colors, stops, tileMode);
    }

    @Override
    void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        paint.setShader(shader);
        canvas.drawCircle(x,y,width/2,paint);
        paint.setShader(null);
    }
}
