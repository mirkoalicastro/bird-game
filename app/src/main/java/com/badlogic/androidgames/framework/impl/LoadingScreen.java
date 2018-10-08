package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.Game;
import com.badlogic.androidgames.framework.Screen;

import java.util.LinkedList;
import java.util.Queue;

public abstract class LoadingScreen extends Screen {
    private volatile int progressValue;
    private final Queue<Integer> animations;
    private final GradualProgress thread;
    private final int delta;

    public LoadingScreen(Game game) {
        this(game, 5);
    }

    /**
     *
     * @param game the game instance
     * @param delta how much it has to increase the percentage in order to reach the desired value (Default: 2)
     */
    public LoadingScreen(Game game, int delta) {
        super(game);
        this.delta = delta;
        progressValue = 0;
        animations = new LinkedList<>();
        thread = new GradualProgress();
        thread.start();
    }

    protected void setProgress(int progress) {
        synchronized (animations) {
            animations.add(progress);
            animations.notifyAll();
        }
    }

    public int getProgress() {
        return progressValue;
    }

    @Override
    public final void present(float deltaTime) { }

    /**
     * If a class wants to override dispose method, it must first call 'super.dispose()'
     */
    public void dispose() {
        synchronized (animations) {
            while(!animations.isEmpty()) {
                try {
                    animations.wait();
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
        thread.interrupt();
    }

    public abstract void onProgress(int progress);

    private class GradualProgress extends Thread {
        @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()) {
                synchronized (animations) {
                    while(animations.isEmpty()) {
                        try {
                            animations.wait();
                        } catch (InterruptedException e) {
                            return;
                        }
                    }
                    int from = progressValue;
                    int to = animations.peek();
                    if (from == to) {
                        animations.remove();
                        continue;
                    }
                    if(from < to) {
                        for(int i=from; i+delta<to; i+=delta) {
                            progressValue = i;
                            onProgress(i);
                        }
                    } else {
                        for(int i=from; i-delta>to; i-=delta) {
                            progressValue = i;
                            onProgress(i);
                        }
                    }
                    if(progressValue != to) {
                        progressValue = to;
                        onProgress(to);
                    }
                    animations.remove();
                    animations.notifyAll();
                }
            }
        }
    }
}
