package com.shc.blox.entities;

import com.shc.blox.Blox;
import com.shc.silenceengine.entity.ModelEntity;
import com.shc.silenceengine.geom3d.Cuboid;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Floor extends ModelEntity
{
    public Floor(Vector3 position)
    {
        super(Blox.FLOOR, new Cuboid(position, 1, 1, 1));
        setPosition(position);
    }
}
