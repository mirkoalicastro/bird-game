package com.mirkoalicastro.birdgame.utils;

public class IdGenerator {
    private static final int DEFAULT_START = 0;
    private static final int DEFAULT_STEP = 1;
    private static IdGenerator instance;

    private final int step;
    private int nextId, size;

    public static IdGenerator getInstance() {
        if(instance == null)
            instance = new IdGenerator(DEFAULT_START, DEFAULT_STEP);
        return instance;
    }

    public static IdGenerator getInstance(int start, int step) {
        if(instance == null)
            instance = new IdGenerator(start, step);
        return instance;
    }

    private IdGenerator(int start, int step) {
        this.nextId = start;
        this.step = step;
    }

    public int next() {
        size++;
        int ret = nextId;
        nextId += step;
        return ret;
    }

    public int size() {
        return size;
    }

}
