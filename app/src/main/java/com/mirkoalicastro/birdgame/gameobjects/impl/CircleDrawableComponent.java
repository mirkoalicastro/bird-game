package com.mirkoalicastro.birdgame.gameobjects.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class CircleDrawableComponent extends DrawableComponent {
    protected int radius;

    public CircleDrawableComponent(Graphics graphics){ super(graphics); }

    @Override
    public void draw() {
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawCircleBorder(x,y,radius,strokeWidth,strokeColor);
        if(color != null)
            graphics.drawCircle(x, y, radius, color);
        if(effect != null)
            graphics.drawEffect(effect, x, y, width, height);
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x-radius, y-radius, width,height);
    }

    @Override
    public DrawableComponent setWidth(int width) {
        return setRadius(width/2);
    }

    @Override
    public DrawableComponent setHeight(int height) {
        return setRadius(height/2);
    }

    public DrawableComponent setRadius(int radius) {
        this.radius = radius;
        width = radius*2;
        height = radius*2;
        return this;
    }
}
