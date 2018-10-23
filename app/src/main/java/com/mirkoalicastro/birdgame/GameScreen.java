package com.mirkoalicastro.birdgame;

import android.app.Activity;
import android.graphics.Color;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.ContactListener;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.PolygonShape;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.birdgame.camera.Camera;
import com.mirkoalicastro.birdgame.camera.impl.ScrollingAbscissaCamera;
import com.mirkoalicastro.birdgame.gameobjects.Component;
import com.mirkoalicastro.birdgame.gameobjects.Entity;
import com.mirkoalicastro.birdgame.gameobjects.impl.CircleAnimationDrawableComponent;
import com.mirkoalicastro.birdgame.gameobjects.impl.DrawableComponent;
import com.mirkoalicastro.birdgame.gameobjects.impl.PhysicsComponent;
import com.mirkoalicastro.birdgame.physics.Converter;
import com.mirkoalicastro.birdgame.physics.MyContactListener;
import com.mirkoalicastro.birdgame.utils.IdGenerator;

import java.util.Iterator;

import mirkoalicastro.com.birdgame.R;

public class GameScreen extends Screen {
    private GameStatus gameStatus;
    private BodyDef bodyDef;
    private FixtureDef fixtureDef;
    private PolygonShape box;
    private final Graphics graphics;
    private Camera camera;
    private GameLevel gameLevel;
    private final IdGenerator idGenerator = IdGenerator.getInstance();
    private long jumpUntil;
    private long animationUntil;
    private Entity[] enclosures;
    private ContactListener contactListener;

    GameScreen(Game game) {
        super(game);
        graphics = game.getGraphics();
        Converter.setScale(graphics.getWidth(), graphics.getHeight());
        allocate();
    }

    private void allocate() {
        bodyDef = new BodyDef();
        fixtureDef = new FixtureDef();
        box = new PolygonShape();
        contactListener = new MyContactListener();
        World world = new World(0, WORLD_GRAVITY*FAST_FACTOR);
        world.setContactListener(contactListener);
        Entity cameraman = createCameraman(world, (int)(graphics.getWidth()*RELATIVE_X_AVATAR), -50);
        Entity avatar = createAvatar(world, (int)(graphics.getWidth()*RELATIVE_X_AVATAR), (int)(graphics.getHeight()*RELATIVE_Y_AVATAR));
        enclosures = createEnclosure(world, graphics.getWidth(), graphics.getHeight());
        gameStatus = new GameStatus(world, avatar);
        camera = new ScrollingAbscissaCamera(cameraman, graphics.getWidth(), graphics.getHeight());
        gameLevel = new GameLevel(world, graphics);
    }

    @Override
    public void update(float deltaTime) {
        boolean jump = false;
        boolean isAlive = gameStatus.isAvatarAlive();
        PhysicsComponent avatarPhysics = (PhysicsComponent) gameStatus.getAvatar().getComponent(Component.Type.Phyisics);
        int absoluteAvatarX = (int)Converter.physicsToFrame(avatarPhysics.getX());
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);
        long now = System.currentTimeMillis();

        for (Input.TouchEvent event: game.getInput().getTouchEvents()) {
            if (event.type == Input.TouchEvent.TOUCH_UP) {
//                if (menubutton.inBounds(event)) {

//                } else
                if (isAlive) {
                    jump = true;
                    ((CircleAnimationDrawableComponent) avatarDrawable).play();
                } else {
                    allocate(); //TODO button
                    return;
                }

//                }
            }
        }

        if (!isAlive)
            return;

        if (jump) {
            if (now > jumpUntil)
                stopGravity(gameStatus.getAvatar());
            animationUntil = now + ANIMATION_JUMP_DURATION;
            jumpUntil = now + JUMP_DURATION;
        }
        if (now <= jumpUntil)
            jumpEntity(gameStatus.getAvatar());

        if (now > animationUntil)
            ((CircleAnimationDrawableComponent) avatarDrawable).stopAt(DEFAULT_FRAME_AVATAR);

        gameStatus.getWorld().step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);
        camera.step();
        gameLevel.step(absoluteAvatarX);
        updateEntityPosition(gameStatus.getAvatar());
        updateScore();
    }

    @Override
    public void present(float deltaTime) {
        graphics.drawEffect(Assets.backgroundGradient,0,0,graphics.getWidth(), graphics.getHeight());
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);
        avatarDrawable.draw();
        for(Entity obs: gameLevel.getObstacles()) {
            DrawableComponent obsDrawable = (DrawableComponent) obs.getComponent(Component.Type.Drawable);
            obsDrawable.draw();
        }
        graphics.drawText(((Activity)game).getString(R.string.score) + gameStatus.getScore(),graphics.getWidth()-SCORE_RIGHT_MARGIN,SCORE_TOP_MARGIN,SCORE_FONT_SIZE, Color.BLACK, Graphics.TextAlign.RIGHT);
        if (!gameStatus.isAvatarAlive()) {
            graphics.drawText("HAI PERSO", 100, 100, 45, Color.BLACK, Graphics.TextAlign.CENTER);
        }
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        for (Entity enclosure: enclosures)
            ((PhysicsComponent) enclosure.getComponent(Component.Type.Phyisics)).delete();
        bodyDef.delete();
        fixtureDef.delete();
        box.delete();
        contactListener.delete();
        gameLevel.dispose();
        camera.dispose();
        gameStatus.dispose();
    }

    @Override
    public void back() {
        ((Activity)game).finish();
    }

    private void updateScore() {
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);
        if (avatarDrawable.getX()+avatarDrawable.getWidth()/2 < 0 ||
                avatarDrawable.getY()-avatarDrawable.getHeight()/2 > graphics.getHeight()) {
            gameStatus.gameOver();
            return;
        }
        Iterator<Entity> iterator = gameLevel.getObstacles().iterator();
        while (iterator.hasNext()) {
            Entity obstacle = iterator.next();
            updateEntityPosition(obstacle, true);
            DrawableComponent drawableComponent = (DrawableComponent) obstacle.getComponent(Component.Type.Drawable);
            if ((drawableComponent.getX()+drawableComponent.getWidth()) < avatarDrawable.getX()) {
                gameStatus.updateScore(obstacle.getId());
                if (drawableComponent.getX() < -drawableComponent.getWidth()) {
                    iterator.remove();
                }
            }
        }
    }

    private void stopGravity(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        if (physicsComponent.getYVelocity() > 0)
            physicsComponent.stopY();
    }

    private void jumpEntity(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        physicsComponent.applyLinearVelocity(WORLD_PROGRESS*FAST_FACTOR,-JUMP_FORCE*FAST_FACTOR);
    }

    private void updateEntityPosition(Entity entity) {
        updateEntityPosition(entity, false);
    }


    private void updateEntityPosition(Entity entity, boolean isCentered) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        DrawableComponent drawableComponent = (DrawableComponent) entity.getComponent(Component.Type.Drawable);
        if (drawableComponent == null)
            throw new IllegalArgumentException("Entity has not any drawable component");
        float x = physicsComponent.getX();
        float y = physicsComponent.getY();
        if (isCentered) {
            x -= physicsComponent.getWidth()/2;
            y -= physicsComponent.getHeight()/2;
        }
        drawableComponent.setX((int)Converter.physicsToFrame(camera.calculateX(x)));
        drawableComponent.setY((int)Converter.physicsToFrame(camera.calculateY(y)));
    }

    private Entity createCameraman(World world, int x, int y) {
        Entity cameraman = new Entity(idGenerator.next());

        bodyDef.setPosition(Converter.frameToPhysics(x),Converter.frameToPhysics(y));
        bodyDef.setType(BodyType.kinematicBody);

        Body cameramanBody = world.createBody(bodyDef);

        cameramanBody.setSleepingAllowed(false);
        cameramanBody.setUserData(cameraman);
        cameramanBody.setAwake(true);

        Shape cameramanShape = new CircleShape();
        cameramanShape.setRadius(Converter.frameToPhysics(1));

        fixtureDef.setShape(cameramanShape);

        cameramanBody.createFixture(fixtureDef);

        PhysicsComponent cameramanPhysics = new PhysicsComponent(cameramanBody);

        cameramanPhysics.setWidth(Converter.frameToPhysics(0.5f)).setHeight(Converter.frameToPhysics(0.5f));

        cameramanPhysics.applyLinearVelocity(WORLD_PROGRESS*FAST_FACTOR, 0);

        cameraman.addComponent(cameramanPhysics);

        return cameraman;

    }

    private Entity createAvatar(World world, int x, int y) {
        Entity avatar = new Entity(idGenerator.next());
        Component avatarDrawable = new CircleAnimationDrawableComponent(graphics)
                .setDuration(ANIMATION_JUMP_VELOCITY).setFrameHeight(170).setFrameWidth(170).setPixmap(Assets.avatar).stopAt(DEFAULT_FRAME_AVATAR)
                .setRadius(DRAWABLE_RADIUS_AVATAR); //setColor(Color.RED).setX(x).setY(y);
        avatar.addComponent(avatarDrawable);

        bodyDef.setPosition(Converter.frameToPhysics(x),Converter.frameToPhysics(y));
        bodyDef.setType(BodyType.dynamicBody);

        Body avatarBody = world.createBody(bodyDef);

        avatarBody.setSleepingAllowed(false);
        avatarBody.setBullet(true);
        avatarBody.setUserData(avatar);
        avatarBody.setAwake(true);

        Shape avatarShape = new CircleShape();
        avatarShape.setRadius(Converter.frameToPhysics(PHYSICS_RADIUS_AVATAR));

        fixtureDef.setDensity(1);
        fixtureDef.setFriction(1);
        fixtureDef.setRestitution(1);
        fixtureDef.setShape(avatarShape);

        avatarBody.createFixture(fixtureDef);

        PhysicsComponent avatarPhysics = new PhysicsComponent(avatarBody);

        avatarPhysics.setWidth(Converter.frameToPhysics(PHYSICS_RADIUS_AVATAR /2)).setHeight(Converter.frameToPhysics(PHYSICS_RADIUS_AVATAR /2));
        avatarPhysics.applyLinearVelocity(WORLD_PROGRESS*FAST_FACTOR, 0);

        avatar.addComponent(avatarPhysics);

        return avatar;
    }

    private Entity[] createEnclosure(World world, int width, int height) {
        int id = idGenerator.next();
        Entity[] ret = new Entity[2];
        ret[0] = createLinearMovingBox(world, id,0,0, width,1,WORLD_PROGRESS*FAST_FACTOR,0);
        ret[1] = createLinearMovingBox(world, id, width,0,1, height,WORLD_PROGRESS*FAST_FACTOR,0);
        return ret;
    }

    private Entity createLinearMovingBox(World world, int entityId, int x, int y, int width, int height, float xLinearVelocity, float yLinearVelocity) {
        Entity entity = new Entity(entityId);

        box.setAsBox(Converter.frameToPhysics(width/2), Converter.frameToPhysics(height/2));

        bodyDef.setPosition(Converter.frameToPhysics(x+width/2),Converter.frameToPhysics(y+height/2));
        bodyDef.setType(BodyType.kinematicBody);

        Body body = world.createBody(bodyDef);

        body.setUserData(entity);
        body.createFixture(box,0);

        PhysicsComponent physicsComponent = new PhysicsComponent(body);
        physicsComponent.setWidth(Converter.frameToPhysics(width)).setHeight(Converter.frameToPhysics(height));

        physicsComponent.applyLinearVelocity(xLinearVelocity, yLinearVelocity);

        entity.addComponent(physicsComponent);

        return entity;
    }

    private final static int ANIMATION_JUMP_VELOCITY = 75;
    private final static long ANIMATION_JUMP_DURATION = 300;
    private final static int DEFAULT_FRAME_AVATAR = 1;

    private final static int PHYSICS_RADIUS_AVATAR = 55;
    private final static int DRAWABLE_RADIUS_AVATAR = 60;

    private final static long JUMP_DURATION = 50;
    private final static float JUMP_FORCE = 2.3f;

    private final static float RELATIVE_X_AVATAR = 0.2f;
    private final static float RELATIVE_Y_AVATAR = 0.15f;

    private final static float WORLD_GRAVITY = 2.3f;
    private final static float WORLD_PROGRESS = 1.5f;
    private final static int VELOCITY_ITERATIONS = 8;
    private final static int POSITION_ITERATIONS = 3;
    private final static float FAST_FACTOR = 1.2f;

    private final static int SCORE_RIGHT_MARGIN = 100;
    private final static int SCORE_TOP_MARGIN = 100;
    private final static int SCORE_FONT_SIZE = 50;
}
