package com.badlogic.androidgames.framework;

import java.util.List;

public interface Joystick extends Button {
    List<Input.TouchEvent> processAndRelease(List<Input.TouchEvent> events);
    float getAngle();
    float getDistance();
    float getNormX();
    float getNormY();
}