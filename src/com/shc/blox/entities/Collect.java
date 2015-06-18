package com.shc.blox.entities;

import com.shc.blox.Resources;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Collect extends Entity3D
{
    public Collect(Vector3 position)
    {
        super(Resources.Models.COLLECT, new Cuboid(position, 0.2f, 0.2f, 0.2f));
        setPosition(position);
    }

    @Override
    public void update(float delta)
    {
        rotate(90 * delta, 90 * delta, 0);
    }
}
