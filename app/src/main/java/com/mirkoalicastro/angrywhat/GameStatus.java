package com.mirkoalicastro.angrywhat;

import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.angrywhat.gameobjects.Component;
import com.mirkoalicastro.angrywhat.gameobjects.Entity;
import com.mirkoalicastro.angrywhat.gameobjects.impl.CircleDrawableComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.PhysicsComponent;

public class GameStatus {
    private int score;
    private int lastObstaclePassed;
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

    void dispose() {
        PhysicsComponent physicsComponent = (PhysicsComponent) avatar.getComponent(Component.Type.Phyisics);
        if(physicsComponent != null)
            physicsComponent.delete();
        world.delete();
    }

    public void updateScore(int obstaclePassed) {
        if(obstaclePassed > lastObstaclePassed) {
            score++;
            lastObstaclePassed = obstaclePassed;
        }
    }

    public int getScore() {
        return score;
    }
}
