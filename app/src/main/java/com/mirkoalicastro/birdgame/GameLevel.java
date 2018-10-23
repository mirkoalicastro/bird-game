package com.mirkoalicastro.birdgame;

import com.badlogic.androidgames.framework.Graphics;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.birdgame.gameobjects.Component;
import com.mirkoalicastro.birdgame.gameobjects.Entity;
import com.mirkoalicastro.birdgame.gameobjects.impl.PhysicsComponent;
import com.mirkoalicastro.birdgame.gameobjects.impl.RectangleDrawableComponent;
import com.mirkoalicastro.birdgame.physics.Converter;
import com.mirkoalicastro.birdgame.utils.IdGenerator;

import java.util.Iterator;

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
    private final IdGenerator idGenerator;

    GameLevel(World world, Graphics graphics) {
        this.graphics = graphics;
        this.world = world;
        lastObstacleX = graphics.getWidth()/2;
        tolerance = graphics.getWidth() * LEVEL_PRECALCULATED_PERCENTAGE/100;
        idGenerator = IdGenerator.getInstance();
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
        return (int)(Math.random()*((OBSTACLE_TO-OBSTACLE_FROM-OBSTACLE_FREE_HEIGHT)) + OBSTACLE_FROM);
    }

    private void addObstacle(int x, int y) {
        int id = idGenerator.next();
        Entity first = new Entity(id), second = new Entity(id);
        float h = y;
        Component firstDrawable = new RectangleDrawableComponent(graphics).setWidth(OBSTACLE_WIDTH)
                .setHeight((int)h).setPixmap(null).setEffect(Assets.obstacleTile);
        first.addComponent(firstDrawable);

        box.setAsBox(Converter.frameToPhysics(OBSTACLE_WIDTH/2),
                Converter.frameToPhysics(h/2));

        bodyDef.setPosition(Converter.frameToPhysics(x+OBSTACLE_WIDTH/2),Converter.frameToPhysics(h/2));
        bodyDef.setType(BodyType.staticBody);

        Body firstBody = world.createBody(bodyDef);

        firstBody.setUserData(first);
        firstBody.createFixture(box,0);

        PhysicsComponent firstPhysicsComponent = new PhysicsComponent(firstBody);
        firstPhysicsComponent.setWidth(Converter.frameToPhysics(OBSTACLE_WIDTH)).setHeight(Converter.frameToPhysics(h));

        first.addComponent(firstPhysicsComponent);

        obstacles.add(first);

        y += OBSTACLE_FREE_HEIGHT;
        h = graphics.getHeight()-y;
        Component secondDrawable = new RectangleDrawableComponent(graphics).setWidth(OBSTACLE_WIDTH)
                .setHeight((int)h).setPixmap(null).setEffect(Assets.obstacleTile);
        second.addComponent(secondDrawable);

        box.setAsBox(Converter.frameToPhysics(OBSTACLE_WIDTH/2),
                Converter.frameToPhysics(h/2));

        bodyDef.setPosition(Converter.frameToPhysics(x+OBSTACLE_WIDTH/2),Converter.frameToPhysics(y+h/2));
        bodyDef.setType(BodyType.staticBody);

        Body secondBody = world.createBody(bodyDef);

        secondBody.setUserData(second);
        secondBody.createFixture(box,0);

        PhysicsComponent secondPhysicsComponent = new PhysicsComponent(secondBody);
        secondPhysicsComponent.setWidth(Converter.frameToPhysics(OBSTACLE_WIDTH)).setHeight(Converter.frameToPhysics(h));

        second.addComponent(secondPhysicsComponent);

        obstacles.add(second);
    }

    void dispose() {
        Iterator<Entity> iterator = getObstacles().iterator();
        while (iterator.hasNext()) {
            Entity obstacle = iterator.next();
            iterator.remove();
        }
    }

    private final static float OBSTACLE_FROM = 150;
    private final static float OBSTACLE_TO = 1080-150;
    private final static int OBSTACLE_SPACE_BETWEEN = 350;
    private final static int OBSTACLE_WIDTH = 120;
    private final static int OBSTACLE_FREE_HEIGHT = 360;
    private final static int LEVEL_PRECALCULATED_PERCENTAGE = 100;

}
