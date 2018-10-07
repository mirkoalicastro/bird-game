package com.badlogic.androidgames.framework.impl;

import android.graphics.Color;
import android.support.annotation.NonNull;

import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Pixmap;

public class NumberPickerButton extends AndroidRectangularButton {
    private static final int DEFAULT_STEPS = 1;
    private static final int FONT_SIZE = 74;
    private final Graphics graphics;
    private int min, max, steps;
    private int x, y;
    private int fontColor = Color.BLACK;
    private int value;
    private final AndroidButton minusButton, plusButton;
    private boolean enabled = true;

    public NumberPickerButton setMin(int min) {
        if(min > max)
            throw new IllegalArgumentException("Min cannot be greater than max");
        this.min = min;
        if(this.value < min)
            this.value = min;
        return this;
    }
    public NumberPickerButton setMax(int max) {
        if(max < min)
            throw new IllegalArgumentException("Max cannot be lower than min");
        this.max = max;
        if(this.value > max)
            this.value = max;
        return this;
    }
    public NumberPickerButton setSteps(int steps) {
        if(steps < 1)
            throw new IllegalArgumentException("Step must be greater than zero");
        this.steps = steps;
        return this;
    }
    public NumberPickerButton(@NonNull Graphics graphics, int x, int y, int min, int max) {
        this(graphics, x, y, min, max, DEFAULT_STEPS);
    }
    public NumberPickerButton(@NonNull Graphics graphics, int x, int y, int min, int max, int steps) {
        super(graphics, x, y, calculatePlusButtonX(x) + FONT_SIZE - x, FONT_SIZE+6);
        this.min = min;
        this.max = max;
        this.minusButton = new AndroidRectangularButton(graphics,x,y,FONT_SIZE,FONT_SIZE+6);
        this.plusButton = new AndroidRectangularButton(graphics,x,y,FONT_SIZE,FONT_SIZE+6);
        this.graphics = graphics;
        setX(x);
        setY(y);
        setMin(min);
        setMax(max);
        setSteps(steps);
    }
    public NumberPickerButton setMinusPixmap(Pixmap minus) {
        minusButton.setPixmap(minus);
        return this;
    }

    public NumberPickerButton setPlusPixmap(Pixmap plus) {
        plusButton.setPixmap(plus);
        return this;
    }

    public NumberPickerButton setX(int x) {
        this.x = x;
        minusButton.setX(x);
        plusButton.setX(calculatePlusButtonX(x));
        return this;
    }

    private static int calculatePlusButtonX(int x) {
        return x+3*FONT_SIZE+108;
    }

    public NumberPickerButton setY(int y) {
        this.y = y;
        minusButton.setY(y);
        plusButton.setY(y);
        return this;
    }
    public NumberPickerButton setFontColor(int fontColor) {
        this.fontColor = fontColor;
        return this;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean inBounds(Input.TouchEvent event) {
        return minusButton.inBounds(event) || plusButton.inBounds(event);
    }

    @Override
    public int getWidth() {
        return plusButton.getX()+plusButton.getWidth()-minusButton.getX();
    }

    @Override
    public int getHeight() {
        return minusButton.getHeight();
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void enable(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void draw() {
        super.draw();
        if(value -steps >= min)
            minusButton.draw();
        if(value +steps <= max)
            plusButton.draw();
        graphics.drawText(String.valueOf(value),x+100+FONT_SIZE,y+66,FONT_SIZE,fontColor);
    }

    /**
     * @param event touch event
     * @return true if the current value is changed; false, otherwise.
     */
    public boolean update(Input.TouchEvent event) {
        if (minusButton.inBounds(event)) {
            if(value -steps >= min) {
                value -= steps;
                return true;
            }
        } else if (plusButton.inBounds(event)) {
            if (value + steps <= max) {
                value += steps;
                return true;
            }
        }
        return false;
    }

    public int getValue() {
        return value;
    }

    public NumberPickerButton setValue(int value) {
        if(value < min)
            throw new IllegalArgumentException("The value cannot be lower than min");
        if(value > max)
            throw new IllegalArgumentException("The value cannot be greater than max");
        if((value-min)%steps != 0)
            throw new IllegalArgumentException("The value cannot be selected using steps of " + steps);
        this.value = value;
        return this;
    }
}
