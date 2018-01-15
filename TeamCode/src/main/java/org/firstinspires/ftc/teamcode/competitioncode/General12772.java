package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Main general robot class.
 * Used for....?
 */

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.Arrays;

class General12772 {

    boolean[][] debouncePressedArray = new boolean[3][15]; //3 gamepads, 15 debounce buttons each.
    //See 'legends for PressedArrays.txt' for which index means what.

    String ourVuforiaLicenseKey = "AWQk7mb/////AAAAGZzcT2AtsU7fnFlKo1X5AwwP5Bwu/DPZnIJ6ObPBUoJBAbsK6ZofzC7u7b/ZzaqwD4GdQcla6Cmxqw+2a3u/X2kjfNh/jYnLnHX+vw8GEhgLmgUFPmG6ehcupHxQO+IImFWFdBXYfUIaIKcO0OxnZlg3A8OWthBsSVD3BpuIhkuYaY/pOKEZUalyf0NQepGxMa/n5iL4SYDVNQjmaKwj0lZZU2SNhr12qQWIBg3fF9b3HC33/OFGlQhjFrxYCAXzAV3LnOjptc0D0Y5g9CtQABxB3aoI7ZRkCmHpXpYtcKmq1MGFmzxKNjIL90bJcRJnP7IWyxC2hFzpiLvojC2MbJjDVtVW7jbStZhArGewsAqd";

    /* local OpMode members. */
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    General12772(/*Hardware12772 hardware*/){
        //TODO: Get this constructor to pass reference of hardware instance to rest of the general
        //TODO: instance. Whenever I call 'r' outside of this constructor, I get an error.
        //Hardware12772 r = hardware;
    }
    // Opened up Android Studio and took a look at SWForward4Feet and computer started throwing tantrums, commented out lines above as a temp fix... - CB
    void init() {
        for(int i = debouncePressedArray.length - 1; i>=0; i--) //initializes all gamepad debouncers
            Arrays.fill(debouncePressedArray[i], false);
    }

    boolean debounce(boolean input, int gamepadNumber, int buttonIndex){
        if (input != debouncePressedArray[gamepadNumber][buttonIndex]){
            debouncePressedArray[gamepadNumber][buttonIndex] = input;
            return input;
        }
        else
            return false;
    }
}