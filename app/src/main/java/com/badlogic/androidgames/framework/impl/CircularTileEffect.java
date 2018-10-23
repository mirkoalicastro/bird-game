package com.badlogic.androidgames.framework.impl;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircularTileEffect extends AndroidTileEffect {

    public CircularTileEffect(AndroidPixmap pixmap) {
        super(pixmap);
    }

    @Override
    void apply(Canvas canvas, Paint paint, int x, int y, int width, int height) {
        paint.setShader(shader);
        canvas.drawCircle(x, y, width/2, paint);
        paint.setShader(null);
    }
}
