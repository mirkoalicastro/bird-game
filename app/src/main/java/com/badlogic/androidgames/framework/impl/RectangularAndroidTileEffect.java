package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectangularAndroidTileEffect extends AndroidTileEffect {

    public RectangularAndroidTileEffect(AndroidPixmap pixmap) {
        super(pixmap);
    }

    @Override
    void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        paint.setShader(shader);
        canvas.drawRect(x, y, x + width, y + height, paint);
        paint.setShader(null);
    }
}