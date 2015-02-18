package com.shc.blox.states;

import com.shc.blox.ControllerMapping;
import com.shc.blox.entities.Floor;
import com.shc.blox.entities.Player;
import com.shc.silenceengine.collision.colliders.DynamicSceneCollider3D;
import com.shc.silenceengine.collision.colliders.ISceneCollider3D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene;
import com.shc.silenceengine.scene.lights.PointLight;
import com.shc.silenceengine.utils.FileUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    public static Scene scene;
    public static PerspCam camera;
    public static Player player;

    private PointLight camLight;

    private ISceneCollider3D collider;

    public PlayState()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);

        scene = new Scene();
        scene.addComponent(camLight = new PointLight(new Vector3(), Color.WHITE));
        scene.init();

        collider = new DynamicSceneCollider3D();
        collider.setScene(scene);

        collider.register(Player.class, Floor.class);

        camera.setPosition(new Vector3(0, 4, 4));
        camera.lookAt(Vector3.ZERO);

        loadLevel("levels/level1.lvl");
    }

    private void loadLevel(String filename)
    {
        float x, z;

        // Remove all the children
        scene.removeChildren();

        x = z = 0;

        String[] lines = FileUtils.readLinesToStringArray(FileUtils.getResource(filename));

        for (String line : lines)
        {
            char[] tiles = line.toCharArray();

            for (char ch : tiles)
            {
                switch (ch)
                {
                    case 'P': scene.addChild(player = new Player(new Vector3(x, 5, z)));
                    case 'F': scene.addChild(new Floor(new Vector3(x, 0, z)));
                }

                x++;
            }

            z--;
            x = 0;
        }

        scene.init();
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        scene.update(delta);
        collider.checkCollisions();

        Vector3 temp = Vector3.REUSABLE_STACK.pop();
        camera.setPosition(temp.set(player.getPosition()).addSelf(0, 4, 3));
        camLight.setPosition(camera.getPosition());
        Vector3.REUSABLE_STACK.push(temp);

        camera.rotateX(-Controller.getAxe(ControllerMapping.AXE_RS_Y, 0));
        camera.rotateY(-Controller.getAxe(ControllerMapping.AXE_RS_X, 0));

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            camera.rotateX(1);
        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            camera.rotateX(-1);

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            camera.rotateY(1);
        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            camera.rotateY(-1);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        camera.apply();
        scene.render(delta, batcher);
    }

    @Override
    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
    }
}
