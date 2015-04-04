package com.shc.blox;

import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.opengl.Texture;
import com.shc.silenceengine.models.Model;
import com.shc.silenceengine.utils.FileUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Blox extends Game
{
    public static Model PLAYER;
    public static Model FLOOR;
    public static Model CONE;
    public static Model SPHERE;

    public static Texture EARTH;

    public static TrueTypeFont FONT;

    @Override
    public void init()
    {
        Display.setTitle("Blox - SilenceEngine 3D Demo");
        Display.setSize(1280, 720);
        Display.centerOnScreen();

//        Display.setFullScreen(true);
//        Display.hideCursor();

        PLAYER = Model.load("resources/player.obj");
        FLOOR  = Model.load("resources/floor.obj");
        CONE   = Model.load("resources/cone.obj");
        SPHERE = Model.load("resources/sphere.obj");

        EARTH = Texture.fromResource("resources/texture-earth.png");

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setFont(FONT = new TrueTypeFont(FileUtils.getResource("resources/Blox2.ttf"), TrueTypeFont.STYLE_NORMAL, 30, true));

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
        SPHERE.dispose();
        EARTH.dispose();
        FONT.dispose();
    }

    public static void main(String[] args)
    {
        new Blox().start();
    }
}
