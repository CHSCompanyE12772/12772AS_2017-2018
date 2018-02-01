package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Hardware class for 4-wheeled robot.
 * Robot will have drive wheel on each side, and can move in x and z directions as well as rotate.
 * It hasn't been built yet, but eventually will.
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

class HardwareOmniDirection {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftRearDrive = null;
    DcMotor rightFrontDrive = null;
    DcMotor leftFrontDrive = null;
    DcMotor rightBackDrive = null;


    //Motor power variables.
    double leftRearDrivePower;
    double rightFrontDrivePower;
    double leftFrontDrivePower;
    double rightBackDrivePower;

    // DRIVE SPEED
    double driveSpeedMin = 0.25;
    double driveSpeedMed = 0.5;
    double driveSpeedMax = 1.0;
    double driveSpeedStick = driveSpeedMed;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    HardwareOmniDirection(){
    }
    //Any purpose for this?
    //It's a default constructor. The code will probably work without it, but we may as well leave
    //it here just in case. Called when a zero-parameter HardwareRearWheelDrive instance is created.

    //Main function called for initialization stage
    void init(HardwareMap ahwMap, boolean isAuto) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        /*
           Initialize the hardware variables. Note that the strings used here as parameters
           to 'get' must correspond to the names assigned during the robot configuration
           step (using the FTC Robot Controller app on the phone).
        */
        leftRearDrive = hwMap.get(DcMotor.class, "leftRearDrive");   //LEFT DRIVE WHEEL MOTOR
        rightFrontDrive = hwMap.get(DcMotor.class, "rightFrontDrive");  //RIGHT DRIVE WHEEL MOTOR
        leftFrontDrive = hwMap.get(DcMotor.class, "leftFrontDrive");
        rightBackDrive = hwMap.get(DcMotor.class, "rightBackDrive");

        // Since motors face opposite on each side, one drive motor needs to be reversed.
        // Reverse the motor that runs backwards when connected directly to the battery
        leftRearDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power, juuuust in case
        leftRearDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);

        /*
        RELEASE THE SHAKIN'!! Running using encoders causes motors to shake a bit, so best to
        avoid when possible.
        */
        if (isAuto) {
            leftRearDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            rightFrontDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            leftFrontDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            rightBackDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);

            leftRearDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else {
            leftRearDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    //Main function usually called repeatedly after 'Start'
    void update(){
        // Send calculated power to DRIVE MOTORS
        leftRearDrive.setPower(leftRearDrivePower);
        rightFrontDrive.setPower(rightFrontDrivePower);
        leftFrontDrive.setPower(leftFrontDrivePower);
        rightBackDrive.setPower(rightBackDrivePower);
    }

    //used in Autonomous to set speed but retain direction.
    void setDriveSpeed(double speed){
        //Left
        if (leftRearDrivePower != 0.0) //avoids divide by zero
            leftRearDrivePower *= speed/Math.abs(leftRearDrivePower); //speed times sign of drivepower
        else
            leftRearDrivePower = speed; //if zero, set to zero.
        //Right
        if (rightFrontDrivePower != 0.0)
            rightFrontDrivePower *= speed/Math.abs(rightFrontDrivePower);
        else
            rightFrontDrivePower = speed;
        //Front
        if (leftFrontDrivePower != 0.0) //avoids divide by zero
            leftFrontDrivePower *= speed/Math.abs(leftFrontDrivePower); //speed times sign of drivepower
        else
            leftFrontDrivePower = speed; //if zero, set to zero.
        //Back
        if (rightBackDrivePower != 0.0)
            rightBackDrivePower *= speed/Math.abs(rightBackDrivePower);
        else
            rightBackDrivePower = speed;
    }

    //set drivePower given single-joystick input
    void povDrive(double x, double y, double cw, double ccw, double speed){
        if (cw+ccw > 0)
        {
            leftRearDrivePower = Range.scale(cw - ccw, -1.0, 1.0, -speed, speed);
            rightFrontDrivePower = -leftRearDrivePower;
            leftFrontDrivePower = leftRearDrivePower;
            rightBackDrivePower = -leftRearDrivePower;
        }
        else
        {
            leftRearDrivePower = Range.scale(y, -1.0, 1.0, -speed, speed);
            rightFrontDrivePower = leftRearDrivePower;
            leftFrontDrivePower = Range.scale(x, -1.0, 1.0, -speed, speed);
            rightBackDrivePower = leftFrontDrivePower;
        }
    }

    void setDriveSpeedWithButtons(boolean increase, boolean decrease){
        //Maybe we should do this with an array? Idk, I don't think it's necessary.
        if (increase) {
            if (driveSpeedStick == driveSpeedMin) driveSpeedStick = driveSpeedMed;
            else if (driveSpeedStick == driveSpeedMed) driveSpeedStick = driveSpeedMax;
            else if (driveSpeedStick == driveSpeedMax) ;//TODO: add sound cue for this condition.
            else driveSpeedStick = driveSpeedMed;
        }
        if (decrease) {
            if (driveSpeedStick == driveSpeedMin) ;//TODO: add sound cue for this condition.
            else if (driveSpeedStick == driveSpeedMed) driveSpeedStick = driveSpeedMin;
            else if (driveSpeedStick == driveSpeedMax) driveSpeedStick = driveSpeedMed;
            else driveSpeedStick = driveSpeedMed;
        }
    }
}