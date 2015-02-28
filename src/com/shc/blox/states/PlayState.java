package com.shc.blox.states;

import com.shc.blox.Direction;
import com.shc.blox.entities.Cone;
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
    public PerspCam camera;
    public PerspCam camera2;
    public static Player player;

    public static Direction cameraDirection;

    private PointLight camLight;

    private ISceneCollider3D collider;

    public PlayState()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        camera2 = new PerspCam().initProjection(70, Display.getAspectRatio(), 0.01f, 100f);

        scene = new Scene();
        scene.addComponent(camLight = new PointLight(new Vector3(), Color.WHITE));
        scene.init();

        collider = new DynamicSceneCollider3D();
        collider.setScene(scene);

        collider.register(Player.class, Floor.class);
        collider.register(Cone.class, Player.class);

        loadLevel("levels/level1.lvl");

        cameraDirection = Direction.NORTH;
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
                    case 'F': scene.addChild(new Floor(new Vector3(x, 0, z))); break;

                    case 'N':
                        scene.addChild(new Cone(new Vector3(x, 1, z), Direction.NORTH));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'E':
                        scene.addChild(new Cone(new Vector3(x, 1, z), Direction.EAST));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'W':
                        scene.addChild(new Cone(new Vector3(x, 1, z), Direction.WEST));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'S':
                        scene.addChild(new Cone(new Vector3(x, 1, z), Direction.SOUTH));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;
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

        switch (cameraDirection)
        {
            case NORTH:
                camera.getRotation().set();
                camera.setPosition(player.getPosition());
                camera.moveUp(5);
                camera.moveBackward(5);
                camera.rotateX(-45);
                break;

            case SOUTH:
                camera.getRotation().set();
                camera.setPosition(player.getPosition());
                camera.moveUp(5);
                camera.moveForward(5);
                camera.rotateY(180);
                camera.rotateX(-45);
                break;

            case EAST:
                camera.getRotation().set();
                camera.setPosition(player.getPosition());
                camera.moveUp(5);
                camera.moveLeft(5);
                camera.rotateY(-90);
                camera.rotateX(-45);
                break;

            case WEST:
                camera.getRotation().set();
                camera.setPosition(player.getPosition());
                camera.moveUp(5);
                camera.moveRight(5);
                camera.rotateY(90);
                camera.rotateX(-45);
                break;
        }

        camLight.setPosition(camera.getPosition());

        // Smoothly interpolate the camera
        camera2.slerp(camera, delta * 3);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        camera2.apply();
        scene.render(delta, batcher);
    }

    @Override
    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
        camera2.initProjection(70, Display.getAspectRatio(), 0.01f, 100f);
    }
}
