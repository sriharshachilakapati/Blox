package com.shc.blox;

import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    public static final class Textures
    {
        public static Texture EARTH;
    }

    public static final class Models
    {
        public static Model FLOOR;
        public static Model PLAYER;
        public static Model SPHERE;
        public static Model CONE;
    }

    public static void load()
    {
        ResourceLoader loader = ResourceLoader.getInstance();

        int texEarthID = loader.defineTexture("resources/texture-earth.png");

        int modFloorID = loader.defineModel("resources/floor.obj");
        int modPlayerID = loader.defineModel("resources/player.obj");
        int modSphereID = loader.defineModel("resources/sphere.obj");
        int modConeID = loader.defineModel("resources/cone.obj");

        int fontBloxID = loader.defineFont("resources/Blox2.ttf", TrueTypeFont.STYLE_NORMAL, 30);

        loader.startLoading();

        Textures.EARTH = loader.getTexture(texEarthID);
        Models.FLOOR = loader.getModel(modFloorID);
        Models.PLAYER = loader.getModel(modPlayerID);
        Models.SPHERE = loader.getModel(modSphereID);
        Models.CONE = loader.getModel(modConeID);

        SilenceEngine.graphics.getGraphics2D().setFont(loader.getFont(fontBloxID));
    }

    public static void dispose()
    {
        ResourceLoader.getInstance().dispose();
    }
}
