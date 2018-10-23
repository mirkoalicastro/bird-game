package com.mirkoalicastro.birdgame.camera.impl;

import com.mirkoalicastro.birdgame.camera.Camera;
import com.mirkoalicastro.birdgame.gameobjects.Component;
import com.mirkoalicastro.birdgame.gameobjects.Entity;
import com.mirkoalicastro.birdgame.gameobjects.impl.PhysicsComponent;

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
    }

    @Override
    public float calculateX(float x) {
        return x + deltaX;
    }

    @Override
    public float calculateY(float y) {
        return y;
    }

    @Override
    public void dispose() {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        physicsComponent.delete();
    }

    public Entity getCameraman() {
        return entity;
    }
}
