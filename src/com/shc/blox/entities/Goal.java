package com.shc.blox.entities;

import com.shc.blox.Resources;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Sphere;
import com.shc.silenceengine.scene.entity.Entity3D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Goal extends Entity3D
{
    private boolean growing  = false;
    private boolean accepted = false;

    private float scale = 1;

    private Player player;

    public Goal(Vector3 position)
    {
        super(Resources.Models.SPHERE, new Sphere(position, 0.5f));
        setPosition(position);
    }

    @Override
    public void update(float delta)
    {
        rotate(90 * delta, 90 * delta, 90 * delta);

        if (accepted)
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
                    PlayState.SCORE += PlayState.LEVEL * 100;

                    destroy();
                    PlayState.nextLevel();
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
