package com.shc.blox.entities;

import com.shc.blox.Blox;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.ModelEntity;

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
