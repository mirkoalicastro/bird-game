package com.mirkoalicastro.angrywhat;

import android.graphics.Color;

import com.badlogic.androidgames.framework.Graphics;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.angrywhat.gameobjects.Component;
import com.mirkoalicastro.angrywhat.gameobjects.Entity;
import com.mirkoalicastro.angrywhat.gameobjects.impl.PhysicsComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.RectangleDrawableComponent;
import com.mirkoalicastro.angrywhat.utils.Converter;

public class GameLevel {
    private final int tolerance;
    private int lastObstacleX;
    private final Graphics graphics;
    private final World world;
    private final PolygonShape box = new PolygonShape();
    private final BodyDef bodyDef = new BodyDef();
    private final MyList<Entity> obstacles = new MyList<>(new MyList.FullyRemover<Entity>() {
        @Override
        public void remove(Entity el) {
            PhysicsComponent physicsComponent = (PhysicsComponent) el.getComponent(Component.Type.Phyisics);
            if(physicsComponent != null)
                physicsComponent.delete();
        }
    });

    GameLevel(World world, Graphics graphics) {
        this.graphics = graphics;
        this.world = world;
        lastObstacleX = graphics.getWidth()/2;
        tolerance = graphics.getWidth() * LEVEL_PRECALCULATED_PERCENTAGE/100;
    }

    /* Non thread-safe for improving performance */
    public void step(int currentX) {
        while (currentX+tolerance > lastObstacleX) {
            lastObstacleX += OBSTACLE_WIDTH + OBSTACLE_SPACE_BETWEEN;
            addObstacle(lastObstacleX,randomEntrance());
        }
    }

    /* Non thread-safe for improving perfomance */
    public Iterable<Entity> getObstacles() {
        obstacles.resetIterator();
        return obstacles;
    }

    private int randomEntrance() {
        return (int)(Math.random()*(graphics.getHeight()-(1.5*OBSTACLE_FREE_HEIGHT)));
    }

    private void addObstacle(int x, int y) {
        Entity first = new Entity();
        Entity second = new Entity();

        Component firstDrawable = new RectangleDrawableComponent(graphics).setWidth(OBSTACLE_WIDTH)
                .setHeight(y).setPixmap(null).setColor(Color.GREEN).setX(x).setY(0);
        first.addComponent(firstDrawable);

        box.setAsBox(Converter.frameToPhysics(OBSTACLE_WIDTH/2),
                Converter.frameToPhysics(y/2));

        bodyDef.setPosition(Converter.frameToPhysics(x),Converter.frameToPhysics(0));
        bodyDef.setType(BodyType.staticBody);

        Body firstBody = world.createBody(bodyDef);

        firstBody.setUserData(first);
        firstBody.createFixture(box,0);

        first.addComponent(new PhysicsComponent(firstBody));

        Component secondDrawable = new RectangleDrawableComponent(graphics).setWidth(OBSTACLE_WIDTH)
                .setHeight((graphics.getHeight()-(y+OBSTACLE_FREE_HEIGHT))).setPixmap(null).setColor(Color.GREEN).setX(x).setY(y+OBSTACLE_FREE_HEIGHT);
        second.addComponent(secondDrawable);

        box.setAsBox(Converter.frameToPhysics(OBSTACLE_WIDTH/2),
                Converter.frameToPhysics((graphics.getHeight()-(y+OBSTACLE_FREE_HEIGHT))/2));

        bodyDef.setPosition(Converter.frameToPhysics(x),Converter.frameToPhysics(y+OBSTACLE_FREE_HEIGHT));
        bodyDef.setType(BodyType.staticBody);

        Body secondBody = world.createBody(bodyDef);

        secondBody.setUserData(second);
        secondBody.createFixture(box,0);

        second.addComponent(new PhysicsComponent(secondBody));

        obstacles.add(first);
        obstacles.add(second);
    }

    void dispose() {
        for(Entity obs: getObstacles()) {
            PhysicsComponent physicsComponent = (PhysicsComponent) obs.getComponent(Component.Type.Phyisics);
            if(physicsComponent != null)
                physicsComponent.delete();
        }
    }

    private final static int OBSTACLE_SPACE_BETWEEN = 350;
    private final static int OBSTACLE_WIDTH = 125;
    private final static int OBSTACLE_FREE_HEIGHT = 325;
    private final static int LEVEL_PRECALCULATED_PERCENTAGE = 100;
}
