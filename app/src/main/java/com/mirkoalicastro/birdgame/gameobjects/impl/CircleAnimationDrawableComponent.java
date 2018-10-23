package com.mirkoalicastro.birdgame.gameobjects.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.RegularSpritesheetAnimation;

public class CircleAnimationDrawableComponent extends CircleDrawableComponent {
    private final RegularSpritesheetAnimation animation;

    public CircleAnimationDrawableComponent(Graphics graphics) {
        super(graphics);
        animation = new RegularSpritesheetAnimation(graphics);
    }

    @Override
    public CircleAnimationDrawableComponent setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        animation.setAndroidPixmap((AndroidPixmap)pixmap);
        return this;
    }

    public CircleAnimationDrawableComponent setFrameWidth(int frameWidth) {
        animation.setFrameWidth(frameWidth);
        return this;
    }

    public CircleAnimationDrawableComponent setFrameHeight(int frameHeight) {
        animation.setFrameHeight(frameHeight);
        return this;
    }

    public CircleAnimationDrawableComponent setDuration(int duration) {
        animation.setDuration(duration);
        return this;
    }

    public CircleAnimationDrawableComponent stopAt(int numFrame) {
        animation.stopAt(numFrame);
        return this;
    }

    public CircleAnimationDrawableComponent play() {
        animation.play();
        return this;
    }

    @Override
    public void draw() {
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawCircleBorder(x,y,radius,strokeWidth,strokeColor);
        if(color != null)
            graphics.drawCircle(x, y, radius, color);
        if(effect != null)
            graphics.drawEffect(effect, x, y, width, height);
        if(pixmap != null)
            animation.draw(x-width/2,y-height/2,width,height);
    }


}
