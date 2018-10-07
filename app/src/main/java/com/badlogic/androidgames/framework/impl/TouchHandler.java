package com.badlogic.androidgames.framework.impl;

import android.view.View.OnTouchListener;

import com.badlogic.androidgames.framework.Input.TouchEvent;

import java.util.List;

public interface TouchHandler extends OnTouchListener {
    boolean isTouchDown(int pointer);
    
    int getTouchX(int pointer);

    int getTouchY(int pointer);
    
    List<TouchEvent> getTouchEvents();
}
