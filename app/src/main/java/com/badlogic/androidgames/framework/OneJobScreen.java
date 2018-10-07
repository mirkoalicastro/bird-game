package com.badlogic.androidgames.framework;


public abstract class OneJobScreen extends Screen {

    public OneJobScreen(Game game) {
        super(game);
    }

    @Override
    public final void present(float deltaTime) { }

    @Override
    public final void update(float deltaTime) {
        doJob();
    }

    public abstract void doJob();

    public abstract void setProgress(int progress);

    public abstract int getProgress();

    public abstract void onProgress(int progress);
}
