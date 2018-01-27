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

class Hardware4x4 {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftDrive = null;
    DcMotor rightDrive = null;
    DcMotor frontDrive = null;
    DcMotor backDrive = null;


    //Motor power variables.
    double leftDrivePower;
    double rightDrivePower;
    double frontDrivePower;
    double backDrivePower;

    // DRIVE SPEED
    double driveSpeedMin = 0.25;
    double driveSpeedMed = 0.5;
    double driveSpeedMax = 1.0;
    double driveSpeedStick = driveSpeedMed;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    Hardware4x4(){
    }
    //Any purpose for this?
    //It's a default constructor. The code will probably work without it, but we may as well leave
    //it here just in case. Called when a zero-parameter Hardware12772 instance is created.

    //Main function called for initialization stage
    void init(HardwareMap ahwMap, boolean isAuto) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        /*
           Initialize the hardware variables. Note that the strings used here as parameters
           to 'get' must correspond to the names assigned during the robot configuration
           step (using the FTC Robot Controller app on the phone).
        */
        leftDrive = hwMap.get(DcMotor.class, "leftDrive");   //LEFT DRIVE WHEEL MOTOR
        rightDrive = hwMap.get(DcMotor.class, "rightDrive");  //RIGHT DRIVE WHEEL MOTOR
        frontDrive = hwMap.get(DcMotor.class, "frontDrive");
        backDrive = hwMap.get(DcMotor.class, "backDrive");

        // Since motors face opposite on each side, one drive motor needs to be reversed.
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        frontDrive.setDirection(DcMotor.Direction.FORWARD);
        backDrive.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power, juuuust in case
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        frontDrive.setPower(0);
        backDrive.setPower(0);

        /*
        RELEASE THE SHAKIN'!! Running using encoders causes motors to shake a bit, so best to
        avoid when possible.
        */
        if (isAuto) {
            leftDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            rightDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            frontDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            backDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);

            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else {
            leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            frontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            backDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    //Main function usually called repeatedly after 'Start'
    void update(){
        // Send calculated power to DRIVE MOTORS
        leftDrive.setPower(leftDrivePower);
        rightDrive.setPower(rightDrivePower);
        frontDrive.setPower(frontDrivePower);
        backDrive.setPower(backDrivePower);
    }

    //used in Autonomous to set speed but retain direction.
    void setDriveSpeed(double speed){
        //Left
        if (leftDrivePower != 0.0) //avoids divide by zero
            leftDrivePower *= speed/Math.abs(leftDrivePower); //speed times sign of drivepower
        else
            leftDrivePower = speed; //if zero, set to zero.
        //Right
        if (rightDrivePower != 0.0)
            rightDrivePower *= speed/Math.abs(rightDrivePower);
        else
            rightDrivePower = speed;
        //Front
        if (frontDrivePower != 0.0) //avoids divide by zero
            frontDrivePower *= speed/Math.abs(frontDrivePower); //speed times sign of drivepower
        else
            frontDrivePower = speed; //if zero, set to zero.
        //Back
        if (backDrivePower != 0.0)
            backDrivePower *= speed/Math.abs(backDrivePower);
        else
            backDrivePower = speed;
    }

    //set drivePower given single-joystick input
    void povDrive(double x, double y, double cw, double ccw, double speed){
        if (cw+ccw > 0)
        {
            leftDrivePower = Range.scale(cw - ccw, -1.0, 1.0, -speed, speed);
            rightDrivePower = -leftDrivePower;
            frontDrivePower = leftDrivePower;
            backDrivePower = -leftDrivePower;
        }
        else
        {
            leftDrivePower = Range.scale(y, -1.0, 1.0, -speed, speed);
            rightDrivePower = leftDrivePower;
            frontDrivePower = Range.scale(x, -1.0, 1.0, -speed, speed);
            backDrivePower = frontDrivePower;
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