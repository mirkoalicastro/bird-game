package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;

public class AndroidCircularButton extends AndroidButton {
    protected int radius;

    /**
     * @param graphics to be used for draw calls
     * @param x position of the center of the button
     * @param y position of the center of the button
     * @param radius of the button
     */
    public AndroidCircularButton(Graphics graphics, int x, int y, int radius) {
        super(graphics, x-radius, y-radius, radius*2, radius*2);
        setRadius(radius);
    }

    @Override
    public AndroidCircularButton setX(int x) {
        super.x = x - radius;
        return this;
    }

    @Override
    public AndroidCircularButton setY(int y) {
        super.y = y - radius;
        return this;
    }

    @Override
    public boolean inBounds(Input.TouchEvent event) {
        return Math.pow(event.x-x,2)+ Math.pow(event.y-y,2)< Math.pow(radius,2);
    }

    @Override
    public AndroidCircularButton setWidth(int width) {
        return setRadius(width / 2);
    }

    @Override
    public AndroidCircularButton setHeight(int height) {
        return setRadius(height / 2);
    }

    /**
     * @param radius of the button
     * @return the <i>this</i> instance
     */
    public AndroidCircularButton setRadius(int radius) {
        super.x += this.radius;
        super.y += this.radius;
        this.radius = radius;
        super.width = radius*2;
        super.height = radius*2;
        super.x -= radius;
        super.y -= radius;
        return this;
    }

    /**
     * @return the radius of the button
     */
    public int getRadius() {
        return radius;
    }

    @Override
    public void draw() {
        if(strokeWidth > 0 && strokeColor != null)
            graphics.drawCircleBorder(x, y, radius, strokeWidth, strokeColor);
        if(color != null)
            graphics.drawCircle(x, y, radius, color);
        if(effect != null)
            graphics.drawEffect(effect, x, y, width, height);
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x-radius, y-radius, width, height);
    }

}
