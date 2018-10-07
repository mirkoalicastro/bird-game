package com.badlogic.androidgames.framework.impl;

import android.graphics.BitmapShader;
import android.graphics.Shader;

public abstract class AndroidTileEffect extends AndroidEffect {

    public AndroidTileEffect(AndroidPixmap pixmap) {
        shader = new BitmapShader(pixmap.bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    }

}