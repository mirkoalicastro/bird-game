package com.mirkoalicastro.angrywhat;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Shader;
import android.util.Log;

import com.badlogic.androidgames.framework.Effect;
import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Graphics;
import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.Screen;
import com.badlogic.androidgames.framework.impl.AndroidPixmap;
import com.badlogic.androidgames.framework.impl.RectangularLinearGradientEffect;
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
import com.mirkoalicastro.angrywhat.gameobjects.impl.RectangleDrawableComponent;
import com.mirkoalicastro.angrywhat.utils.Converter;
import com.mirkoalicastro.angrywhat.utils.IdGenerator;

import java.util.Iterator;

public class GameScreen extends Screen {
    private final GameStatus gameStatus;
    private final BodyDef bodyDef = new BodyDef();
    private final FixtureDef fixtureDef = new FixtureDef();
    private final Graphics graphics;
    private final RegularSpritesheetAnimation tmpAnimation;
    private final Camera camera;
    private final GameLevel gameLevel;
    private final Effect backgroundEffect;
    private final IdGenerator idGenerator = IdGenerator.getInstance();
    private long jumpUntil;
    private final Entity[] enclosures;

    GameScreen(Game game) {
        super(game);
        graphics = game.getGraphics();
        Converter.setScale(graphics.getWidth(), graphics.getHeight());
        World world = new World(0, WORLD_GRAVITY);
        Entity cameraman = createCameraman(world, (int)(graphics.getWidth()*RELATIVE_X_AVATAR), -50);
        Entity avatar = createAvatar(world, (int)(graphics.getWidth()*RELATIVE_X_AVATAR), (int)(graphics.getHeight()*RELATIVE_Y_AVATAR));
        enclosures = createEnclosure(world, graphics.getWidth(), graphics.getHeight());
        gameStatus = new GameStatus(world, avatar);
        tmpAnimation = new RegularSpritesheetAnimation(graphics, (AndroidPixmap)Assets.avatar,39,39,500);
        camera = new ScrollingAbscissaCamera(cameraman, graphics.getWidth(), graphics.getHeight());
        gameLevel = new GameLevel(world, graphics);
        backgroundEffect = new RectangularLinearGradientEffect(0,0, 0, graphics.getHeight(), new int[]{BACKGROUND_TOP_COLOR, BACKGROUND_BOTTOM_COLOR}, new float[]{0,1}, Shader.TileMode.REPEAT);
    }

    private Entity[] createEnclosure(World world, int width, int height) {
        int id = idGenerator.next();
        Entity[] arr = new Entity[4];
        for(int i=0; i<arr.length; i++)
            arr[i] = new Entity(id);
        //TODO create drawable and physics components
        return arr;
    }

    @Override
    public void update(float deltaTime) {
        boolean jump = false;
        for (Input.TouchEvent event: game.getInput().getTouchEvents()) {
            if(event.type == Input.TouchEvent.TOUCH_UP) {
//                if(menubutton.inBounds(event)) {

//                } else
                jump = true;
//                }
            }
        }
        if (jump) {
            if(System.currentTimeMillis() > jumpUntil) {
                stopGravity(gameStatus.getAvatar());
            } else {
                Log.d("PROVA", "non stoppo");
                //nothing only if there is no limit to jump!
            }
            jumpUntil = System.currentTimeMillis() + JUMP_DURATION;
        }
        if (System.currentTimeMillis() <= jumpUntil)
            jumpEntity(gameStatus.getAvatar());

        PhysicsComponent avatarPhysics = (PhysicsComponent) gameStatus.getAvatar().getComponent(Component.Type.Phyisics);
        int absoluteAvatarX = (int)Converter.physicsToFrame(avatarPhysics.getX());
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);

        gameStatus.getWorld().step(deltaTime, VELOCITY_ITERATIONS, POSITION_ITERATIONS, 0);
        camera.step();
        gameLevel.step(absoluteAvatarX);
        updateEntityPosition(gameStatus.getAvatar());
        Iterator<Entity> iterator = gameLevel.getObstacles().iterator();
        while(iterator.hasNext()) {
            Entity obs = iterator.next();
            updateEntityPosition(obs, true);
            DrawableComponent drawableComponent = (DrawableComponent) obs.getComponent(Component.Type.Drawable);
            if((drawableComponent.getX()+drawableComponent.getWidth()) < avatarDrawable.getX()) {
                gameStatus.updateScore(obs.getId());
                if (drawableComponent.getX() < -drawableComponent.getWidth()) {
                    iterator.remove();
                    Log.d("PROVA", "rimosso");
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        graphics.drawEffect(backgroundEffect,0,0,graphics.getWidth(), graphics.getHeight());
        DrawableComponent avatarDrawable = (DrawableComponent) gameStatus.getAvatar().getComponent(Component.Type.Drawable);
        avatarDrawable.draw();
        tmpAnimation.draw(100,100);
        for(Entity obs: gameLevel.getObstacles()) {
            DrawableComponent obsDrawable = (DrawableComponent) obs.getComponent(Component.Type.Drawable);
            obsDrawable.draw();
        }
        graphics.drawText("Score: " + gameStatus.getScore(),1700,100,45, Color.BLACK);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {
        gameLevel.dispose();
        gameStatus.dispose();
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
        if(isCentered) {
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

        cameramanPhysics.applyLinearVelocity(WORLD_PROGRESS, 0);

        cameraman.addComponent(cameramanPhysics);

        return cameraman;

    }

    private Entity createAvatar(World world, int x, int y) {
        Entity avatar = new Entity(idGenerator.next());
        Component avatarDrawable = new CircleDrawableComponent(graphics).setRadius(RADIUS_AVATAR)
                .setPixmap(null).setColor(Color.RED).setX(x).setY(y);
        avatar.addComponent(avatarDrawable);

        bodyDef.setPosition(Converter.frameToPhysics(x),Converter.frameToPhysics(y));
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

        avatarPhysics.setWidth(Converter.frameToPhysics(RADIUS_AVATAR/2)).setHeight(Converter.frameToPhysics(RADIUS_AVATAR/2));
        avatarPhysics.applyLinearVelocity(WORLD_PROGRESS, 0);

        avatar.addComponent(avatarPhysics);

        return avatar;
    }

    private final static long JUMP_DURATION = 80;
    private final static long JUMP_RECHARGE = 0;//JUMP_DURATION*3;
    private final static int RADIUS_AVATAR = 60;
    private final static float RELATIVE_X_AVATAR = 0.1f;
    private final static float RELATIVE_Y_AVATAR = 0.15f;
    private final static int VELOCITY_ITERATIONS = 8;
    private final static int POSITION_ITERATIONS = 3;
    private final static int RADIUS_JUMP_BUTTON = 150;
    private final static float RELATIVE_X_JUMP_BUTTON = 0.85f;
    private final static float RELATIVE_Y_JUMP_BUTTON = 0.8f;
    private final static float JUMP_FORCE = 2.3f;
    private final static float WORLD_GRAVITY = 2.3f;
    private final static float WORLD_PROGRESS = 1.5f;
    private final static int BACKGROUND_TOP_COLOR = Color.parseColor("#E8FFFF");
    private final static int BACKGROUND_BOTTOM_COLOR = Color.parseColor("#A8D5F4");

}
