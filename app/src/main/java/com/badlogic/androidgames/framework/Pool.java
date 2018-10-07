package com.badlogic.androidgames.framework;

import java.util.ArrayList;
import java.util.List;

public abstract class Pool<T> {
    public interface PoolObjectFactory<T> {
        T createObject();
    }

    public abstract T newObject();
    public abstract void free(T object);

    public static class SynchronizedPool<T> extends SimplePool<T> {
        private final Object lock = new Object();
        public SynchronizedPool(PoolObjectFactory<T> factory, int maxSize) {
            super(factory, maxSize);
        }

        public T newObject() {
            synchronized (lock) {
                return super.newObject();
            }
        }

        public void free(T object) {
            synchronized (lock) {
                super.free(object);
            }
        }

    }

    public static class SimplePool<T> extends Pool<T> {

        private final List<T> freeObjects;
        private final PoolObjectFactory<T> factory;
        private final int maxSize;

        public SimplePool(PoolObjectFactory<T> factory, int maxSize) {
            this.factory = factory;
            this.maxSize = maxSize;
            this.freeObjects = new ArrayList<T>(maxSize);
        }

        public T newObject() {
            T object;

            if (freeObjects.size() == 0)
                object = factory.createObject();
            else
                object = freeObjects.remove(freeObjects.size() - 1);

            return object;
        }

        public void free(T object) {
            if (freeObjects.size() < maxSize)
                freeObjects.add(object);
        }
    }
}
