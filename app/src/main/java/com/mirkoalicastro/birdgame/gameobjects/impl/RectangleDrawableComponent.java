package com.mirkoalicastro.birdgame.gameobjects.impl;

import com.badlogic.androidgames.framework.Graphics;

public class RectangleDrawableComponent extends DrawableComponent {
    public RectangleDrawableComponent(Graphics graphics){ super(graphics); }

    @Override
    public void draw() {
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawRectBorder(x,y,width,height,strokeWidth, strokeColor);
        if(color != null)
            graphics.drawRect(x, y, width, height, color);
        if(effect != null)
            graphics.drawEffect(effect, x, y, width, height);
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x-width, y-height, width,height);
    }
}
