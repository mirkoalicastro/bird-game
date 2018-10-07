package com.badlogic.androidgames.framework;

public interface Button {
    boolean isEnabled();
    boolean inBounds(Input.TouchEvent event);
    int getWidth();
    int getHeight();
    int getX();
    int getY();
    void enable(boolean enabled);
    void draw();
}
