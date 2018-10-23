package com.mirkoalicastro.birdgame.physics;

public class Converter {
    private static final float PHYSIC_HEIGHT = 15;

    private static float SCALE;

    public static void setScale(int frameWidth, int frameHeight){
        SCALE = (float)frameHeight/PHYSIC_HEIGHT;
    }

    public static float physicsToFrame(float val){ return val*SCALE; }

    public static float frameToPhysics(float val){ return val/SCALE; }

}