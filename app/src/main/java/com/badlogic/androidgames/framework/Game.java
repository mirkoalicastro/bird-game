package com.badlogic.androidgames.framework;

public interface Game {
    Input getInput();

    FileIO getFileIO();

    Graphics getGraphics();

    Audio getAudio();

    void setScreen(Screen screen);

    Screen getCurrentScreen();

    void display();

}