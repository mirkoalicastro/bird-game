package com.mirkoalicastro.birdgame.gameobjects;

import java.util.EnumMap;
import java.util.Map;

public class Entity {
    private final int id;
    private final Map<Component.Type, Component> features;

    public Entity(int id) {
        this.id = id;
        features = new EnumMap<>(Component.Type.class);
    }

    public Entity addComponent(Component component) {
        features.put(component.type(), component);
        component.setEntity(this);
        return this;
    }

    public Entity removeComponent(Component.Type type) {
        Component comp = features.remove(type);
        if(comp != null)
            comp.setEntity(null);
        return this;
    }

    public Component getComponent(Component.Type type) {
        return features.get(type);
    }

    public int getId() {
        return id;
    }
}
