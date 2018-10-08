package com.mirkoalicastro.angrywhat.gameobjects;

public abstract class Component {
    public abstract Type type();
    public enum Type {
        Phyisics, Drawable;
    }
}
