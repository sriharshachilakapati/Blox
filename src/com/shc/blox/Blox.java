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

//        Display.setFullScreen(true);
//        Display.hideCursor();

        PLAYER = Model.load("resources/player.obj");
        FLOOR  = Model.load("resources/floor.obj");
        CONE   = Model.load("resources/cone.obj");

        EARTH = Texture.fromResource("resources/texture-earth.png");

        ControllerMapping.mapController();

        setGameState(new PlayState());
        SilenceEngine.graphics.setClearColor(0.2f, 0.2f, 0.4f, 1f);
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
