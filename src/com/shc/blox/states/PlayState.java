package com.shc.blox.states;

import com.shc.blox.Direction;
import com.shc.blox.Resources;
import com.shc.blox.entities.CameraSwitch;
import com.shc.blox.entities.Collect;
import com.shc.blox.entities.Floor;
import com.shc.blox.entities.Goal;
import com.shc.blox.entities.Player;
import com.shc.blox.entities.Thorns;
import com.shc.blox.entities.ThunderBall;
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
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private Scene3D scene;

    private PerspCam camera;
    private Player   player;

    public static Direction cameraDirection;

    public static int LEVEL = 1;
    public static int SCORE = 0;

    private PerspCam        camera2;
    private PointLight      camLight;
    private SceneCollider3D collider;
    private String          message;

    private static boolean reloadLevel = false;
    private static boolean freeCamera = false;

    public PlayState()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);
        camera2 = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);

        loadLevel("levels/level" + LEVEL + ".lvl");
    }

    private void loadLevel(String filename)
    {
        reloadLevel = false;
        freeCamera = false;

        message = "";

        float x, z;

        if (scene != null)
            scene.destroy();

        scene = new Scene3D();

        collider = new SceneCollider3D(new DynamicTree3D());
        collider.setScene(scene);

        collider.register(Player.class, Floor.class);
        collider.register(Player.class, Goal.class);
        collider.register(Player.class, Thorns.class);
        collider.register(Player.class, Collect.class);
        collider.register(Player.class, ThunderBall.class);
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

                    case 'T':
                        scene.addChild(new ThunderBall(new Vector3(x, 1, z), MathUtils.random_range(0, 2) == 0 ? Direction.NORTH : Direction.SOUTH));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 't':
                        scene.addChild(new ThunderBall(new Vector3(x, 1, z), MathUtils.random_range(0, 2) == 0 ? Direction.EAST : Direction.WEST));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'C':
                        scene.addChild(new Collect(new Vector3(x, 3, z)));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

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

                    case 'X':
                        scene.addChild(new Thorns(new Vector3(x, 0.8f, z)));
                        scene.addChild(new Floor(new Vector3(x, 0, z)));
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

        if (Keyboard.isClicked(Keyboard.KEY_F1))
        {
            Display.setFullScreen(!Display.isFullScreen());

            if (Display.isFullScreen())
                Display.hideCursor();
            else
                Display.showCursor();
        }

        if (Keyboard.isClicked(Keyboard.KEY_TAB))
            freeCamera = !freeCamera;

        if (!freeCamera)
        {
            scene.update(delta);
            collider.checkCollisions();
        }

        SCORE = Math.max(SCORE, 0);

        if (!freeCamera)
        {
            switch (cameraDirection)
            {
                case NORTH:
                    camera.getRotation().set();
                    camera.setPosition(player.getPosition());
                    camera.moveUp(5);
                    camera.moveBackward(3);
                    camera.rotateX(-45);
                    break;

                case SOUTH:
                    camera.getRotation().set();
                    camera.setPosition(player.getPosition());
                    camera.moveUp(5);
                    camera.moveForward(3);
                    camera.rotateY(180);
                    camera.rotateX(-45);
                    break;

                case EAST:
                    camera.getRotation().set();
                    camera.setPosition(player.getPosition());
                    camera.moveUp(5);
                    camera.moveLeft(3);
                    camera.rotateY(-90);
                    camera.rotateX(-45);
                    break;

                case WEST:
                    camera.getRotation().set();
                    camera.setPosition(player.getPosition());
                    camera.moveUp(5);
                    camera.moveRight(3);
                    camera.rotateY(90);
                    camera.rotateX(-45);
                    break;
            }
        }
        else
        {
            if (Keyboard.isPressed('W'))
                camera.moveForward(4 * delta);

            if (Keyboard.isPressed('S'))
                camera.moveBackward(4 * delta);

            if (Keyboard.isPressed('A'))
                camera.moveLeft(4 * delta);

            if (Keyboard.isPressed('D'))
                camera.moveRight(4 * delta);

            if (Keyboard.isPressed('Q'))
                camera.moveUp(4 * delta);

            if (Keyboard.isPressed('E'))
                camera.moveDown(4 * delta);

            if (Keyboard.isPressed(Keyboard.KEY_UP))
                camera.rotateX(45 * delta);

            if (Keyboard.isPressed(Keyboard.KEY_DOWN))
                camera.rotateX(-45 * delta);

            if (Keyboard.isPressed(Keyboard.KEY_LEFT))
                camera.rotateY(45 * delta);

            if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
                camera.rotateY(-45 * delta);
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

        String scoreString = String.format("%d PTAS", SCORE);
        float x = Display.getWidth() - g2d.getFont().getWidth(scoreString) - 10;
        float y = Display.getHeight() - g2d.getFont().getHeight() - 10;
        g2d.drawString(scoreString, x, y);

        if (freeCamera)
        {
            g2d.setColor(Color.RED);
            g2d.drawRect(10, 10, Display.getWidth() - 20, Display.getHeight() - 20);
            g2d.drawRect(9, 9, Display.getWidth() - 18, Display.getHeight() - 18);
            g2d.drawString("FREE MODE", 20, Display.getHeight() - g2d.getFont().getHeight() - 20);
        }
    }

    @Override
    public void resize()
    {
        camera.initProjection(70, Display.getAspectRatio(), 1, 100);
        camera2.initProjection(70, Display.getAspectRatio(), 1, 100);
        SilenceEngine.graphics.getGraphics2D().getCamera().initProjection(Display.getWidth(), Display.getHeight());
    }

    public static void nextLevel()
    {
        if (!FilePath.getResourceFile("levels/level" + (++LEVEL) + ".lvl").exists())
        {
            LEVEL = 1;
            SCORE = 0;
        }

        reloadLevel();
    }

    public static void reloadLevel()
    {
        reloadLevel = true;
    }
}
