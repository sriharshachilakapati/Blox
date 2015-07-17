package com.shc.blox;

import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blox extends Game
{
    @Override
    public void preInit()
    {
        Display.setTitle("Blox - SilenceEngine 3D Demo");

        Display.setFullScreen(true);
        Display.hideCursor();
    }

    @Override
    public void init()
    {
        Resources.load();
        ControllerMapping.mapController();

        setGameState(new PlayState());

        // Set to 10% Blue
        SilenceEngine.graphics.setClearColor(0, 0, 0.1f, 1);
    }

    @Override
    public void dispose()
    {
        Resources.dispose();
    }

    public static void main(String[] args)
    {
        new Blox().start();
    }
}
