package com.shc.blox;

import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.models.Model;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blox extends Game
{
    public static Model PLAYER;
    public static Model FLOOR;
    public static Model CONE;

    public static Texture EARTH;

    @Override
    public void init()
    {
        Display.setTitle("Blox - SilenceEngine 3D Demo");
        Display.setSize(1280, 720);
        Display.centerOnScreen();

        Display.setFullScreen(true);
        Display.hideCursor();

        PLAYER = Model.load("resources/player.obj");
        FLOOR  = Model.load("resources/floor.obj");
        CONE   = Model.load("resources/cone.obj");

        EARTH = Texture.fromResource("resources/texture-earth.png");

        ControllerMapping.mapController();

        setGameState(new PlayState());

        // Set to 10% Blue
        SilenceEngine.graphics.setClearColor(0, 0, 0.1f, 1);
    }

    @Override
    public void dispose()
    {
        PLAYER.dispose();
        FLOOR.dispose();
        CONE.dispose();

        EARTH.dispose();
    }

    public static void main(String[] args)
    {
        new Blox().start();
    }
}
