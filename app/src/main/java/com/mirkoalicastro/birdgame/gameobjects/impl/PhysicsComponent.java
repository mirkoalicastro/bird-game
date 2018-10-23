package com.mirkoalicastro.birdgame.gameobjects.impl;

import android.util.Log;

import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.Vec2;
import com.mirkoalicastro.birdgame.gameobjects.Component;

public class PhysicsComponent extends Component {
    private Body body;
    private final Vec2 v = new Vec2();

    private float width, height;

    public float getXVelocity() {
        return body.getLinearVelocity().getX();
    }

    public float getYVelocity() {
        return body.getLinearVelocity().getY();
    }

    public void applyForce(float x, float y) {
        v.set(x, y);
        body.applyForceToCenter(v,true);
    }

    public void applyLinearVelocity(float x, float y) {
        v.set(x, y);
        body.setLinearVelocity(v);
    }

    public float getX(){
        return body.getPositionX();
    }

    public float getY(){
        return body.getPositionY();
    }

    public PhysicsComponent(Body body){
        this.body = body;
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
        body.getWorld().destroyBody(body);
    }

    public Body getBody() {
        return body;
    }

    @Override
    public Type type() {
        return Type.Phyisics;
    }

    public void stop() {
        applyLinearVelocity(0,0);
    }

    public void stopX() {
        applyLinearVelocity(0, body.getLinearVelocity().getY());
    }

    public void stopY() {
        applyLinearVelocity(body.getLinearVelocity().getX(), 0);
    }
}