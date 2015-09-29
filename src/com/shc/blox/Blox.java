package com.shc.blox;

import com.shc.blox.states.IntroState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.core.SilenceException;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.utils.Logger;

import java.io.IOException;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blox extends Game
{
    public static void main(String[] args)
    {
        new Blox().start();
    }

    @Override
    public void preInit()
    {
        try
        {
            Logger.addLogStream(FilePath.getExternalFile("blox.log"));
        }
        catch (IOException e)
        {
            SilenceException.reThrow(e);
        }

        Display.setTitle("Blox - SilenceEngine 3D Demo");
        Display.setSize(1366, 768);
    }

    @Override
    public void init()
    {
        Resources.load();
        PlayerController.map();

        setGameState(new IntroState());
    }

    @Override
    public void update(float delta)
    {
        Display.setTitle("UPS: " + Game.getUPS() +
                         " | FPS: " + Game.getFPS() +
                         " | RC: " + SilenceEngine.graphics.renderCallsPerFrame +
                         " | BS: " + SilenceEngine.graphics.getBatcher().getBatchSize() + " verts" +
                         " | MBS: " + SilenceEngine.graphics.getBatcher().getMaxBatchSize() + " verts");

        if (Keyboard.isClicked(Keyboard.KEY_F1))
        {
            Display.setFullScreen(!Display.isFullScreen());

            if (Display.isFullScreen())
                Display.hideCursor();
            else
                Display.showCursor();
        }
    }

    @Override
    public void dispose()
    {
        Resources.dispose();
    }
}
