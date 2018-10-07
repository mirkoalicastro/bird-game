package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public abstract class AndroidButton implements Button {
    protected int x, y, width, height;
    protected final Graphics graphics;
    private boolean enabled = true;
    protected Integer color;
    protected Pixmap pixmap;
    protected int strokeWidth;
    protected Integer strokeColor;
    protected Effect effect;

    /**
     * @param graphics to be used for draw calls
     * @param x position of the top-left corner of the button
     * @param y position of the top-left corner of the button
     * @param width of the button
     * @param height of the button
     */
    public AndroidButton(Graphics graphics, int x, int y, int width, int height) {
        this.graphics = graphics;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * @param effect to be used for draw calls
     * @return the <i>this</i> instance
     */
    public AndroidButton setEffect(Effect effect) {
        this.effect = effect;
        return this;
    }

    /**
     * @return the effect of the button, if there is one; else returns null
     */
    public Effect getEffect() {
        return effect;
    }

    /**
     * @param x of the top-left corner of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setX(int x) {
        this.x = x;
        return this;
    }

    /**
     * @param y of the top-left corner of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setY(int y) {
        this.y = y;
        return this;
    }

    /**
     * @param width of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * @param height of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * @param color of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setColor(Integer color) {
        this.color = color;
        return this;
    }

    /**
     * @return the color of the button, if there is one; else returns null
     */
    public Integer getColor() {
        return color;
    }

    /**
     * @param pixmap of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        return this;
    }

    /**
     * @return the pixmap of the button, if there is one; else returns null
     */
    public Pixmap getPixmap() {
        return pixmap;
    }

    /**
     * @param strokeWidth of the button
     * @param strokeColor of the button
     * @return the <i>this</i> instance
     */
    public AndroidButton setStroke(int strokeWidth, Integer strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        return this;
    }

    /**
     * @return the stroke width of the button
     */
    public int getStrokeWidth() {
        return strokeWidth;
    }

    /**
     * @return the stroke color of the button, if there is one; else returns null
     */
    public Integer getStrokeColor() {
        return strokeColor;
    }

    /**
     * @return true if the button is enabled; false otherwise
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param event of the touch
     * @return true if the event concerns the button; false otherwise
     */
    public abstract boolean inBounds(Input.TouchEvent event);

    /**
     * @return the width of the button
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * @return the height of the button
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * @return the x of the top-left corner of the button
     */
    @Override
    public int getX() {
        return x;
    }

    /**
     * @return the y of the top-left corner of the button
     */
    @Override
    public int getY() {
        return y;
    }

    /**
     * @param enabled set to true or false the enabled state of the button
     */
    @Override
    public void enable(boolean enabled) {
        this.enabled = enabled;
    }

}
