package com.shc.blox.entities;

import com.shc.blox.Resources;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Floor extends Entity3D
{
    public Floor(Vector3 position)
    {
        super(Resources.Models.FLOOR, new Cuboid(position, 1, 1, 1));
        setPosition(position);
    }
}
