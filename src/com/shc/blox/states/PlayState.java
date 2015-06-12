package com.shc.blox.states;

import com.shc.blox.Direction;
import com.shc.blox.Resources;
import com.shc.blox.entities.CameraSwitch;
import com.shc.blox.entities.Floor;
import com.shc.blox.entities.Goal;
import com.shc.blox.entities.Player;
import com.shc.silenceengine.collision.broadphase.DynamicTree3D;
import com.shc.silenceengine.collision.colliders.SceneCollider3D;
import com.shc.silenceengine.core.Display;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.core.GameState;
import com.shc.silenceengine.core.SilenceEngine;
import com.shc.silenceengine.graphics.Batcher;
import com.shc.silenceengine.graphics.Color;
import com.shc.silenceengine.graphics.Graphics2D;
import com.shc.silenceengine.graphics.cameras.PerspCam;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene3D;
import com.shc.silenceengine.scene.lights.PointLight;
import com.shc.silenceengine.utils.FileUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private Scene3D scene;

    private PerspCam camera;
    private Player   player;

    public static Direction cameraDirection;

    private static int level = 1;

    private PerspCam        camera2;
    private PointLight      camLight;
    private SceneCollider3D collider;
    private String          message;

    private static boolean reloadLevel = false;

    public PlayState()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);
        camera2 = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);

        loadLevel("levels/level" + level + ".lvl");
    }

    private void loadLevel(String filename)
    {
        reloadLevel = false;

        message = "";

        float x, z;

        if (scene != null)
            scene.destroy();

        scene = new Scene3D();

        collider = new SceneCollider3D(new DynamicTree3D());
        collider.setScene(scene);

        collider.register(Player.class, Floor.class);
        collider.register(Player.class, Goal.class);
        collider.register(CameraSwitch.class, Player.class);

        x = z = 0;

        String[] lines = FileUtils.readLinesToStringArray(FilePath.getResourceFile(filename));

        for (String line : lines)
        {
            if (line.trim().startsWith("#"))
                continue;

            if (line.trim().startsWith("@"))
            {
                message += line.trim().replaceFirst("@", "") + "\n";
                continue;
            }

            if (line.trim().startsWith("!"))
            {
                switch (line.trim().replaceFirst("!", "").trim().toUpperCase().charAt(0))
                {
                    case 'N': cameraDirection = Direction.NORTH; break;
                    case 'S': cameraDirection = Direction.SOUTH; break;
                    case 'E': cameraDirection = Direction.EAST;  break;
                    case 'W': cameraDirection = Direction.WEST;  break;
                }

                continue;
            }

            char[] tiles = line.toCharArray();

            for (char ch : tiles)
            {
                switch (ch)
                {
                    case 'G':
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        scene.addChild(new Goal(new Vector3(x, 3, z)));
                        break;

                    case 'P': scene.addChild(player = new Player(new Vector3(x, 5, z)));
                    case 'F': scene.addChild(new Floor(new Vector3(x, 0, z))); break;

                    case 'N':
                        scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.NORTH));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'E':
                        scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.EAST));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'W':
                        scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.WEST));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'S':
                        scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.SOUTH));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'f': scene.addChild(new Floor(new Vector3(x, 1, z))); break;

                    case 'n':
                        scene.addChild(new CameraSwitch(new Vector3(x, 2, z), Direction.NORTH));
                        scene.addChild(new Floor(new Vector3(x, 1, z)));
                        break;

                    case 'e':
                        scene.addChild(new CameraSwitch(new Vector3(x, 2, z), Direction.EAST));
                        scene.addChild(new Floor(new Vector3(x, 1, z)));
                        break;

                    case 'w':
                        scene.addChild(new CameraSwitch(new Vector3(x, 2, z), Direction.WEST));
                        scene.addChild(new Floor(new Vector3(x, 1, z)));
                        break;

                    case 's':
                        scene.addChild(new CameraSwitch(new Vector3(x, 2, z), Direction.SOUTH));
                        scene.addChild(new Floor(new Vector3(x, 1, z)));
                        break;
                }

                x++;
            }

            z++;
            x = 0;
        }

        camLight = null;
        scene.addComponent(camLight = new PointLight(new Vector3(), Color.WHITE));
    }

    @Override
    public void update(float delta)
    {
        Display.setTitle("UPS: " + Game.getUPS() + " | FPS: " + Game.getFPS() + " | RC: " + SilenceEngine.graphics.renderCallsPerFrame);

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

        // Smoothly interpolate the camera
        camera2.slerp(camera, delta * 4);

        if (camLight != null)
            camLight.setPosition(camera.getPosition());

        if (reloadLevel)
            Game.setGameState(new PlayState());
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.drawTexture(Resources.Textures.EARTH, 0, 0, Display.getWidth(), Display.getHeight());

        camera2.apply();
        scene.render(delta);

        g2d.setColor(Color.WHITE);
        g2d.drawString(message, 10, 10);
    }

    @Override
    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 1, 100);
        camera2.initProjection(70, Display.getAspectRatio(), 1, 100);
    }

    public static void nextLevel()
    {
        if (!FileUtils.resourceExists("levels/level" + (++level) + ".lvl"))
            level = 1;

        reloadLevel();
    }

    public static void reloadLevel()
    {
        reloadLevel = true;
    }
}
