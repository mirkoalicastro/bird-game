package com.mirkoalicastro.angrywhat;

import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.angrywhat.gameobjects.Entity;
import com.mirkoalicastro.angrywhat.gameobjects.impl.CircleDrawableComponent;

public class GameStatus {
    private final World world;
    private final Entity avatar;
    GameStatus(World world, Entity avatar) {
        this.world = world;
        this.avatar = avatar;
    }

    Entity getAvatar() {
        return avatar;
    }

    World getWorld() {
        return world;
    }
}
