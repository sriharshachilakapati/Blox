package com.shc.blox.states;

import com.shc.blox.Level;
import com.shc.blox.Resources;
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
import com.shc.silenceengine.math.Transform;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.lights.PointLight;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.TimeUtils;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Sri Harsha Chilakapati
 */
public class IntroState extends GameState
{
    private final PointLight camLight;

    private Vector3[] demoPts;
    private PerspCam  gameCam;
    private PerspCam  viewCam;
    private PerspCam  earthCam;

    private Transform earthTransform;

    private Level level;

    private int currentPointIndex;
    private int currentMenuIndex;

    private MenuOption[] menuOptions;

    public IntroState()
    {
        level = Level.load(FilePath.getResourceFile("levels/intro.lvl"));

        gameCam = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);
        viewCam = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 100);
        earthCam = new PerspCam().initProjection(70, Display.getAspectRatio(), 1, 1000);
        earthCam.setPosition(new Vector3(0, 70, 50)).lookAt(Vector3.ZERO);

        currentPointIndex = 0;
        earthTransform = new Transform();

        GameTimer pointTimer = new GameTimer(3, TimeUtils.Unit.SECONDS);
        pointTimer.setCallback(() ->
        {
            if (Game.getGameState() != this)
                return;

            // Use a circular array index to switch between points
            currentPointIndex = (currentPointIndex + 1) % demoPts.length;

            pointTimer.start();
        });
        pointTimer.start();

        level.getScene().addComponent(camLight = new PointLight(new Vector3(), Color.WHITE));

        // The level view points, this is from where the camera looks at the level
        demoPts = new Vector3[]
                {
                        new Vector3(-2, 7, 13),
                        new Vector3(11, 7, 17),
                        new Vector3(25, 7, 20),
                        new Vector3(30, 7, 15)
                };

        // The menu options, this contains all the options
        menuOptions = MenuOption.values();
        currentMenuIndex = 0;
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked(Keyboard.KEY_ESCAPE))
            Game.end();

        if (Keyboard.isClicked(Keyboard.KEY_SPACE) || Keyboard.isClicked(Keyboard.KEY_ENTER))
        {
            switch (menuOptions[currentMenuIndex])
            {
                case PLAY:
                    Game.setGameState(new PlayState());
                    break;

                case ABOUT:
                    try
                    {
                        if (Desktop.isDesktopSupported())
                            Desktop.getDesktop().browse(new URL("https://github.com/sriharshachilakapati/Blox/").toURI());
                    }
                    catch (IOException | URISyntaxException e)
                    {
                        e.printStackTrace();
                    }
                    break;

                case QUIT:
                    Game.end();
                    break;
            }
        }

        if (Keyboard.isClicked(Keyboard.KEY_RIGHT))
            currentMenuIndex = (currentMenuIndex + 1) % menuOptions.length;

        if (Keyboard.isClicked(Keyboard.KEY_LEFT))
            currentMenuIndex = (currentMenuIndex + menuOptions.length - 1) % menuOptions.length;

        level.update(delta);

        gameCam.setPosition(demoPts[currentPointIndex])
                .lookAt(level.getCenter())
                .moveRight(10)
                .lookAt(level.getCenter());

        viewCam.slerp(gameCam, delta);
        camLight.setPosition(viewCam.getPosition());

        earthTransform.rotateSelf(0, 5 * delta, 0);
        earthCam.setPosition(earthCam.getPosition().set(0, 70, 50).addSelf(viewCam.getPosition())).lookAt(Vector3.ZERO);
    }

    @Override
    public void render(float delta, Batcher batcher)
    {
        earthCam.apply();
        Resources.Models.EARTH.render(earthTransform);

        viewCam.apply();
        level.render(delta);

        Graphics2D g2d = SilenceEngine.graphics.getGraphics2D();
        g2d.setFont(Resources.Fonts.TITLE_FONT);

        String title = "BLOX";

        if (currentPointIndex % 2 == 0)
            title = title.toLowerCase();

        float x = Display.getWidth() / 2 - g2d.getFont().getWidth(title) / 2;
        float y = 150;

        g2d.setColor(Color.BURLY_WOOD);
        g2d.drawString(title, x, y);
        g2d.setColor(Color.MAROON);
        g2d.drawString(title, x - 5, y);

        g2d.setColor(Color.GRAY);
        g2d.setFont(Resources.Fonts.HUD_FONT);

        String message = "press SPACE to select";
        x = Display.getWidth() / 2 - g2d.getFont().getWidth(message) / 2;
        y = Display.getHeight() - g2d.getFont().getHeight() - 50;

        g2d.drawString(message, x, y);

        g2d.setColor(Color.DARK_SLATE_GRAY);
        g2d.setFont(Resources.Fonts.MENU_FONT);

        message = "< " + menuOptions[currentMenuIndex].getMessage() + " >";
        x = Display.getWidth() / 2 - g2d.getFont().getWidth(message) / 2;
        y = Display.getHeight() - g2d.getFont().getHeight() - 100;

        g2d.drawString(message, x - 1, y - 1);
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawString(message, x + 1, y + 1);
    }

    @Override
    public void resize()
    {
        gameCam.initProjection(70, Display.getAspectRatio(), 1, 100);
        viewCam.initProjection(70, Display.getAspectRatio(), 1, 100);
        earthCam.initProjection(70, Display.getAspectRatio(), 1, 1000);
        SilenceEngine.graphics.getGraphics2D().getCamera().initProjection(Display.getWidth(), Display.getHeight());
    }

    @Override
    public void onLeave()
    {
        level.destroy();
    }

    private enum MenuOption
    {
        PLAY("Play the Game"),
        ABOUT("About"),
        QUIT("Quit");

        private String message;

        MenuOption(String message)
        {
            this.message = message;
        }

        public String getMessage()
        {
            return message;
        }
    }
}
