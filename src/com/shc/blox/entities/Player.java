package com.shc.blox.entities;

import com.shc.blox.PlayerController;
import com.shc.blox.Resources;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.utils.MathUtils;

/**
 * @author Sri Harsha Chilakapati
 */
public class Player extends Entity3D
{
    private boolean canJump;
    private boolean inFall;
    private boolean accepted = false;

    private float jumpTo;
    
    private Vector3 acceleration;

    public Player(Vector3 position)
    {
        super(Resources.Models.PLAYER, new Cuboid(position, 1, 1, 1));
        setPosition(position);

        canJump = false;
        inFall = true;
        
        acceleration = new Vector3();
    }

    @Override
    public void update(float delta)
    {
        if (accepted)
        {
            acceleration.set(0);
            getVelocity().set(0);
            return;
        }
        
        if (getPosition().y < -10)
            PlayState.SCORE -= PlayState.LEVEL * 50;

        if (Keyboard.isClicked('R') || getPosition().y < -10 || Controller.isClicked(PlayerController.BUTTON_START, 0))
            PlayState.reloadLevel();

        if (accepted)
            return;

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        // The axes to use for movement
        Vector3 forward = Vector3.REUSABLE_STACK.pop();
        Vector3 right   = Vector3.REUSABLE_STACK.pop();

        // Determine the axes based on camera direction
        switch (PlayState.cameraDirection)
        {
            case NORTH:
                forward.set(0, 0, -1);
                right.set(1, 0, 0);
                break;

            case SOUTH:
                forward.set(0, 0, 1);
                right.set(-1, 0, 0);
                break;

            case EAST:
                forward.set(1, 0, 0);
                right.set(0, 0, 1);
                break;

            case WEST:
                forward.set(-1, 0, 0);
                right.set(0, 0, -1);
        }
        
        // Simple movement on the maze
        if (Keyboard.isPressed('W') || Controller.isPressed(PlayerController.BUTTON_DPAD_UP, 0))
            acceleration.addSelf(temp.set(forward).normalizeSelf().scaleSelf(2));

        if (Keyboard.isPressed('S') || Controller.isPressed(PlayerController.BUTTON_DPAD_DOWN, 0))
            acceleration.addSelf(temp.set(forward).normalizeSelf().scaleSelf(-2));

        if (Keyboard.isPressed('A') || Controller.isPressed(PlayerController.BUTTON_DPAD_LEFT, 0))
            acceleration.addSelf(temp.set(right).normalizeSelf().scaleSelf(-2));

        if (Keyboard.isPressed('D') || Controller.isPressed(PlayerController.BUTTON_DPAD_RIGHT, 0))
            acceleration.addSelf(temp.set(right).normalizeSelf().scaleSelf(2));

        // Controller axes
        acceleration.addSelf(temp.set(forward).normalizeSelf()
                .scaleSelf(Controller.getAxe(PlayerController.AXE_LS_Y, 0) * -2));
        acceleration.addSelf(temp.set(right).normalizeSelf()
                .scaleSelf(Controller.getAxe(PlayerController.AXE_LS_X, 0) * 2));

        Vector3.REUSABLE_STACK.push(temp);
        Vector3.REUSABLE_STACK.push(forward);
        Vector3.REUSABLE_STACK.push(right);

        // Jumping
        if (canJump && (Keyboard.isClicked(Keyboard.KEY_SPACE) || Controller.isClicked(PlayerController.BUTTON_JUMP, 0)))
        {
            canJump = false;
            inFall = false;
            jumpTo = getY() + 2;
        }

        if (!canJump && !inFall)
        {
            acceleration.addSelf(0, 12, 0);

            if (getPosition().y > jumpTo)
                inFall = true;
        }

        // Gravity
        acceleration.subtractSelf(0, 1, 0);
        
        // Friction
        acceleration.x = applyFriction(acceleration.x, 0.5f);
        acceleration.y = applyFriction(acceleration.y, 0.5f);
        acceleration.z = applyFriction(acceleration.z, 0.5f);
        
        // Limit acceleration
        acceleration.x = MathUtils.clamp(acceleration.x, -6, 6);
        acceleration.y = MathUtils.clamp(acceleration.y, -4, 4);
        acceleration.z = MathUtils.clamp(acceleration.z, -6, 6);
        
        getVelocity().set(acceleration).scaleSelf(delta);

        canJump = false;
    }
    
    private float applyFriction(float component, float friction)
    {
        if (component < 0 && component + friction > 0)
            friction = component - friction;
        
        if (component > 0 && component - friction < 0)
            friction = component + friction;
        
        if (component < 0)
            component += friction;
        
        if (component > 0)
            component -= friction;
        
        return component;
    }

    @Override
    public void collision(Entity3D other)
    {
        if (other instanceof Floor)
        {
            setY(other.getY() + other.getHeight() / 2 + getHeight() / 2);

            canJump = true;
            jumpTo = 0;
            inFall = false;
        }

        if (other instanceof Goal)
        {
            ((Goal) other).accept(this);
            accepted = true;
        }

        if (other instanceof ThunderBall)
        {
            ((ThunderBall) other).accept(this);
            accepted = true;
            
            acceleration.set(0);
        }

        if (other instanceof Collect)
        {
            other.destroy();
            PlayState.SCORE += 10;
        }

        if (other instanceof Spikes)
        {
            PlayState.SCORE -= PlayState.LEVEL * 50;
            PlayState.reloadLevel();
        }
    }
}
