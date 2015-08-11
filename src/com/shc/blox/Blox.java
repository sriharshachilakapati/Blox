package com.shc.blox;

import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blox extends Game
{
    @Override
    public void preInit()
    {
        Display.setTitle("Blox - SilenceEngine 3D Demo");
        Display.setSize(1366, 768);
    }

    @Override
    public void init()
    {
        Resources.load();
        PlayerController.map();

        setGameState(new PlayState());
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
