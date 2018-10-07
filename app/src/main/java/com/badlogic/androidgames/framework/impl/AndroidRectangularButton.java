package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;

public class AndroidRectangularButton extends AndroidButton {

    public AndroidRectangularButton(Graphics graphics, int x, int y, int width, int height) {
        super(graphics, x, y, width, height);
    }

    @Override
    public boolean inBounds(Input.TouchEvent event) {
        return (event.x > x && event.x < x + width - 1 && event.y > y && event.y < y + height - 1);
    }

    @Override
    public void draw() {
        if(color != null)
            graphics.drawRect(x, y, width, height, color);
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x, y, width, height);
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawRectBorder(x, y, width, height, strokeWidth, strokeColor);
        if(effect != null)
            graphics.drawEffect(effect, x, y, width, height);
    }
}