package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Each motor will fire one after the other, testing which are connected. Prayers not included.
 * TODO: Get programmers who can handle these things I struggle with so.
 */

@Autonomous(name = "AutoOD Test All Motor Connections", group = "OD")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODTestEachMotor extends LinearOpMode {

    Hardware_OD_OmniDirection r = new Hardware_OD_OmniDirection(); //Use the shared hardware and function code.
    General12772 g = new General12772(); //Use the shared general robot code.

    //Distance Variables
    private int timeToMove = 200; //Milliseconds in each direction.

    @Override
    public void runOpMode() {
        r.init(hardwareMap, false);  //should have named 'isAuto' better, this needs to be false for this OP mode.
        r.mainArmPower = 0;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        r.runtime.reset();
        /* I am SO sorry for this code, please never do what I am about to do...*/
        /** LF, RF, RR, LR, MA*/
        r.setDriveSpeed(0.0);
        r.leftFrontDrivePower = r.driveSpeedMed;
        r.update();
        sleep(timeToMove);

        r.setDriveSpeed(0.0);
        r.rightFrontDrivePower = r.driveSpeedMed;
        r.update();
        sleep(timeToMove);

        r.setDriveSpeed(0.0);
        r.rightRearDrivePower = r.driveSpeedMed;
        r.update();
        sleep(timeToMove);

        r.setDriveSpeed(0.0);
        r.leftRearDrivePower = r.driveSpeedMed;
        r.update();
        sleep(timeToMove);

        r.setDriveSpeed(0.0);
        r.mainArmPower = r.driveSpeedMed;
        r.update();
        sleep(timeToMove);

        r.setDriveSpeed(0.0);
        r.update();
    }
}