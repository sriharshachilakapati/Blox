package com.shc.blox.entities;

import com.shc.blox.Direction;
import com.shc.blox.Resources;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Sphere;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.MathUtils;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class ThunderBall extends Entity3D
{
    private Direction velocityDir;

    private boolean accepted;
    private boolean growing;
    private Player  player;

    private Vector3 originalPos;

    private float scale = 1;

    private GameTimer timer;

    public ThunderBall(Vector3 position, Direction direction)
    {
        super(Resources.Models.SPHERE2, new Sphere(position, 0.5f));
        velocityDir = direction;

        originalPos = new Vector3(position);

        switch (velocityDir)
        {
            case NORTH:
                setPosition(position.addSelf(+0, +0, +3));
                break;
            case EAST:
                setPosition(position.addSelf(-3, +0, +0));
                break;
            case SOUTH:
                setPosition(position.addSelf(+0, +0, -3));
                break;
            case WEST:
                setPosition(position.addSelf(+3, +0, +0));
                break;
        }

        timer = new GameTimer(1.5, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            switch (velocityDir)
            {
                case NORTH:
                    velocityDir = Direction.SOUTH;
                    break;
                case EAST:
                    velocityDir = Direction.WEST;
                    break;
                case SOUTH:
                    velocityDir = Direction.NORTH;
                    break;
                case WEST:
                    velocityDir = Direction.EAST;
                    break;
            }
        });
        timer.start();
    }

    @Override
    public void update(float delta)
    {
        float random = MathUtils.random_range(0, 360);
        setRotation(random, random, random);

        getVelocity().set(0);

        if (PlayState.isFreeCamera())
            timer.stop();
        else if (!timer.isActive() && !accepted)
        {
            Vector3 position = Vector3.REUSABLE_STACK.pop();

            // Move to the extreme position and then start the timer again
            switch (velocityDir)
            {
                case NORTH:
                    position.set(+0, +0, +3).addSelf(originalPos);
                    break;
                case EAST:
                    position.set(-3, +0, +0).addSelf(originalPos);
                    break;
                case SOUTH:
                    position.set(+0, +0, -3).addSelf(originalPos);
                    break;
                case WEST:
                    position.set(+3, +0, +0).addSelf(originalPos);
                    break;
            }

            if (moveTo(position, 4 * delta))
                timer.start();

            Vector3.REUSABLE_STACK.push(position);
        }
        else
        {
            if (!accepted)
                switch (velocityDir)
                {
                    case NORTH:
                        getVelocity().set(+0, +0, -4).scaleSelf(delta);
                        break;
                    case EAST:
                        getVelocity().set(+4, +0, +0).scaleSelf(delta);
                        break;
                    case SOUTH:
                        getVelocity().set(+0, +0, +4).scaleSelf(delta);
                        break;
                    case WEST:
                        getVelocity().set(-4, +0, +0).scaleSelf(delta);
                        break;
                }
            else
            {
                player.moveTo(getPosition(), 2 * delta);

                if (growing)
                {
                    scale += 4 * delta;
                    setScale(scale, scale, scale);

                    if (scale > 3)
                    {
                        player.destroy();
                        growing = false;
                    }
                }
                else
                {
                    scale -= 4 * delta;
                    setScale(scale, scale, scale);

                    if (scale < 0.1)
                    {
                        PlayState.SCORE -= PlayState.LEVEL * 50;

                        destroy();
                        PlayState.reloadLevel();
                    }
                }
            }
        }
    }

    public void accept(Player player)
    {
        this.player = player;
        accepted = growing = true;
    }
}
