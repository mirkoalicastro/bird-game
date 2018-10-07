package com.badlogic.androidgames.framework.impl;

import com.badlogic.androidgames.framework.OneJobScreen;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AndroidLoadingScreen extends OneJobScreen {
    protected final AndroidGame androidGame;

    private volatile int progressValue;
    private final Queue<Integer> animations;
    private final GradualProgress thread;

    public abstract void doJob();
    private final int delta;

    public AndroidLoadingScreen(AndroidGame androidGame) {
        this(androidGame, 5);
    }

    /**
     *
     * @param androidGame the android game instance
     * @param delta how much it has to increase the percentage in order to reach the desidered value (Default: 2)
     */
    public AndroidLoadingScreen(AndroidGame androidGame, int delta) {
        super(androidGame);
        this.androidGame = androidGame;
        this.delta = delta;
        progressValue = 0;
        animations = new LinkedList<>();
        thread = new GradualProgress();
        thread.start();
    }

    public void setProgress(final int progress) {
        synchronized (animations) {
            animations.add(progress);
            animations.notifyAll();
        }
    }

    public int getProgress() {
        return progressValue;
    }

    /**
     * If AngryWhatGame class wants to override dispose method, it must first call 'super.dispose()'
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
