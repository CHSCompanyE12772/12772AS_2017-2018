package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Abstract hardware class.
 * TODO: Will this actually simplify code? IDK, maybe just interfaces would be better. Or maybe keep structure as is.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

abstract class Hardware_12772 {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    DcMotor[] driveMotors;
    double[] drivePowers;

    // DRIVE SPEED
    double driveSpeedMin = 0.25;
    double driveSpeedMed = 0.5;
    double driveSpeedMax = 1.0;
    double driveSpeedStick = driveSpeedMed;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    Hardware_12772(){
    }

    //Main function called for initialization stage
    abstract void init(HardwareMap ahwMap, boolean useEncoders);

    //Main function usually called repeatedly after 'Start'
    void update(){
        // Send calculated power to DRIVE MOTORS
        for (int i = 0; i < driveMotors.length; i++)
            driveMotors[i].setPower(drivePowers[i]);
    }

    //used in Autonomous to set speed but retain direction.
    void setDriveSpeed(double speed){
        for (int i = 0; i < drivePowers.length; i++) {
            if (drivePowers[i] != 0.0)//avoids divide by zero
                drivePowers[i] *= speed / Math.abs(drivePowers[i]);//speed times sign of drivepower
            else
                drivePowers[i] = speed;//if zero, set to zero.
        }
    }

    void setDriveSpeedWithButtons(boolean increase, boolean decrease){
        //Maybe we should do this with an array? Idk, I don't think it's necessary.
        if (increase) {
            if (driveSpeedStick == driveSpeedMin) driveSpeedStick = driveSpeedMed;
            else if (driveSpeedStick == driveSpeedMed) driveSpeedStick = driveSpeedMax;
            else if (driveSpeedStick == driveSpeedMax) ;
            else driveSpeedStick = driveSpeedMed;
        }
        if (decrease) {
            if (driveSpeedStick == driveSpeedMin) ;
            else if (driveSpeedStick == driveSpeedMed) driveSpeedStick = driveSpeedMin;
            else if (driveSpeedStick == driveSpeedMax) driveSpeedStick = driveSpeedMed;
            else driveSpeedStick = driveSpeedMed;
        }
    }
}