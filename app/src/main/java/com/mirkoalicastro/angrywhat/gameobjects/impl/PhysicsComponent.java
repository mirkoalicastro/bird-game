package com.mirkoalicastro.angrywhat.gameobjects.impl;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;
import com.google.fpl.liquidfun.World;

public class PhysicsComponent {
    private Body body;
    private World world;
    private final Vec2 v = new Vec2();

    private float width, height;

    public void applyForce(float x, float y){
        x /= 140; y /= 140;

        x -= body.getLinearVelocity().getX();
        y -= body.getLinearVelocity().getY();

        v.set(x, y);

        body.applyForceToCenter(v,true);
    }

    public float getX(){
        return body.getPositionX();
    }

    public float getY(){
        return body.getPositionY();
    }

    public PhysicsComponent setBody(Body body){
        this.body = body;
        return this;
    }

    public PhysicsComponent setWidth(float width){
        this.width = width;
        return this;
    }

    public PhysicsComponent setHeight(float height){
        this.height = height;
        return this;
    }

    public float getWidth(){
        return width;
    }

    public float getHeight(){
        return height;
    }

    public void delete(){
        world.destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    public PhysicsComponent setWorld(World world) {
        this.world = world;
        return this;
    }

}