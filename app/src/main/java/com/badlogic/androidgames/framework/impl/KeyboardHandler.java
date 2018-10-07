package com.badlogic.androidgames.framework.impl;

import android.view.View;
import android.view.View.OnKeyListener;

import com.badlogic.androidgames.framework.Input.KeyEvent;
import com.badlogic.androidgames.framework.Pool;
import com.badlogic.androidgames.framework.Pool.PoolObjectFactory;

import java.util.ArrayList;
import java.util.List;

public class KeyboardHandler implements OnKeyListener {
    private final boolean[] pressedKeys = new boolean[128];
    private final Pool<KeyEvent> keyEventPool;
    private List<KeyEvent> keyEventsBuffer = new ArrayList<KeyEvent>();
    private List<KeyEvent> keyEvents = new ArrayList<KeyEvent>();
    private static final int MAXPOOLSIZE = 100;

    public KeyboardHandler(View view) {
        keyEventPool = new Pool.SimplePool<KeyEvent>(new PoolObjectFactory<KeyEvent>() {
            @Override
            public KeyEvent createObject() {
                return new KeyEvent();
            }
        }, MAXPOOLSIZE);
        view.setOnKeyListener(this);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
    }

    @Override
    public boolean onKey(View v, int keyCode, android.view.KeyEvent event) {
        if (event.getAction() == android.view.KeyEvent.ACTION_MULTIPLE)
            return false;

        synchronized (this) {
            KeyEvent keyEvent = keyEventPool.newObject();
            keyEvent.keyCode = keyCode;
            keyEvent.keyChar = (char) event.getUnicodeChar();
            if (event.getAction() == android.view.KeyEvent.ACTION_DOWN) {
                keyEvent.type = KeyEvent.KEY_DOWN;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = true;
            }
            if (event.getAction() == android.view.KeyEvent.ACTION_UP) {
                keyEvent.type = KeyEvent.KEY_UP;
                if(keyCode > 0 && keyCode < 127)
                    pressedKeys[keyCode] = false;
            }
            keyEventsBuffer.add(keyEvent);
        }
        return false;
    }

    public boolean isKeyPressed(int keyCode) {
        return keyCode >= 0 && keyCode <= 127 && pressedKeys[keyCode];
    }

    public List<KeyEvent> getKeyEvents() {
        synchronized (this) {
            int len = keyEvents.size();
            for (int i = 0; i < len; i++)
                keyEventPool.free(keyEvents.get(i));

            List<KeyEvent> tmp = keyEventsBuffer;
            keyEvents.clear();
            keyEventsBuffer = keyEvents;
            keyEvents = tmp;
            return keyEvents;

        }
    }
}
