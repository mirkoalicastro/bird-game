package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class SpinAnimation implements Animation {
    private final Graphics graphics;
    private final long delta;
    private Pixmap pixmap;
    private int x, y;
    private final int numFrames;

    public SpinAnimation(Graphics graphics, int numFrames, long delta) {
        this.graphics = graphics;
        this.numFrames = numFrames;
        this.delta = delta;
    }

    public SpinAnimation setX(int x) {
        this.x = x;
        return this;
    }

    public SpinAnimation setY(int y) {
        this.y = y;
        return this;
    }

    public SpinAnimation setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        return this;
    }

    public void draw() {
        if(pixmap != null) {
            int currentFrame = (int)((System.currentTimeMillis() / delta) % numFrames);
            float degrees = (360/numFrames)*currentFrame;
            graphics.drawPixmap(pixmap, x, y, degrees);
        }
    }
}
