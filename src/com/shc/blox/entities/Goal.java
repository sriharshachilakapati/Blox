package com.shc.blox.entities;

import com.shc.blox.Resources;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Sphere;
import com.shc.silenceengine.scene.entity.Entity3D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Goal extends Entity3D
{
    public Goal(Vector3 position)
    {
        super(Resources.Models.SPHERE, new Sphere(position, 0.5f));
        setPosition(position);
    }

    @Override
    public void update(float delta)
    {
        rotate(90 * delta, 90 * delta, 90 * delta);
    }
}
