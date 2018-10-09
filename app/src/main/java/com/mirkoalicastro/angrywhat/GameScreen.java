package com.mirkoalicastro.angrywhat;

import android.graphics.Color;
import android.util.Log;

import com.badlogic.androidgames.framework.Button;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.TimedCircularButton;
import com.google.fpl.liquidfun.Body;
import com.google.fpl.liquidfun.BodyDef;
import com.google.fpl.liquidfun.BodyType;
import com.google.fpl.liquidfun.CircleShape;
import com.google.fpl.liquidfun.FixtureDef;
import com.google.fpl.liquidfun.Shape;
import com.google.fpl.liquidfun.World;
import com.mirkoalicastro.angrywhat.gameobjects.Component;
import com.mirkoalicastro.angrywhat.gameobjects.Entity;
import com.mirkoalicastro.angrywhat.gameobjects.impl.CircleDrawableComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.DrawableComponent;
import com.mirkoalicastro.angrywhat.gameobjects.impl.PhysicsComponent;
import com.mirkoalicastro.angrywhat.utils.Converter;

public class GameScreen extends Screen {
    private final static long JUMP_DURATION = 300;
    private final static long JUMP_RECHARGE = JUMP_DURATION*2;
    private final static int RADIUS_AVATAR = 60;
    private final static float RELATIVE_X_AVATAR = 0.95f;
    private final static float RELATIVE_Y_AVATAR = 0.05f;
    private final static int VELOCITY_ITERATIONS = 8;
    private final static int POSITION_ITERATIONS = 5;
    private final static int RADIUS_JUMP_BUTTON = 150;
    private final static float RELATIVE_X_JUMP_BUTTON = 1f;
    private final static float RELATIVE_Y_JUMP_BUTTON = 1f;
    private final static float JUMP_FORCE = 25f;
    private final static float WORLD_GRAVITY = 5f;
    private final GameStatus gameStatus;
    private final BodyDef bodyDef = new BodyDef();
    private final FixtureDef fixtureDef = new FixtureDef();
    private final Graphics graphics;
    private final TimedCircularButton jumpButton;
    private long jumpUntil;
    GameScreen(Game game) {
        super(game);
        graphics = game.getGraphics();
        Converter.setScale(graphics.getWidth(), graphics.getHeight());
        int jumpX = (int)(graphics.getWidth()*RELATIVE_X_JUMP_BUTTON + RADIUS_JUMP_BUTTON);
        int jumpY = (int)(graphics.getHeight()*RELATIVE_Y_JUMP_BUTTON + RADIUS_JUMP_BUTTON);
        jumpButton = new TimedCircularButton(graphics, jumpX, jumpY, RADIUS_JUMP_BUTTON, JUMP_RECHARGE);
                jumpButton.setSecondaryColor(Color.RED)//.setSecondaryPixmap(Assets.swordsWhite)
                .setColor(Color.GREEN)//.setPixmap(Assets.swordsBlack)
                .setStroke(15, Color.BLACK);

        int avatarX = (int)(graphics.getWidth()*RELATIVE_X_AVATAR);
        int avatarY = (int)(graphics.getHeight()*RELATIVE_Y_AVATAR);
        World world = new World(-WORLD_GRAVITY, 0);
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

        Component avatarPhysics = new PhysicsComponent(avatarBody);

        avatar.addComponent(avatarPhysics);

        this.gameStatus = new GameStatus(world, avatar);
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
            jumpUntil = System.currentTimeMillis() + JUMP_DURATION;
            stopEntity(gameStatus.getAvatar());
        }
        if (System.currentTimeMillis() <= jumpUntil) {
            Log.d("PROVA", "salto");
            jumpEntity(gameStatus.getAvatar());
        } else if(jumpUntil != 0) {
            stopEntity(gameStatus.getAvatar());
            jumpUntil = 0;
        }
        gameStatus.getWorld().step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);
        updateEntityPosition(gameStatus.getAvatar());
    }

    @Override
    public void present(float deltaTime) {
        graphics.clear(Color.BLACK);
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);
        avatarDrawable.draw();
        jumpButton.draw();
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

    }

    private void stopEntity(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        physicsComponent.stop();
    }

    private void jumpEntity(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        physicsComponent.applyForce(JUMP_FORCE,0);
    }

    private void updateEntityPosition(Entity entity) {
        PhysicsComponent physicsComponent = (PhysicsComponent) entity.getComponent(Component.Type.Phyisics);
        if (physicsComponent == null)
            throw new IllegalArgumentException("Entity has not any physics component");
        DrawableComponent drawableComponent = (DrawableComponent) entity.getComponent(Component.Type.Drawable);
        if (drawableComponent == null)
            throw new IllegalArgumentException("Entity has not any drawable component");
        drawableComponent.setX((int)Converter.physicsToFrame(physicsComponent.getX()));
        drawableComponent.setY((int)Converter.physicsToFrame(physicsComponent.getY()));
    }
}
