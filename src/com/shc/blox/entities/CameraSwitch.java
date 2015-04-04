package com.shc.blox.entities;

import com.shc.blox.Blox;
import com.shc.blox.Direction;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.scene.entity.ModelEntity;

/**
 * @author Sri Harsha Chilakapati
 */
public class CameraSwitch extends ModelEntity
{
    private Direction direction;

    public CameraSwitch(Vector3 position, Direction direction)
    {
        super(Blox.CONE, new Cuboid(position, 0.5f, 0.9f, 0.5f));
        setPosition(position);

        this.direction = direction;

        switch (direction)
        {
            case NORTH: rotate(-90, 0, 0); break;
            case SOUTH: rotate(90, 0, 0); break;
            case EAST: rotate(0, 0, -90); break;
            case WEST: rotate(0, 0, 90); break;
        }
    }

    @Override
    public void collision(Entity3D other)
    {
        if (other instanceof Player)
            PlayState.cameraDirection = direction;
    }
}
