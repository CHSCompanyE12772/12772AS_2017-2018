package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Main Hardware class for robot.
 * Used for common functions between OP modes, can probably be used between similar robots.
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

    //Motor power variables
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
    //do we need an offset for each claw or can we just use one offset for both?
    //no, the offset is to correct the individual imperfect servos.

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
    //I'm so sorry for this, I don't know a better way, blame Cruz for leaving... and Sherman for
    // being such a bad teacher! <-- Implying she teaches.
    boolean[] gamepad1PressedArray = {
            false, false, false, false, false,
            false, false, false, false, false,
            false, false, false, false, false,
    };
    //the multi-dimentsional array below should replace gamepad1.PressedArray.
    boolean[][] debouncePressedArray = new boolean[3][15]; //3 gamepads, 15 debounce buttons each.
    //See 'legends for PressedArrays.txt' for which index means what.
    //Currently empty, needs to be updated to match notebook when school resumes.

    /* local OpMode members. */
    HardwareMap hwMap           =  null;
    private ElapsedTime period  = new ElapsedTime();

    /* Constructor */
    Hardware12772(){
    }
    //Any purpose for this?
    //It's a default constructor. The code will probably work without it, but we may as well leave
    //it here just in case.

    //Main function called for initialization stage
    void init(HardwareMap ahwMap, boolean isAuto) {
        // Save reference to Hardware map
        hwMap = ahwMap;

        for(int i = debouncePressedArray.length - 1; i>=0; i--) //initializes all gamepad debouncers
            Arrays.fill(debouncePressedArray[i], false);

        /*
           Initialize the hardware variables. Note that the strings used here as parameters
           to 'get' must correspond to the names assigned during the robot configuration
           step (using the FTC Robot Controller app on the phone).
        */
        leftDrive = hwMap.get(DcMotor.class, "leftDrive");   //LEFT DRIVE WHEEL MOTOR
        rightDrive = hwMap.get(DcMotor.class, "rightDrive");  //RIGHT DRIVE WHEEL MOTOR
        leftClaw = hwMap.get(Servo.class, "leftClaw");      //LEFT CLAW SERVO
        rightClaw = hwMap.get(Servo.class, "rightClaw");      //RIGHT CLAW SERVO
        mainArm = hwMap.get(DcMotor.class, "mainArm");      //ARM MOTOR

        // Since motors face opposite on each side, one drive motor needs to be reversed.
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.FORWARD);
        rightDrive.setDirection(DcMotor.Direction.REVERSE);
        // This arm is backwards too, probably.
        mainArm.setDirection(DcMotor.Direction.REVERSE);

        // Set all motors to zero power, juuuust in case
        leftDrive.setPower(0);
        rightDrive.setPower(0);
        mainArm.setPower(0);

        /*
        RELEASE THE SHAKIN'!! Running using encoders causes motors to shake a bit, so best to
        avoid when possible.
        */
        if (isAuto) {
            leftDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
        else {
            leftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            rightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        }
        mainArm.setMode(DcMotor.RunMode.RESET_ENCODERS); //resting position set to zero
        mainArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER); //Default & shakeless. OP modes can change this if needed.
        mainArmPower = mainArmPowerMax;
        mainArm.setTargetPosition(0);
    }

    //Main function usually called repeatedly after 'Start'
    void update(){
        // Send calculated power to DRIVE MOTORS
        leftDrive.setPower(leftDrivePower);
        rightDrive.setPower(rightDrivePower);
        // Send calculated position to SERVOS
        moveClaw(clawsPOS);

        //Mutilated code for mainArm below.

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
    }

    //set drivePower given single-joystick input
    void povDrive(double x, double y, double speed){
        //I think this should use Range.scale(y -+ x, -1.0, 1.0, -speed, speed); instead.
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

    //legacy function to convert 3-button input to array index
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

    void setArmPositionJoystick(double y, double x, boolean toggleHolding, boolean movingToResting){
        if (movingToResting) { //when moveToResting button is held, arm motor uses encoders to move self.
            mainArm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            mainArmPower = -0.1;
            mainArm.setTargetPosition(0);
        }
        else { //otherwise, joystick is used to control arm motor power.
            mainArm.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

            //Some math that works for some reason
            double OutputMax = mainArmPowerMax;
            if (y < 0)
                OutputMax = 1.1 * mainArmHoldingPower;
            mainArmPower = y * OutputMax;

            if (mainArmHolding) //Give additional power if holding, allowing user to release joystick without arm falling.
                mainArmPower += mainArmHoldingPower;
            if (toggleHolding) //used debounced button to toggle if holding.
                mainArmHolding = !mainArmHolding;
        }
    }

    void setServoPositionTwoButton(boolean in1, boolean in2){
        double incr = 0.025;
        if (in1)
            clawsPOS += incr;
        if (in2)
            clawsPOS -= incr;

        //I think this code can be simplified using:
        clawsPOS = Range.clip(clawsPOS, clawPOSMin, clawPOSMax);
    }

    //Bugged, .getPosition is always returning zero, regardless of actual position. Why??
    //Maybe we could set the zero position on servos physically before starting OpMode? idek...
    //Maybe, but we don't know where zero is given each servo's offset.
    //Perhaps moving the claw during init would help?
    void initClawServosPOS(double startPosition){
        leftClaw.setPosition(0.0);
        rightClaw.setPosition(0.0);
        leftClaw.setPosition(startPosition);
        rightClaw.setPosition(startPosition);
        leftClawOffset =   leftClaw.getPosition() - startPosition;
        rightClawOffset =  rightClaw.getPosition() + startPosition;
    }

    //set positions of claw servos
    void moveClaw(double toPosition){
        leftClaw.setPosition(leftClawOffset + toPosition);
        rightClaw.setPosition(rightClawOffset - toPosition);
    }

    /* BETTER VERSION OF PREVIOUS FUNCTION, if it works.*/
    boolean debounce(boolean input, int gamepadNumber, int buttonIndex){
        if (input != debouncePressedArray[gamepadNumber][buttonIndex]){
            debouncePressedArray[gamepadNumber][buttonIndex] = input;
            return input;
        }
        else return false;
    }

    void cTelemetry() {     //cTelemetry = Common Telemetry
        /*
         ¯\_(ツ)_/¯
        */

    }
}