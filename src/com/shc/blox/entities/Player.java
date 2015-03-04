package com.shc.blox.entities;

import com.shc.blox.Blox;
import com.shc.blox.ControllerMapping;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;
import com.shc.silenceengine.math.geom3d.Cuboid;
import com.shc.silenceengine.scene.entity.Entity3D;
import com.shc.silenceengine.scene.entity.ModelEntity;

/**
 * @author Sri Harsha Chilakapati
 */
public class Player extends ModelEntity
{
    private boolean canJump;
    private boolean inFall;

    private float jumpTo;

    public Player(Vector3 position)
    {
        super(Blox.PLAYER, new Cuboid(position, 1, 1, 1));
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

        Vector3 temp = Vector3.REUSABLE_STACK.pop();

        // Simple movement on the maze
        if (Keyboard.isPressed('W') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_UP, 0))
            getVelocity().addSelf(temp.set(PlayState.camera.getForward()).normalizeSelf().scaleSelf(delta * 3.75f));

        if (Keyboard.isPressed('S') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_DOWN, 0))
            getVelocity().addSelf(temp.set(PlayState.camera.getForward()).normalizeSelf().scaleSelf(delta * -3.75f));

        if (Keyboard.isPressed('A') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_LEFT, 0))
            getVelocity().addSelf(temp.set(PlayState.camera.getRight()).normalizeSelf().scaleSelf(delta * -3.75f));

        if (Keyboard.isPressed('D') || Controller.isPressed(ControllerMapping.BUTTON_DPAD_RIGHT, 0))
            getVelocity().addSelf(temp.set(PlayState.camera.getRight()).normalizeSelf().scaleSelf(delta * 3.75f));

        // Controller axes
        getVelocity().addSelf(temp.set(PlayState.camera.getForward()).normalizeSelf()
                                  .scaleSelf(Controller.getAxe(ControllerMapping.AXE_LS_Y, 0) * delta * -3.75f));
        getVelocity().addSelf(temp.set(PlayState.camera.getRight()).normalizeSelf()
                                  .scaleSelf(Controller.getAxe(ControllerMapping.AXE_LS_X, 0) * delta * 3.75f));

        Vector3.REUSABLE_STACK.push(temp);

        // Jumping
        if (canJump && (Keyboard.isClicked(Keyboard.KEY_SPACE) || Controller.isClicked(ControllerMapping.BUTTON_JUMP, 0)))
        {
            canJump = false;
            inFall = false;
            jumpTo = 3.5f;
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
            if (other.getY() > getY())
            {
                jumpTo = 0;
                inFall = true;
                return;
            }

            setY(other.getY() + other.getHeight()/2 + getHeight()/2);

            canJump = true;
            jumpTo = 0;
            inFall = false;
        }
    }
}
