package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.eventloop.opmode.Disabled; // Leave this line here even when not used, please

/**
 * Test autonomous mode.
 */

@Autonomous(name = "AutoRWD Move (experimental)", group = "OD")
@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoRWDDistance extends LinearOpMode {

    Hardware_RWD_RearWheelDrive r = new Hardware_RWD_RearWheelDrive(); //Use the shared hardware and function code.
    General12772 g = new General12772(); //Use the shared general robot code.

    //Distance Variables
    private double numberOfFeet = 4.0;          //Distance desired to travel
    private double driveWheelDiameter = 3.5;    //Given in inches
    private int incrementsPerRevolution = 1500;            //Motor types read encoders at different rates. This is a rough empirical value
    private int targetPosition;

    @Override
    public void runOpMode() {
        r.init(hardwareMap, true);  //Initialization with safe space for shakes.

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        r.runtime.reset();
        r.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        r.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        targetPosition = (int)( (numberOfFeet*incrementsPerRevolution)*(12.0) / (driveWheelDiameter*Math.PI) );
        r.setDriveSpeed(r.driveSpeedMin);
        r.leftDrive.setTargetPosition(targetPosition);
        r.rightDrive.setTargetPosition(targetPosition);

        r.update();
        //To prevent major seizure near target destination, this code cuts off the motors once it is close enough
        while (Math.abs(r.leftDrive.getCurrentPosition()-r.leftDrive.getTargetPosition())>50){ //checks if close enough
            sleep(200);     // time interval between checking if close enough
        }
        r.setDriveSpeed(0.0);
    }
}