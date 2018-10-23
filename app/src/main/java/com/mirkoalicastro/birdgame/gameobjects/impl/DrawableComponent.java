package com.mirkoalicastro.birdgame.gameobjects.impl;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;
import com.mirkoalicastro.birdgame.gameobjects.Component;

public abstract class DrawableComponent extends Component {
    protected final Graphics graphics;
    protected int x, y;
    protected int width, height;
    protected Pixmap pixmap;
    protected Integer color;
    protected Effect effect;
    protected int strokeWidth;
    protected Integer strokeColor;

    public DrawableComponent(Graphics graphics){
        this.graphics = graphics;
    }

    @Override
    public Component.Type type() { return Component.Type.Drawable; }

    public abstract void draw();

    public DrawableComponent setStroke(int strokeWidth, Integer strokeColor) {
        this.strokeWidth = strokeWidth;
        this.strokeColor = strokeColor;
        return this;
    }

    public DrawableComponent setColor(Integer color) {
        this.color = color;
        return this;
    }

    public DrawableComponent setEffect(Effect effect) {
        this.effect = effect;
        return this;
    }

    public DrawableComponent setPixmap(Pixmap pixmap) {
        this.pixmap = pixmap;
        return this;
    }

    public DrawableComponent setWidth(int width) {
        this.width = width;
        return this;
    }

    public DrawableComponent setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public DrawableComponent setX(int x) {
        this.x = x;
        return this;
    }

    public DrawableComponent setY(int y) {
        this.y = y;
        return this;
    }

    public int getX(){ return x; }
    public int getY(){ return y; }
}