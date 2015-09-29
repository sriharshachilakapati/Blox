package com.shc.blox.states;

import com.shc.blox.Direction;
import com.shc.blox.Level;
import com.shc.blox.PlayerController;
import com.shc.blox.Resources;
import com.shc.blox.entities.CameraSwitch;
import com.shc.blox.entities.Collect;
import com.shc.blox.entities.Floor;
import com.shc.blox.entities.Goal;
import com.shc.blox.entities.Player;
import com.shc.blox.entities.Spikes;
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
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.lights.PointLight;

/**
 * @author Sri Harsha Chilakapati
 */
public class PlayState extends GameState
{
    private static boolean reloadLevel = false;
    private static boolean freeCamera  = false;

    public static Direction cameraDirection;

    public static int LEVEL = 1;
    public static int SCORE = 0;

    private Level level;

    private PerspCam camera;
    private PerspCam camera2;
    private PerspCam camera3;

    private PointLight      camLight;
    private SceneCollider3D collider;

    private Transform earthTransform = new Transform();

    public PlayState()
    {
        camera = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);
        camera2 = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);
        camera3 = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 1000);
        camera3.setPosition(new Vector3(0, 70, 50)).lookAt(Vector3.ZERO);

        loadLevel("levels/level" + LEVEL + ".lvl");
    }

    public static boolean isFreeCamera()
    {
        return freeCamera;
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

    private void loadLevel(String filename)
    {
        reloadLevel = false;
        freeCamera = false;

        if (level != null)
            level.destroy();

        level = Level.load(filename);
        cameraDirection = level.getInitialDirection();

        collider = new SceneCollider3D(new DynamicTree3D());
        collider.setScene(level.getScene());

        collider.register(Player.class, Floor.class);
        collider.register(Player.class, Goal.class);
        collider.register(Player.class, Spikes.class);
        collider.register(Player.class, Collect.class);
        collider.register(Player.class, ThunderBall.class);
        collider.register(CameraSwitch.class, Player.class);

        level.getScene().addComponent(camLight = new PointLight(new Vector3(), Color.WHITE));
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.setGameState(new IntroState());

        if (Keyboard.isClicked(Keyboard.KEY_TAB) || Controller.isClicked(PlayerController.BUTTON_FREEMODE, 0))
        {
            freeCamera = !freeCamera;

            // Update as play mode to notify ThunderBalls
            updatePlayMode(delta);
        }

        if (freeCamera)
            updateFreeMode(delta);
        else
            updatePlayMode(delta);

        SCORE = Math.max(SCORE, 0);

        // Smoothly interpolate the camera
        camera2.slerp(camera, delta * 4);

        if (camLight != null)
            camLight.setPosition(camera.getPosition());

        if (reloadLevel)
            Game.setGameState(new PlayState());

        earthTransform.rotateSelf(0, 5 * delta, 0);
        camera3.setPosition(camera3.getPosition().set(0, 70, 50).addSelf(camera2.getPosition())).lookAt(Vector3.ZERO);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setFont(Resources.Fonts.HUD_FONT);

        camera3.apply();

        // Render the static earth model
        Resources.Models.EARTH.render(earthTransform);

        camera2.apply();
        level.render(delta);

        g2d.setColor(Color.WHITE);
        g2d.drawString(level.getMessage(), 10, 10);

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
        camera3.initProjection(70, Display.getAspectRatio(), 1, 1000);
        SilenceEngine.graphics.getGraphics2D().getCamera().initProjection(Display.getWidth(), Display.getHeight());
    }

    private void updatePlayMode(float delta)
    {
        level.update(delta);
        collider.checkCollisions();

        Player player = level.getPlayer();

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

    private void updateFreeMode(float delta)
    {
        if (Keyboard.isPressed('W') || Controller.isPressed(PlayerController.BUTTON_DPAD_UP, 0))
            camera.moveForward(4 * delta);

        if (Keyboard.isPressed('S') || Controller.isPressed(PlayerController.BUTTON_DPAD_DOWN, 0))
            camera.moveBackward(4 * delta);

        if (Keyboard.isPressed('A') || Controller.isPressed(PlayerController.BUTTON_DPAD_LEFT, 0))
            camera.moveLeft(4 * delta);

        if (Keyboard.isPressed('D') || Controller.isPressed(PlayerController.BUTTON_DPAD_RIGHT, 0))
            camera.moveRight(4 * delta);

        if (Keyboard.isPressed('Q') || Controller.isPressed(PlayerController.BUTTON_FREEMODE_PHEIGHT, 0))
            camera.moveUp(4 * delta);

        if (Keyboard.isPressed('E') || Controller.isPressed(PlayerController.BUTTON_FREEMODE_NHEIGHT, 0))
            camera.moveDown(4 * delta);

        if (Keyboard.isPressed(Keyboard.KEY_UP))
            camera.rotateX(45 * delta);

        if (Keyboard.isPressed(Keyboard.KEY_DOWN))
            camera.rotateX(-45 * delta);

        if (Keyboard.isPressed(Keyboard.KEY_LEFT))
            camera.rotateY(45 * delta);

        if (Keyboard.isPressed(Keyboard.KEY_RIGHT))
            camera.rotateY(-45 * delta);

        camera.rotateX(Controller.getAxe(PlayerController.AXE_RS_Y, 0) * 45 * delta);
        camera.rotateY(Controller.getAxe(PlayerController.AXE_RS_X, 0) * 45 * delta);
    }
}
