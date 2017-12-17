package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Created by Jose on 16 Nov.
 * Last modified 17 Dec by Carlos
 * Used to extend to program common functions
 */

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import java.util.Arrays;

class Hardware12772{
    // Declare OpMode members.
    ElapsedTime runtime = new ElapsedTime();
    DcMotor leftDrive = null;
    DcMotor rightDrive = null;
    Servo leftClaw = null;
    Servo rightClaw = null;
    DcMotor mainArm = null;
    //private ColorSensor colorDistanceSensor = null;

    // Setup a variable for each drive wheel to save power level for telemetry
    double leftDrivePower;
    double rightDrivePower;
    double mainArmPower;

    // CLAW MAX AND MIN POS
    double clawPOSMin = 0.0;
    double clawPOSMax = 1.0;
    double clawsPOS = 0;

    //  CLAW OFFSET. used to adjust to real values
    double leftClawOffset = 0.0; //Default ideal values, modified later
    double rightClawOffset = 1.0;
    //double clawsOffset;   //do we need an offset for each claw or can we just use one offset for both?

    // MAIN ARM POS AND POWER
    double mainArmPowerMax = 0.5;
    int[] mainArmPositions = {10, 120, 260, 320};
    int mainArmPosition = 0;
    int mainArmPositionX = -1;  //Test variable, find ideal arm positions
    double mainArmHoldingPower = 0.2;
    boolean mainArmHolding = false;

    // DRIVE SPEED
    double driveSpeedMin = 0.25;
    double driveSpeedMed = 0.5;
    double driveSpeedMax = 1.0;
    double driveSpeedStick = driveSpeedMed;

    boolean[] gamepad1PressedArray = {  //I'm so sorry for this, I don't know a better way, blame Cruz for leaving...
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
    };
    //See Notebook for which index means what.

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    Hardware12772(){
    }
    //Any purpose for this?

    void init(HardwareMap ahwMap, boolean isAuto) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        /*
           Initialize the hardware variables. Note that the strings used here as parameters
           to 'get' must correspond to the names assigned during the robot configuration
           step (using the FTC Robot Controller app on the phone).
        */
        leftDrive = hwMap.get(DcMotor.class, "leftDrive");   //LEFT FRONT WHEEL MOTOR
        rightDrive = hwMap.get(DcMotor.class, "rightDrive");  //RIGHT FRONT WHEEL MOTOR
        leftClaw = hwMap.get(Servo.class, "leftClaw");      //LEFT CLAW SERVO
        rightClaw = hwMap.get(Servo.class, "rightClaw");      //RIGHT CLAW SERVO
        mainArm = hwMap.get(DcMotor.class, "mainArm");      //ARM MOTOR

        // Most robots need the motor on one side to be reversed to drive forward
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        mainArm.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        mainArm.setPower(0);

        if (isAuto) {
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else {
            leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        mainArm.setMode(DcMotor.RunMode.RESET_ENCODERS);
//        mainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        mainArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //Temp
        mainArmPower = mainArmPowerMax;
        mainArm.setTargetPosition(0);
    }

    void update(){
        // Send calculated power to MOTORS
        leftDrive.setPower(leftDrivePower);
        rightDrive.setPower(rightDrivePower);
//        mainArm.setTargetPosition(mainArmPositions[mainArmPosition]);
        if (mainArmPositionX != -1){
/*
            if (Math.abs( mainArm.getCurrentPosition()-mainArm.getTargetPosition() ) <= 100) {
                mainArm.setTargetPosition(mainArm.getCurrentPosition());
            }
            //Didn't fix problem, but might be useful to keep this code here.
*/
            mainArm.setTargetPosition(mainArmPositionX);
        }
//      if ((mainArmPosition - mainArm.getCurrentPosition() > 0) == (mainArmPower > 0)) { //XOR gate. Makes sure arm is moving in right direction.
//          mainArmPower *= -1;
//      }
        mainArm.setPower(mainArmPower);
        moveClaw(clawsPOS);
    }

    void setDriveSpeed(double speed){
        if (leftDrivePower != 0.0) //avoids Divide by zero
            leftDrivePower *= speed/Math.abs(leftDrivePower); //speed times sign of drivepower
        else
            leftDrivePower = speed;
        if (rightDrivePower != 0.0) //avoids Divide by zero
            rightDrivePower *= speed/Math.abs(rightDrivePower); //same thing for right motor
        else
            rightDrivePower = speed;
    }

/*
    void tankControls(){
    }
    //What is this for?
*/

    void povDrive(double x, double y, double speed){
        leftDrivePower = Range.clip(y - x, -speed, speed);
        rightDrivePower = Range.clip(y + x, -speed, speed);
    }

    void setDriveSpeedWithButtons(boolean in1, boolean in2, boolean in3){
        if (in1) {
            driveSpeedStick = driveSpeedMin;
        }
        if (in2) {
            driveSpeedStick = driveSpeedMed;
        }
        if (in3) {
            driveSpeedStick = driveSpeedMax;
        }
    }

    void setArmPositionDPad(boolean in1, boolean in2, boolean in3, boolean in4) {
        if (in1) {
            mainArmPosition = 0;
        }
        if (in2) {
            mainArmPosition = 1;
        }
        if (in3) {
            mainArmPosition = 2;
        }
        if (in4) {
            mainArmPosition = 3;
        }
    }

    void setArmPositionJoystick(double y, double x, boolean clk){
        double OutputMax = mainArmPowerMax;
        if (y > 0)  OutputMax *= 0;
        mainArmPower = y * OutputMax;
        if (mainArmHolding) mainArmPower += mainArmHoldingPower;
        if (clk) mainArmHolding = !mainArmHolding;
    }

/*
    void setArmPositionCareful(boolean in1, boolean in2, boolean in3, boolean in4){
        if (in1) {
            mainArmPositionX += 5;
        }
        if (in2) {
            mainArmPositionX += 100;
        }
        if (in3) {
            mainArmPositionX -= 5;
        }
        if (in4) {
            mainArmPositionX -= 100;
        }
    } //Outdated Test Method.
*/

    void setServoPositionTwoButton(boolean in1, boolean in2){
        double incr = 0.025;
        if (in1){
//            TclawsPOS += incr;
            clawsPOS += incr;
        }
        if (in2){
//            TclawsPOS -= incr;
            clawsPOS -= incr;
        }
    }

    void initClawServosPOS(double startPosition){ //Bugged, .getPosition is always returning zero, regardless of actual position. Why??
        leftClaw.setPosition(0.0);
        rightClaw.setPosition(0.0);
        leftClaw.setPosition(startPosition);
        rightClaw.setPosition(startPosition);
        leftClawOffset =   leftClaw.getPosition() - startPosition;
        rightClawOffset =  rightClaw.getPosition() + startPosition;
    }
    //Maybe we could set the zero position on servos physically before starting OpMode? idek...

    void moveClaw(double toPosition){
        if (toPosition >= clawPOSMax) {
            toPosition = clawPOSMax;
        }
        if (toPosition <= clawPOSMin) {
            toPosition = clawPOSMin;
        }
        leftClaw.setPosition(leftClawOffset + toPosition);
        rightClaw.setPosition(rightClawOffset - toPosition);
    }

    boolean debounceGamepad1Button(boolean input, int index){
        if (input != gamepad1PressedArray[index]){
            gamepad1PressedArray[index] = input;
            return input;
        }
        else
            return false;
    }

    void cTelemetry() {     //cTelemetry = Common Telemetry

    }
}