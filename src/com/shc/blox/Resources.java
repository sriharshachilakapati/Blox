package com.shc.blox;

import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.models.Model;
import com.shc.silenceengine.graphics.opengl.Texture;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    private static ResourceLoader loader;

    public static final class Textures
    {
        public static Texture EARTH;
    }

    public static final class Models
    {
        public static Model FLOOR;
        public static Model PLAYER;
        public static Model SPHERE;
        public static Model SPHERE2;
        public static Model CONE;
        public static Model COLLECT;
    }

    public static void load()
    {
        loader = new ResourceLoader();

        int texEarthID = loader.loadResource(Texture.class, "resources/texture-earth.png");

        int modFloorID   = loader.loadResource(Model.class, "resources/floor.obj");
        int modPlayerID  = loader.loadResource(Model.class, "resources/player.obj");
        int modSphereID  = loader.loadResource(Model.class, "resources/sphere.obj");
        int modSphere2ID = loader.loadResource(Model.class, "resources/sphere2.obj");
        int modConeID    = loader.loadResource(Model.class, "resources/cone.obj");
        int modCollectID = loader.loadResource(Model.class, "resources/piece.obj");

        int fontBloxID = loader.loadResource(TrueTypeFont.class, "resources/Blox2.ttf");

        loader.startLoading();

        Textures.EARTH = loader.getResource(texEarthID);
        Models.FLOOR   = loader.getResource(modFloorID);
        Models.PLAYER  = loader.getResource(modPlayerID);
        Models.SPHERE  = loader.getResource(modSphereID);
        Models.SPHERE2 = loader.getResource(modSphere2ID);
        Models.CONE    = loader.getResource(modConeID);
        Models.COLLECT = loader.getResource(modCollectID);

        // Tweak it a bit since OBJ doesn't support opacity attribute
        Models.SPHERE2.getMeshes().get(0).getMaterial().setDiffuse(Color.TRANSPARENT).setAmbient(Color.GRAY);

        TrueTypeFont font = loader.getResource(fontBloxID);
        font.setSize(30);
        SilenceEngine.graphics.getGraphics2D().setFont(font);

    }

    public static void dispose()
    {
        loader.dispose();
    }
}
