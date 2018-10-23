package com.mirkoalicastro.birdgame;

import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.birdgame.gameobjects.Component;
import com.mirkoalicastro.birdgame.gameobjects.Entity;
import com.mirkoalicastro.birdgame.gameobjects.impl.CircleDrawableComponent;
import com.mirkoalicastro.birdgame.gameobjects.impl.PhysicsComponent;

public class GameStatus {
    private int score;
    private int lastObstaclePassed;
    private final World world;
    private final Entity avatar;
    private boolean isAlive = true;
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
        if(isAlive && obstaclePassed > lastObstaclePassed) {
            score++;
            lastObstaclePassed = obstaclePassed;
        }
    }

    public int getScore() {
        return score;
    }

    public void gameOver() {
        isAlive = false;
    }

    public boolean isAvatarAlive() {
        return isAlive;
    }
}
