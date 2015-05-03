package com.shc.blox.entities;

import com.shc.blox.Blox;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Sphere;
import com.shc.silenceengine.scene.entity.ModelEntity;

/**
 * @author Sri Harsha Chilakapati
 */
public class Goal extends ModelEntity
{
    public Goal(Vector3 position)
    {
        super(Blox.SPHERE, new Sphere(position, 0.5f));
        setPosition(position);
    }

    @Override
    public void update(float delta)
    {
        rotate(90 * delta, 90 * delta, 90 * delta);
    }
}
