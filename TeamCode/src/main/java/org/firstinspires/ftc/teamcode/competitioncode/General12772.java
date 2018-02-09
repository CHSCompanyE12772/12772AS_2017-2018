package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Class used to store useful but non-hardware specific methods and fields.
 */

import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.Arrays;

class General12772 {

    boolean[][] debouncePressedArray = new boolean[3][15]; //3 gamepads, 15 debounce buttons each.
    //See 'legends for PressedArrays.txt' for which index means what.

    String ourVuforiaLicenseKey = "AWQk7mb/////AAAAGZzcT2AtsU7fnFlKo1X5AwwP5Bwu/DPZnIJ6ObPBUoJBAbsK6ZofzC7u7b/ZzaqwD4GdQcla6Cmxqw+2a3u/X2kjfNh/jYnLnHX+vw8GEhgLmgUFPmG6ehcupHxQO+IImFWFdBXYfUIaIKcO0OxnZlg3A8OWthBsSVD3BpuIhkuYaY/pOKEZUalyf0NQepGxMa/n5iL4SYDVNQjmaKwj0lZZU2SNhr12qQWIBg3fF9b3HC33/OFGlQhjFrxYCAXzAV3LnOjptc0D0Y5g9CtQABxB3aoI7ZRkCmHpXpYtcKmq1MGFmzxKNjIL90bJcRJnP7IWyxC2hFzpiLvojC2MbJjDVtVW7jbStZhArGewsAqd";

    /* local OpMode members. */
    private ElapsedTime period = new ElapsedTime();

    /* Constructor */
    General12772() {
        // Opened up Android Studio and took a look at AutoRWDDistance and computer started throwing tantrums.
        // Fixed those tantrums by removing the parameters for this constructor.
        //We don't need a new Hardware_RWD_RearWheelDrive for non-OP mode classes.
    }

    void init() {
        for (int i = debouncePressedArray.length - 1; i >= 0; i--) //initializes all gamepad debouncers
            Arrays.fill(debouncePressedArray[i], false);
    }

    boolean debounce(boolean input, int gamepadNumber, int buttonIndex) {
        if (input != debouncePressedArray[gamepadNumber][buttonIndex]) {
            debouncePressedArray[gamepadNumber][buttonIndex] = input;
            return input;
        } else
            return false;
    }

    double[] rotateCoords(double xin, double yin, double deltaTheta) {
        double theta = Math.atan2(-yin, xin);
        theta += deltaTheta;
        double hyp = Math.hypot(xin, yin);
        double[] newCoords = {
                hyp * Math.cos(theta),
                hyp * Math.sin(theta),};
        return newCoords;
    }

    double[] rotateCoords(double xin, double yin) {
        return rotateCoords(xin,yin, 3*Math.PI/4);
    }
    double[] concat(double[] a, double[] b){ //New method, haven't tested. Connocates double arrays.
        double out[] = new double[a.length + b.length];
        for (int i = 0; i<a.length; i++)
            out[i] = a[i];
        for (int i = 0; i<b.length; i++)
            out[i+a.length] = b[i];
        return out;
    }
}