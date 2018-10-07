package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Pixmap;

public class TimedCircularButton extends AndroidCircularButton {
    private long millis;
    private long validTime;
    protected Integer secondaryColor;
    protected Pixmap primaryPixmap, secondaryPixmap;

    public TimedCircularButton(Graphics graphics, int x, int y, int radius, long millis) {
        super(graphics, x, y, radius);
        setMillis(millis);
    }

    @Override
    public TimedCircularButton setPixmap(Pixmap pixmap) {
        super.pixmap = pixmap;
        this.primaryPixmap = pixmap;
        return this;
    }

    public TimedCircularButton setSecondaryColor(Integer secondaryColor) {
        this.secondaryColor = secondaryColor;
        return this;
    }

    public Integer getSecondaryColor() {
        return secondaryColor;
    }

    public TimedCircularButton setSecondaryPixmap(Pixmap secondaryPixmap) {
        this.secondaryPixmap = secondaryPixmap;
        return this;
    }

    public Pixmap getSecondaryPixmap() {
        return secondaryPixmap;
    }

    public TimedCircularButton setMillis(long millis) {
        if(millis < 0)
            throw new IllegalArgumentException("Time cannot be less than zero");
        this.millis = millis;
        return this;
    }

    public void resetTime() {
        validTime = System.currentTimeMillis()+millis;
    }

    @Override
    public boolean isEnabled() {
        return validTime != -1 && System.currentTimeMillis() >= validTime;
    }

    @Override
    public void enable(boolean enabled) {
        if(enabled)
            resetTime();
        else
            validTime = -1;
    }

    private void drawPixmap(Pixmap pixmap) {
        if(pixmap != null)
            graphics.drawPixmap(pixmap, x-radius, y-radius, radius*2,radius*2);
    }

    @Override
    public void draw() {
        super.draw();
        if(secondaryColor != null)
            graphics.drawArc(x-radius, y-radius, x+radius, y+radius, 270, calculateProgress()*360, true, secondaryColor);
        if(isEnabled())
            drawPixmap(primaryPixmap);
        else
            drawPixmap(secondaryPixmap);
    }

    private float calculateProgress() {
        if(validTime == -1)
            return 1;
        long tmp = validTime - System.currentTimeMillis();
        if(tmp < 0)
            return 0;
        return (float)tmp/(float)millis;
    }

    public long getMillis() {
        return millis;
    }
}
