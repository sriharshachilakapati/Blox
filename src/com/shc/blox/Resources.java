package com.shc.blox;

import com.shc.silenceengine.core.ResourceLoader;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.TrueTypeFont;
import com.shc.silenceengine.graphics.models.Model;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Resources
{
    private static ResourceLoader loader;

    public static final class Models
    {
        public static Model FLOOR;
        public static Model PLAYER;
        public static Model SPHERE;
        public static Model SPHERE2;
        public static Model CONE;
        public static Model COLLECT;
        public static Model SPIKES;
        public static Model EARTH;
    }

    public static void load()
    {
        loader = new ResourceLoader();

        int modFloorID   = loader.loadResource(Model.class, "resources/floor.obj");
        int modPlayerID  = loader.loadResource(Model.class, "resources/player.obj");
        int modSphereID  = loader.loadResource(Model.class, "resources/sphere.obj");
        int modSphere2ID = loader.loadResource(Model.class, "resources/sphere2.obj");
        int modConeID    = loader.loadResource(Model.class, "resources/cone.obj");
        int modCollectID = loader.loadResource(Model.class, "resources/piece.obj");
        int modSpikesID  = loader.loadResource(Model.class, "resources/spikes.obj");
        int modEarthID   = loader.loadResource(Model.class, "resources/earth.obj");

        int fontBloxID = loader.loadResource(TrueTypeFont.class, "resources/Blox2.ttf");

        loader.startLoading();

        Models.FLOOR   = loader.getResource(modFloorID);
        Models.PLAYER  = loader.getResource(modPlayerID);
        Models.SPHERE  = loader.getResource(modSphereID);
        Models.SPHERE2 = loader.getResource(modSphere2ID);
        Models.CONE    = loader.getResource(modConeID);
        Models.COLLECT = loader.getResource(modCollectID);
        Models.SPIKES  = loader.getResource(modSpikesID);
        Models.EARTH   = loader.getResource(modEarthID);

        // Modify the ambient color so that we can see the earth
        // (No light acts on it because it is not in scene)
        Models.EARTH.getMeshes().get(0).getMaterial().setAmbient(Color.GRAY);
        Models.EARTH.setPrefersStatic(true);

        TrueTypeFont font = ((TrueTypeFont) loader.getResource(fontBloxID)).setSize(30);
        SilenceEngine.graphics.getGraphics2D().setFont(font);
    }

    public static void dispose()
    {
        loader.dispose();
    }
}
