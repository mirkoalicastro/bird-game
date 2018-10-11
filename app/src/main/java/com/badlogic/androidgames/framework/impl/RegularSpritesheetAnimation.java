package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Animation;
import com.badlogic.androidgames.framework.Graphics;

public class RegularSpritesheetAnimation implements Animation {
    private final AndroidPixmap androidPixmap;
    private final int frameWidth, frameHeight;
    private final int numFrames, cols, rows;
    private final int duration;
    private final Graphics graphics;

    public RegularSpritesheetAnimation(Graphics graphics, AndroidPixmap androidPixmap, int frameWidth, int frameHeight, int duration) {
        this.androidPixmap = androidPixmap;
        this.frameWidth = frameWidth;
        this.frameHeight = frameHeight;
        this.cols = androidPixmap.getHeight()/frameHeight;
        this.rows = androidPixmap.getWidth()/frameWidth;
        this.numFrames = this.rows*cols;
        this.duration = duration;
        this.graphics = graphics;
    }

    @Override
    public void draw(int x, int y) {
        int col = (int) ((System.currentTimeMillis()/duration) % numFrames);
        int row = 0;
        while(col > rows) {
            col -= cols;
            row++;
        }
        graphics.drawPixmap(androidPixmap,x, y, frameWidth, frameHeight,col*frameWidth,row*frameHeight,frameWidth,frameHeight);
    }
}
