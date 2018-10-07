package com.badlogic.androidgames.framework;

public interface Audio {
    Music newMusic(String filename);

    Sound newSound(String filename);
}
