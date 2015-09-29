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
    public static int BUTTON_FREEMODE;

    public static int BUTTON_FREEMODE_PHEIGHT;
    public static int BUTTON_FREEMODE_NHEIGHT;

    public static int AXE_LS_X;
    public static int AXE_LS_Y;

    public static int AXE_RS_X;
    public static int AXE_RS_Y;

    public static void map()
    {
        if (Controller.getConnectedControllers().length == 0)
            return;

        Controller controller = Controller.getConnectedControllers()[0];
        switch (controller.getType())
        {
            case PS3:
                BUTTON_DPAD_UP = Controller.PS3_DPAD_UP;
                BUTTON_DPAD_DOWN = Controller.PS3_DPAD_DOWN;
                BUTTON_DPAD_LEFT = Controller.PS3_DPAD_LEFT;
                BUTTON_DPAD_RIGHT = Controller.PS3_DPAD_RIGHT;

                BUTTON_JUMP = Controller.PS3_BUTTON_CROSS;
                BUTTON_START = Controller.PS3_BUTTON_START;
                BUTTON_FREEMODE = Controller.PS3_BUTTON_TRIANGLE;

                BUTTON_FREEMODE_NHEIGHT = Controller.PS3_BUTTON_L1;
                BUTTON_FREEMODE_PHEIGHT = Controller.PS3_BUTTON_R1;

                AXE_LS_X = Controller.PS3_AXE_LS_X;
                AXE_LS_Y = Controller.PS3_AXE_LS_Y;

                AXE_RS_X = Controller.PS3_AXE_RS_X;
                AXE_RS_Y = Controller.PS3_AXE_RS_Y;
                break;

            case PS4:
                System.err.println("You are using a non-recognized controller, so the buttons may not work as expected.");

            case GENERIC:
                BUTTON_DPAD_UP = Controller.GENERIC_DPAD_UP;
                BUTTON_DPAD_DOWN = Controller.GENERIC_DPAD_DOWN;
                BUTTON_DPAD_LEFT = Controller.GENERIC_DPAD_LEFT;
                BUTTON_DPAD_RIGHT = Controller.GENERIC_DPAD_RIGHT;

                BUTTON_JUMP = Controller.GENERIC_BUTTON_3;
                BUTTON_START = Controller.GENERIC_BUTTON_START;
                BUTTON_FREEMODE = Controller.GENERIC_BUTTON_1;

                BUTTON_FREEMODE_NHEIGHT = Controller.GENERIC_BUTTON_5;
                BUTTON_FREEMODE_PHEIGHT = Controller.GENERIC_BUTTON_6;

                AXE_LS_X = Controller.GENERIC_AXE_LEFT_X;
                AXE_LS_Y = Controller.GENERIC_AXE_LEFT_Y;

                AXE_RS_X = Controller.GENERIC_AXE_RIGHT_X;
                AXE_RS_Y = Controller.GENERIC_AXE_RIGHT_Y;
                break;

            case XBOX:
                BUTTON_DPAD_UP = Controller.XBOX_DPAD_UP;
                BUTTON_DPAD_DOWN = Controller.XBOX_DPAD_DOWN;
                BUTTON_DPAD_LEFT = Controller.XBOX_DPAD_LEFT;
                BUTTON_DPAD_RIGHT = Controller.XBOX_DPAD_RIGHT;

                BUTTON_JUMP = Controller.XBOX_BUTTON_A;
                BUTTON_START = Controller.XBOX_BUTTON_START;
                BUTTON_FREEMODE = Controller.XBOX_BUTTON_Y;

                BUTTON_FREEMODE_NHEIGHT = Controller.XBOX_BUTTON_LB;
                BUTTON_FREEMODE_PHEIGHT = Controller.XBOX_BUTTON_RB;

                AXE_LS_X = Controller.XBOX_LEFT_STICKER_X;
                AXE_LS_Y = Controller.XBOX_LEFT_STICKER_Y;

                AXE_RS_X = Controller.XBOX_RIGHT_STICKER_X;
                AXE_RS_Y = Controller.XBOX_RIGHT_STICKER_Y;
                break;
        }
    }
}
