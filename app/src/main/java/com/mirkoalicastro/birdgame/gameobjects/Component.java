package com.mirkoalicastro.birdgame.gameobjects;

public abstract class Component {
    public abstract Type type();
    private Entity entity;
    void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public enum Type {
        Phyisics, Drawable
    }
}
