package com.shc.blox;

import com.shc.silenceengine.input.Controller;

/**
 * @author Sri Harsha Chilakapati
 */
public final class PlayerController
{
    public static int BUTTON_DPAD_UP;
    public static int BUTTON_DPAD_DOWN;
    public static int BUTTON_DPAD_RIGHT;
    public static int BUTTON_DPAD_LEFT;

    public static int BUTTON_JUMP;
    public static int BUTTON_START;

    public static int AXE_LS_X;
    public static int AXE_LS_Y;

    public static void map()
    {
        if (Controller.getConnectedControllers().length == 0)
            return;

        Controller controller = Controller.getConnectedControllers()[0];
        switch (controller.getType())
        {
            case PS3:
            case PS4:
                System.err.println("You are using a non-recognized controller, so the buttons may not work as expected.");

            case GENERIC:
                BUTTON_DPAD_UP = Controller.GENERIC_DPAD_UP;
                BUTTON_DPAD_DOWN = Controller.GENERIC_DPAD_DOWN;
                BUTTON_DPAD_LEFT = Controller.GENERIC_DPAD_LEFT;
                BUTTON_DPAD_RIGHT = Controller.GENERIC_DPAD_RIGHT;

                BUTTON_JUMP = Controller.GENERIC_BUTTON_3;
                BUTTON_START = Controller.GENERIC_BUTTON_START;

                AXE_LS_X = Controller.GENERIC_AXE_LEFT_X;
                AXE_LS_Y = Controller.GENERIC_AXE_LEFT_Y;
                break;

            case XBOX:
                BUTTON_DPAD_UP = Controller.XBOX_DPAD_UP;
                BUTTON_DPAD_DOWN = Controller.XBOX_DPAD_DOWN;
                BUTTON_DPAD_LEFT = Controller.XBOX_DPAD_LEFT;
                BUTTON_DPAD_RIGHT = Controller.XBOX_DPAD_RIGHT;

                BUTTON_JUMP = Controller.XBOX_BUTTON_B;
                BUTTON_START = Controller.XBOX_BUTTON_START;

                AXE_LS_X = Controller.XBOX_LEFT_STICKER_X;
                AXE_LS_Y = Controller.XBOX_LEFT_STICKER_Y;
                break;
        }
    }
}
