package com.shc.blox.entities;

import com.shc.blox.Resources;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.utils.GameTimer;
import com.shc.silenceengine.utils.TimeUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Spikes extends Entity3D
{
    private float targetY;
    
    public Spikes(Vector3 position)
    {
        this(position, true);
    }
    
    public Spikes(Vector3 position, boolean moveDownAndUp)
    {
        super(Resources.Models.SPIKES, new Cuboid(position, 0.8f, 0.8f, 0.8f));
        setPosition(position);
        
        targetY = position.y;
        
        if (moveDownAndUp)
        {
            GameTimer moveTimer = new GameTimer(3, TimeUtils.Unit.SECONDS);
            moveTimer.setCallback(() ->
            {
                // Don't execute if destroyed
                if (isDestroyed())
                    return;
                
                // Revert the target Y so that it moves down and up
                targetY = (targetY > 0) ? -0.1f : 0.8f;
                
                // Start the timer again
                moveTimer.start();
            });
            moveTimer.start();
        }
    }

    @Override
    public void update(float delta)
    {
        // Always move to the same position except with the change in Y
        moveTo(getX(), targetY, getZ(), 2 * delta);
    }
}
