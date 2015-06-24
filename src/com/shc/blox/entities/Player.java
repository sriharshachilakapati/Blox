package com.shc.blox.entities;

import com.shc.blox.ControllerMapping;
import com.shc.blox.Resources;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;

/**
 * @author Sri Harsha Chilakapati
 */
public class Player extends Entity3D
{
    private boolean canJump;
    private boolean inFall;
    private boolean accepted = false;

    private float jumpTo;

    public Player(Vector3 position)
    {
        super(Resources.Models.PLAYER, new Cuboid(position, 1, 1, 1));
        setPosition(position);

        canJump = false;
        inFall = true;
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked('R') || getY() < -10 || Controller.isClicked(ControllerMapping.BUTTON_START, 0))
            Game.setGameState(new PlayState());

        getVelocity().set(0, 0, 0);

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
        if (Keyboard.isPressed('W') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_UP, 0))
            getVelocity().addSelf(temp.set(forward)).normalizeSelf().scaleSelf(delta * 4);

        if (Keyboard.isPressed('S') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_DOWN, 0))
            getVelocity().addSelf(temp.set(forward).normalizeSelf().scaleSelf(delta * -4));

        if (Keyboard.isPressed('A') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_LEFT, 0))
            getVelocity().addSelf(temp.set(right).normalizeSelf().scaleSelf(delta * -4));

        if (Keyboard.isPressed('D') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_RIGHT, 0))
            getVelocity().addSelf(temp.set(right).normalizeSelf().scaleSelf(delta * 4));

        // Controller axes
        getVelocity().addSelf(temp.set(forward).normalizeSelf()
                                  .scaleSelf(Controller.getAxe(ControllerMapping.AXE_LS_Y, 0) * delta * -4));
        getVelocity().addSelf(temp.set(right).normalizeSelf()
                                  .scaleSelf(Controller.getAxe(ControllerMapping.AXE_LS_X, 0) * delta * 4));

        Vector3.REUSABLE_STACK.push(temp);
        Vector3.REUSABLE_STACK.push(forward);
        Vector3.REUSABLE_STACK.push(right);

        // Jumping
        if (canJump && (Keyboard.isClicked(Keyboard.KEY_SPACE) || Controller.isClicked(ControllerMapping.BUTTON_JUMP, 0)))
        {
            canJump = false;
            inFall = false;
            jumpTo = getY() + 2;
        }

        if (!canJump && !inFall)
        {
            getVelocity().addSelf(0, delta * 10, 0);

            if (getPosition().y > jumpTo)
                inFall = true;
        }

        // Gravity
        getVelocity().subtractSelf(0, delta * 4, 0);

        canJump = false;
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

        if (other instanceof Collect)
        {
            other.destroy();
            PlayState.SCORE += 10;
        }
    }
}
