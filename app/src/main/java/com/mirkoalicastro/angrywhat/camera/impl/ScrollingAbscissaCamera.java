package com.mirkoalicastro.angrywhat.camera.impl;

import android.util.Log;

import com.mirkoalicastro.angrywhat.camera.Camera;
import com.mirkoalicastro.angrywhat.gameobjects.Component;
import com.mirkoalicastro.angrywhat.gameobjects.Entity;
import com.mirkoalicastro.angrywhat.gameobjects.impl.DrawableComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.PhysicsComponent;
import com.mirkoalicastro.angrywhat.utils.Converter;

public class ScrollingAbscissaCamera extends Camera {
    private final Entity entity;
    private final float x;
    private float deltaX;
    public ScrollingAbscissaCamera(Entity entity, int width, int height) {
        super(width,height);
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        this.entity = entity;
        this.x = physicsComponent.getX();
    }

    @Override
    public void step() {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        deltaX = x - physicsComponent.getX();
        Log.d("Camera", "deltaX " + deltaX);
    }

    @Override
    public float calculateX(float x) {
        return x + deltaX;
    }

    @Override
    public float calculateY(float y) {
        return y;
    }

}
