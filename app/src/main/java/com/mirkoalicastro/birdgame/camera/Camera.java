package com.mirkoalicastro.birdgame.camera;

public abstract class Camera {
    protected final int width, height;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public abstract void step();
    public abstract float calculateX(float x);
    public abstract float calculateY(float y);
    public abstract void dispose();
}
