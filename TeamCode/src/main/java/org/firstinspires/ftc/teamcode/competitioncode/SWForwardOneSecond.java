package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Test autonomous mode, last resort.
 * Unless a miracle happens, we will use this code. Assuming this OP Mode works.
 */

@Autonomous(name = "Move 1 second and pray for 2", group = "TeamCode")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class SWForwardOneSecond extends LinearOpMode {

    Hardware12772 r = new Hardware12772(); //Use the shared hardware and function code.

    //Distance Variables
    private int timeToMove = 1300; //Given in milliseconds. Change this to change distance robot moves.

    @Override
    public void runOpMode() {
        r.init(hardwareMap, false);  //should have named 'isAuto' better, this needs to be false for this OP mode.
        r.mainArmPower = 0;

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        r.runtime.reset();
        r.setDriveSpeed(-r.driveSpeedMed); //FIXME: For some reason, this is reversed. IDK why.
        r.update();
        sleep(timeToMove);
        r.setDriveSpeed(0.0);
    }
}