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

    private float scale = 1;

    public ThunderBall(Vector3 position, Direction direction)
    {
        super(Resources.Models.SPHERE2, new Sphere(position, 0.5f));
        velocityDir = direction;

        switch (velocityDir)
        {
            case NORTH: setPosition(position.addSelf(+0, +0, +3)); break;
            case EAST:  setPosition(position.addSelf(-3, +0, +0)); break;
            case SOUTH: setPosition(position.addSelf(+0, +0, -3)); break;
            case WEST:  setPosition(position.addSelf(+3, +0, +0)); break;
        }

        GameTimer timer = new GameTimer(1.5, TimeUtils.Unit.SECONDS);
        timer.setCallback(() ->
        {
            if (isDestroyed() || accepted)
                return;

            switch (velocityDir)
            {
                case NORTH: velocityDir = Direction.SOUTH; break;
                case EAST:  velocityDir = Direction.WEST;  break;
                case SOUTH: velocityDir = Direction.NORTH; break;
                case WEST:  velocityDir = Direction.EAST;  break;
            }

            timer.start();
        });
        timer.start();
    }

    @Override
    public void update(float delta)
    {
        float random = MathUtils.random_range(0, 360);
        setRotation(random, random, random);

        getVelocity().set(0);

        if (!accepted)
            switch (velocityDir)
            {
                case NORTH: getVelocity().set(+0, +0, -4).scaleSelf(delta); break;
                case EAST:  getVelocity().set(+4, +0, +0).scaleSelf(delta); break;
                case SOUTH: getVelocity().set(+0, +0, +4).scaleSelf(delta); break;
                case WEST:  getVelocity().set(-4, +0, +0).scaleSelf(delta); break;
            }
        else
        {
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

    public void accept(Player player)
    {
        this.player = player;
        accepted = growing = true;
    }
}
