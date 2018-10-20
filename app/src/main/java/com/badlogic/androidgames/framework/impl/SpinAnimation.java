package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class SpinAnimation implements Animation {
    private final Graphics graphics;
    private final long delta;
    private Pixmap pixmap;
    private final int numFrames;
    private int width, height;

    public SpinAnimation(Graphics graphics, int numFrames, long delta, Pixmap pixmap) {
        this.graphics = graphics;
        this.numFrames = numFrames;
        this.delta = delta;
        this.pixmap = pixmap;
    }

    public SpinAnimation setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        width = pixmap.getWidth();
        height = pixmap.getHeight();
        return this;
    }

    @Override
    public void draw(int x, int y) {
        draw(x,y,width,height);
    }

    @Override
    public void draw(int x, int y, int width, int height) {
        if(pixmap != null) {
            int currentFrame = (int)((System.currentTimeMillis() / delta) % numFrames);
            float degrees = (360/numFrames)*currentFrame;
            graphics.drawPixmap(pixmap, x, y, width, height, degrees);
        }
    }

}
