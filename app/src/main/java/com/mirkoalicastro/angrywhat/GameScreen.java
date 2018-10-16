package com.mirkoalicastro.angrywhat;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.RegularSpritesheetAnimation;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.angrywhat.camera.Camera;
import com.mirkoalicastro.angrywhat.camera.impl.ScrollingAbscissaCamera;
import com.mirkoalicastro.angrywhat.gameobjects.Component;
import com.mirkoalicastro.angrywhat.gameobjects.Entity;
import com.mirkoalicastro.angrywhat.gameobjects.impl.CircleDrawableComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.DrawableComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.PhysicsComponent;
import com.mirkoalicastro.angrywhat.utils.Converter;

import java.util.Iterator;

public class GameScreen extends Screen {
    private final GameStatus gameStatus;
    private final BodyDef bodyDef = new BodyDef();
    private final FixtureDef fixtureDef = new FixtureDef();
    private final Graphics graphics;
    private final TimedCircularButton jumpButton;
    private long jumpUntil;
    private final RegularSpritesheetAnimation tmpAnimation;
    private final Camera camera;
    private final GameLevel level;

    GameScreen(Game game) {
        super(game);
        graphics = game.getGraphics();
        Converter.setScale(graphics.getWidth(), graphics.getHeight());
        //TODO fix x,y
        jumpButton = new TimedCircularButton(graphics, (int)(graphics.getWidth()*RELATIVE_X_JUMP_BUTTON), (int)(graphics.getHeight()*RELATIVE_Y_JUMP_BUTTON), RADIUS_JUMP_BUTTON, JUMP_RECHARGE);

        jumpButton.setSecondaryColor(Color.RED)//.setSecondaryPixmap(Assets.swordsWhite)
                .setColor(Color.GREEN)//.setPixmap(Assets.swordsBlack)
                .setStroke(15, Color.BLACK);

        World world = new World(0, WORLD_GRAVITY);
        Entity avatar = createAvatar(world, (int)(graphics.getWidth()*RELATIVE_X_AVATAR), (int)(graphics.getHeight()*RELATIVE_Y_AVATAR));
        gameStatus = new GameStatus(world, avatar);
        tmpAnimation = new RegularSpritesheetAnimation(graphics, (AndroidPixmap)Assets.avatar,39,39,500);
        camera = new ScrollingAbscissaCamera(avatar, graphics.getWidth(), graphics.getHeight());
        level = new GameLevel(world, graphics);
    }

    @Override
    public void update(float deltaTime) {
        boolean jump = false;
        for (Input.TouchEvent event: game.getInput().getTouchEvents()) {
            if (jumpButton.inBounds(event) && event.type == Input.TouchEvent.TOUCH_UP) {
                if (jumpButton.isEnabled()) {
                    jump = true;
                    jumpButton.resetTime();
                } else {
                    //sound
                }
            }
        }
        if (jump) {
            if(System.currentTimeMillis() > jumpUntil) {
                Log.d("PROVA", "stoppo");
                stopGravity(gameStatus.getAvatar());
            } else {
                Log.d("PROVA", "non stoppo");
            }
            jumpUntil = System.currentTimeMillis() + JUMP_DURATION;
        }
        if (System.currentTimeMillis() <= jumpUntil) {
            jumpEntity(gameStatus.getAvatar());
        }
        gameStatus.getWorld().step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);
        camera.step();
        updateEntityPosition(gameStatus.getAvatar());
        Iterator<Entity> iterator = level.getObstacles().iterator();
        while(iterator.hasNext()) {
            Entity obs = iterator.next();
            updateEntityPosition(obs);
            DrawableComponent drawableComponent = (DrawableComponent) obs.getComponent(Component.Type.Drawable);
            if(drawableComponent.getX() < drawableComponent.getWidth()) {
                iterator.remove();
                Log.d("PROVA", "rimosso");
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        graphics.clear(Color.BLACK);
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);
        avatarDrawable.draw();
        jumpButton.draw();
        tmpAnimation.draw(100,100);
        for(Entity obs: level.getObstacles()) {
            DrawableComponent obsDrawable = (DrawableComponent) obs.getComponent(Component.Type.Drawable);
            obsDrawable.draw();
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

    }

    @Override
    public void back() {
        ((Activity)game).finish();
    }

    private void stopGravity(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        if(physicsComponent.getYVelocity() > 0)
            physicsComponent.stopY();
    }

    private void jumpEntity(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        physicsComponent.applyLinearVelocity(WORLD_PROGRESS,-JUMP_FORCE);
    }

    private void updateEntityPosition(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        DrawableComponent drawableComponent = (DrawableComponent) entity.getComponent(Component.Type.Drawable);
        if (drawableComponent == null)
            throw new IllegalArgumentException("Entity has not any drawable component");
        drawableComponent.setX((int)Converter.physicsToFrame(camera.calculateX(physicsComponent.getX())));
        drawableComponent.setY((int)Converter.physicsToFrame(camera.calculateY(physicsComponent.getY())));
    }

    private Entity createAvatar(World world, int avatarX, int avatarY) {
        Entity avatar = new Entity();
        Component avatarDrawable = new CircleDrawableComponent(graphics).setRadius(RADIUS_AVATAR)
                .setPixmap(null).setColor(Color.RED).setX(avatarX).setY(avatarY);
        avatar.addComponent(avatarDrawable);

        bodyDef.setPosition(Converter.frameToPhysics(avatarX),Converter.frameToPhysics(avatarY));
        bodyDef.setType(BodyType.dynamicBody);

        Body avatarBody = world.createBody(bodyDef);

        avatarBody.setSleepingAllowed(false);
        avatarBody.setBullet(true);
        avatarBody.setUserData(avatar);
        avatarBody.setAwake(true);

        Shape avatarShape = new CircleShape();
        avatarShape.setRadius(Converter.frameToPhysics(RADIUS_AVATAR));

        fixtureDef.setDensity(1);
        fixtureDef.setFriction(1);
        fixtureDef.setRestitution(1);
        fixtureDef.setShape(avatarShape);

        avatarBody.createFixture(fixtureDef);

        PhysicsComponent avatarPhysics = new PhysicsComponent(avatarBody);

        avatarPhysics.applyLinearVelocity(WORLD_PROGRESS, 0);

        avatar.addComponent(avatarPhysics);

        return avatar;
    }

    private final static long JUMP_DURATION = 80;
    private final static long JUMP_RECHARGE = JUMP_DURATION*3;
    private final static int RADIUS_AVATAR = 60;
    private final static float RELATIVE_X_AVATAR = 0.1f;
    private final static float RELATIVE_Y_AVATAR = 0.15f;
    private final static int VELOCITY_ITERATIONS = 8;
    private final static int POSITION_ITERATIONS = 2;
    private final static int RADIUS_JUMP_BUTTON = 150;
    private final static float RELATIVE_X_JUMP_BUTTON = 0.85f;
    private final static float RELATIVE_Y_JUMP_BUTTON = 0.8f;
    private final static float JUMP_FORCE = 2.3f;
    private final static float WORLD_GRAVITY = 2.3f;
    private final static float WORLD_PROGRESS = 1.5f;

}
