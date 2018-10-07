/** Very similar to B.A.G.
 * Minor changes by Marco Faella, marfaella@gmail.com
 */

package com.badlogic.androidgames.framework.impl;

import android.view.MotionEvent;
import android.view.View;

import com.badlogic.androidgames.framework.Input.TouchEvent;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class MultiTouchHandler implements TouchHandler {
    private final boolean[] isTouching = new boolean[20];
    private final int[] touchX = new int[20];
    private final int[] touchY = new int[20];
    private final Pool<TouchEvent> touchEventPool;
    private List<TouchEvent> touchEvents = new ArrayList<TouchEvent>();
    private List<TouchEvent> touchEventsBuffer = new ArrayList<TouchEvent>();
    private final float scaleX;
    private final float scaleY;
    private static final int MAXPOOLSIZE = 100;

    public MultiTouchHandler(View view, float scaleX, float scaleY) {
        touchEventPool = new Pool.SimplePool<>(new PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        }, MAXPOOLSIZE);
        view.setOnTouchListener(this);

        this.scaleX = scaleX;
        this.scaleY = scaleY;
    }

    @Override
    public synchronized boolean onTouch(View v, MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        TouchEvent touchEvent;

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_DOWN;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                isTouching[pointerId] = true;
                touchEventsBuffer.add(touchEvent);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL:
                touchEvent = touchEventPool.newObject();
                touchEvent.type = TouchEvent.TOUCH_UP;
                touchEvent.pointer = pointerId;
                touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                isTouching[pointerId] = false;
                touchEventsBuffer.add(touchEvent);
                break;

            case MotionEvent.ACTION_MOVE:
                int pointerCount = event.getPointerCount();
                for (int i = 0; i < pointerCount; i++) {
                    pointerIndex = i;
                    pointerId = event.getPointerId(pointerIndex);

                    touchEvent = touchEventPool.newObject();
                    touchEvent.type = TouchEvent.TOUCH_DRAGGED;
                    touchEvent.pointer = pointerId;
                    touchEvent.x = touchX[pointerId] = (int) (event.getX(pointerIndex) * scaleX);
                    touchEvent.y = touchY[pointerId] = (int) (event.getY(pointerIndex) * scaleY);
                    touchEventsBuffer.add(touchEvent);
                }
                break;
        }

        return true;
    }

    @Override
    public synchronized  boolean isTouchDown(int pointer) {
        return pointer >= 0 && pointer < 20 && isTouching[pointer];
    }

    @Override
    public synchronized int getTouchX(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return 0;
        else
            return touchX[pointer];
    }

    @Override
    public synchronized int getTouchY(int pointer) {
        if (pointer < 0 || pointer >= 20)
            return 0;
        else
            return touchY[pointer];
    }

    @Override
    public synchronized List<TouchEvent> getTouchEvents() {
        for (TouchEvent event: touchEvents)
                touchEventPool.free(event);

        List<TouchEvent> tmp = touchEventsBuffer;
        touchEvents.clear();
        touchEventsBuffer = touchEvents;
        touchEvents = tmp;
        return touchEvents;

    }
}
