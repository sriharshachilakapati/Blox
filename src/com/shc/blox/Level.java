package com.shc.blox;

import com.shc.blox.entities.CameraSwitch;
import com.shc.blox.entities.Collect;
import com.shc.blox.entities.Floor;
import com.shc.blox.entities.Goal;
import com.shc.blox.entities.Player;
import com.shc.blox.entities.Spikes;
import com.shc.blox.entities.ThunderBall;
import com.shc.silenceengine.io.FilePath;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.scene.Scene3D;
import com.shc.silenceengine.utils.FileUtils;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public final class Level
{
    private String    message;
    private Scene3D   scene;
    private Player    player;
    private Direction initialDirection;

    private Level()
    {
    }

    public static Level load(String resourceName)
    {
        return load(FilePath.getResourceFile(resourceName));
    }

    public static Level load(FilePath filePath)
    {
        Level level = new Level();

        level.scene = new Scene3D();
        level.message = "";

        float x = 0, z = 0;

        String[] lines = FileUtils.readLinesToStringArray(filePath);

        for (String line : lines)
        {
            if (line.trim().startsWith("#"))
                continue;

            if (line.trim().startsWith("@"))
            {
                level.message += line.trim().replaceFirst("@", "") + "\n";
                continue;
            }

            if (line.trim().startsWith("!"))
            {
                switch (line.trim().replaceFirst("!", "").trim().toUpperCase().charAt(0))
                {
                    case 'N':
                        level.initialDirection = Direction.NORTH;
                        break;
                    case 'S':
                        level.initialDirection = Direction.SOUTH;
                        break;
                    case 'E':
                        level.initialDirection = Direction.EAST;
                        break;
                    case 'W':
                        level.initialDirection = Direction.WEST;
                        break;
                }

                continue;
            }

            char[] tiles = line.toCharArray();

            for (char ch : tiles)
            {
                switch (ch)
                {
                    case 'G':
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        level.scene.addChild(new Goal(new Vector3(x, 3, z)));
                        break;

                    case 'P':
                        level.scene.addChild(level.player = new Player(new Vector3(x, 5, z)));
                    case 'F':
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'T':
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        level.scene.addChild(new ThunderBall(new Vector3(x, 1, z), MathUtils.random_range(0, 2) == 0
                                                                                   ? Direction.NORTH
                                                                                   : Direction.SOUTH));
                        break;

                    case 't':
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        level.scene.addChild(new ThunderBall(new Vector3(x, 1, z), MathUtils.random_range(0, 2) == 0
                                                                                   ? Direction.EAST
                                                                                   : Direction.WEST));
                        break;

                    case 'C':
                        level.scene.addChild(new Collect(new Vector3(x, 3, z)));
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'N':
                        level.scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.NORTH));
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'E':
                        level.scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.EAST));
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'W':
                        level.scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.WEST));
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'S':
                        level.scene.addChild(new CameraSwitch(new Vector3(x, 1, z), Direction.SOUTH));
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;

                    case 'X':
                        level.scene.addChild(new Spikes(new Vector3(x, 0.8f, z)));
                        level.scene.addChild(new Floor(new Vector3(x, 0, z)));
                        break;
                }

                x++;
            }

            z++;
            x = 0;
        }

        return level;
    }

    public void update(float delta)
    {
        scene.update(delta);
    }

    public void render(float delta)
    {
        scene.render(delta);
    }

    public void destroy()
    {
        scene.destroy();
    }

    public String getMessage()
    {
        return message;
    }

    public Scene3D getScene()
    {
        return scene;
    }

    public Player getPlayer()
    {
        return player;
    }

    public Direction getInitialDirection()
    {
        return initialDirection;
    }
}
