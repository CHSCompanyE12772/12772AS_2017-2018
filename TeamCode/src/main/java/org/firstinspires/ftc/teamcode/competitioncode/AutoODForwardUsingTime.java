package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Test autonomous mode, last resort.
 * Unless a miracle happens, we will use this code. Assuming this OP Mode works.
 * Those who fail to learn from history are doomed to repeat it...
 */

@Autonomous(name = "AutoOD forward using TIME", group = "OD")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODForwardUsingTime extends LinearOpMode {

    Hardware_OD_OmniDirection r = new Hardware_OD_OmniDirection(); //Use the shared hardware and function code.
    General12772 g = new General12772(); //Use the shared general robot code.

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