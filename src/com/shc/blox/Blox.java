package com.shc.blox;

import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.models.Model;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blox extends Game
{
    public static Model PLAYER;
    public static Model FLOOR;

    @Override
    public void init()
    {
        Display.setTitle("Blox - SilenceEngine 3D Demo");
        Display.setClearColor(new Color(0.1f, 0.1f, 0.2f));

        PLAYER = Model.load("resources/player.obj");
        FLOOR  = Model.load("resources/floor.obj");

        setGameState(new PlayState());
    }

    public static void main(String[] args)
    {
        new Blox().start();
    }
}
