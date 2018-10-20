package com.badlogic.androidgames.framework.impl;

import android.util.Log;

import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;

public class RegularSpritesheetAnimation implements Animation {
    private AndroidPixmap androidPixmap;
    private int frameWidth, frameHeight;
    private int numFrames, cols, rows;
    private int stoppedAt = -1;
    private int duration;
    private final Graphics graphics;

    public RegularSpritesheetAnimation(Graphics graphics) {
        this.graphics = graphics;
    }

    public void setAndroidPixmap(AndroidPixmap androidPixmap) {
        this.androidPixmap = androidPixmap;
        if(frameHeight > 0)
            this.cols = androidPixmap.getHeight() / frameHeight;
        if(frameWidth > 0)
            this.rows = androidPixmap.getWidth() / frameWidth;
        this.numFrames = this.rows * cols;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFrameHeight(int frameHeight) {
        this.frameHeight = frameHeight;
        if(androidPixmap != null) {
            this.cols = androidPixmap.getHeight() / frameHeight;
            this.numFrames = this.rows * cols;
        }
    }

    public void setFrameWidth(int frameWidth) {
        this.frameWidth = frameWidth;
        if(androidPixmap != null) {
            this.rows = androidPixmap.getWidth() / frameWidth;
            this.numFrames = this.rows * cols;
        }
    }

    @Override
    public void draw(int x, int y) {
        draw(x,y,frameWidth,frameHeight);
    }

    @Override
    public void draw(int x, int y, int width, int height) {
        long val = stoppedAt > -1 ? stoppedAt : System.currentTimeMillis()/duration;
        drawFrame(x,y,width,height,val);
    }

    private void drawFrame(int x, int y, int width, int height, long val) {
        if (androidPixmap != null && numFrames > 0) {
            int col = (int) (val % numFrames);
            int row = 0;
            while (col > rows) {
                col -= cols;
                row++;
            }
            graphics.drawPixmap(androidPixmap, x, y, width, height, col * frameWidth, row * frameHeight, frameWidth, frameHeight);
        }
    }

    public void stopAt(int numFrame) {
        if (numFrame < 0)
            throw new IllegalArgumentException("Frame number can not be lower than 0");
        stoppedAt = numFrame;
    }
    public void play() {
        stoppedAt = -1;
    }
}
