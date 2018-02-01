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

class Hardware_OD_OmniDirection {

    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftBackDrive = null;
    DcMotor rightFrontDrive = null;
    DcMotor leftFrontDrive = null;
    DcMotor rightBackDrive = null;
    DcMotor mainArm = null;

    Servo leftTopClaw = null;
    Servo rightTopClaw = null;
    Servo leftBottomClaw = null;
    Servo rightBottomClaw = null;

    //Drive wheel power variables.
    double leftBackDrivePower;
    double rightFrontDrivePower;
    double leftFrontDrivePower;
    double rightBackDrivePower;

    // CLAW MAX AND MIN POS
    double clawPOSMin = 0.0;
    double clawPOSMax = 1.0;
    double clawsPOS = 0;

    //  CLAW OFFSET. used to adjust to real values
    double leftBottomClawOffset = 0.0; //Default ideal values, modified later
    double rightBottomClawOffset = 1.0;
    double leftTopClawOffset = 1.0;
    double rightTopClawOffset = 0.0;

    // MAIN ARM POS AND POWER
    double mainArmPower;
    double mainArmPowerMax = 0.75;
    int[] mainArmPositions = {10, 120, 260, 320};
    int mainArmPosition = 0;
    int mainArmPositionX = -1;  //Test variable, find ideal arm positions
    double mainArmHoldingPower = 0.2;
    double mainArmMaxUpPower = 0.8;
    double mainArmMaxDownPower = mainArmHoldingPower + 0.05;
    boolean mainArmHolding = false;

    // DRIVE SPEED
    double driveSpeedMin = 0.25;
    double driveSpeedMed = 0.5;
    double driveSpeedMax = 1.0;
    double driveSpeedStick = driveSpeedMed;

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    Hardware_OD_OmniDirection(){
    }

    //Main function called for initialization stage
    void init(HardwareMap ahwMap, boolean isAuto) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        leftBackDrive = hwMap.get(DcMotor.class, "leftBackDrive");   //LEFT DRIVE WHEEL MOTOR
        rightFrontDrive = hwMap.get(DcMotor.class, "rightFrontDrive");  //RIGHT DRIVE WHEEL MOTOR
        leftFrontDrive = hwMap.get(DcMotor.class, "leftFrontDrive");
        rightBackDrive = hwMap.get(DcMotor.class, "rightBackDrive");
        mainArm = hwMap.get(DcMotor.class, "mainArm");      //ARM MOTOR

        leftBottomClaw = hwMap.get(Servo.class, "leftBottomClaw");      //LEFT CLAW SERVO
        rightBottomClaw = hwMap.get(Servo.class, "rightBottomClaw");      //RIGHT CLAW SERVO
        leftTopClaw = hwMap.get(Servo.class, "leftTopClaw");      //UPPER LEFT CLAW SERVO
        rightTopClaw = hwMap.get(Servo.class, "rightTopClaw");      //UPPER RIGHT CLAW SERVO

        // Since motors face opposite on each side, one drive motor needs to be reversed.
        // Reverse the motor that runs backwards when connected directly to the battery
        leftBackDrive.setDirection(DcMotor.Direction.FORWARD);
        rightFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        leftFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        rightBackDrive.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power, juuuust in case
        leftBackDrive.setPower(0);
        rightFrontDrive.setPower(0);
        leftFrontDrive.setPower(0);
        rightBackDrive.setPower(0);

        /*
        RELEASE THE SHAKIN'!! Running using encoders causes motors to shake a bit, so best to
        avoid when possible.
        */
        if (isAuto) {
            leftBackDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            rightFrontDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            leftFrontDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
            rightBackDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);

            leftBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else {
            leftBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            leftFrontDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightBackDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
    }

    //Main function usually called repeatedly after 'Start'
    void update(){
        // Send calculated power to DRIVE MOTORS
        leftBackDrive.setPower(leftBackDrivePower);
        rightFrontDrive.setPower(rightFrontDrivePower);
        leftFrontDrive.setPower(leftFrontDrivePower);
        rightBackDrive.setPower(rightBackDrivePower);
    }

    //used in Autonomous to set speed but retain direction.
    void setDriveSpeed(double speed){
        //Left
        if (leftBackDrivePower != 0.0) //avoids divide by zero
            leftBackDrivePower *= speed/Math.abs(leftBackDrivePower); //speed times sign of drivepower
        else
            leftBackDrivePower = speed; //if zero, set to zero.
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
    void povDrive(double x, double y, double cw, double acw, double speed){
        if (cw+acw > 0)
        {
            leftBackDrivePower = Range.scale(cw - acw, -1.0, 1.0, -speed, speed);
            rightFrontDrivePower = -leftBackDrivePower;
            leftFrontDrivePower = leftBackDrivePower;
            rightBackDrivePower = -leftBackDrivePower;
        }
        else
        {
            leftBackDrivePower = Range.scale(y, -1.0, 1.0, -speed, speed);
            rightFrontDrivePower = leftBackDrivePower;
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