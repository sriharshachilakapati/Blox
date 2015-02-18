package com.shc.blox.entities;

import com.shc.blox.Blox;
import com.shc.blox.states.PlayState;
import com.shc.silenceengine.core.Game;
import com.shc.silenceengine.entity.Entity3D;
import com.shc.silenceengine.entity.ModelEntity;
import com.shc.silenceengine.geom3d.Cuboid;
import com.shc.silenceengine.input.Controller;
import com.shc.silenceengine.input.Keyboard;
import com.shc.silenceengine.math.Vector3;

/**
 * @author Sri Harsha Chilakapati
 */
public class Player extends ModelEntity
{
    private boolean canJump;
    private boolean inFall;

    private float jumpTo;

    // Controller support!
    private Controller controller;

    // Mapping for controller depending on type
    private int dPadUp;
    private int dPadDown;
    private int dPadLeft;
    private int dPadRight;
    private int jump;
    private int reset;

    private int axisX;
    private int axisY;

    public Player(Vector3 position)
    {
        super(Blox.PLAYER, new Cuboid(position, 1, 1, 1));
        setPosition(position);

        canJump = false;
        inFall = true;

        setupController();
    }

    private void setupController()
    {
        if (Controller.getConnectedControllers().length == 0)
            return;

        controller = Controller.getConnectedControllers()[0];

        switch (controller.getType())
        {
            case GENERIC:
                dPadUp = Controller.GENERIC_DPAD_UP;
                dPadDown = Controller.GENERIC_DPAD_DOWN;
                dPadLeft = Controller.GENERIC_DPAD_LEFT;
                dPadRight = Controller.GENERIC_DPAD_RIGHT;

                jump = Controller.GENERIC_BUTTON_3;
                reset = Controller.GENERIC_BUTTON_10;

                axisX = Controller.GENERIC_AXE_LEFT_X;
                axisY = Controller.GENERIC_AXE_LEFT_Y;
                break;

            case XBOX:
                dPadUp = Controller.XBOX_DPAD_UP;
                dPadDown = Controller.XBOX_DPAD_DOWN;
                dPadLeft = Controller.XBOX_DPAD_LEFT;
                dPadRight = Controller.XBOX_DPAD_RIGHT;

                jump = Controller.XBOX_BUTTON_X;
                reset = Controller.XBOX_BUTTON_START;

                axisX = Controller.XBOX_LEFT_STICKER_X;
                axisY = Controller.XBOX_LEFT_STICKER_Y;
                break;

            default:
                controller = null;
        }
    }

    private boolean getControllerButton(int button, boolean repeat)
    {
        if (repeat)
            return controller != null && controller.isPressed(button);
        else
            return controller != null && controller.isClicked(button);
    }

    private float getAxe(int axe)
    {
        return controller == null ? 0 : controller.getAxe(axe);
    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isClicked('R') || getControllerButton(reset, false))
            Game.setGameState(new PlayState());

        getVelocity().set(0, 0, 0);

        if (Keyboard.isPressed('W') || getControllerButton(dPadUp, true))
            getVelocity().addSelf(0, 0, -delta * 4);

        if (Keyboard.isPressed('S') || getControllerButton(dPadDown, true))
            getVelocity().addSelf(0, 0, delta * 4);

        if (Keyboard.isPressed('A') || getControllerButton(dPadLeft, true))
            getVelocity().addSelf(-delta * 4, 0, 0);

        if (Keyboard.isPressed('D') || getControllerButton(dPadRight, true))
            getVelocity().addSelf(delta * 4, 0, 0);

        // Controller axes
        getVelocity().addSelf(getAxe(axisX) * 4 * delta, 0, getAxe(axisY) * 4 * delta);

        if (canJump && (Keyboard.isClicked(Keyboard.KEY_SPACE) || getControllerButton(jump, false)))
        {
            canJump = false;
            inFall = false;
            jumpTo = 3.5f;
        }

        if (!canJump && !inFall)
        {
            getVelocity().addSelf(0, delta * 8, 0);

            if (getPosition().y > jumpTo)
                inFall = true;
        }

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
