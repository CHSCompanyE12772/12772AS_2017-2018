package org.firstinspires.ftc.teamcode.oldcode;

/**
 * Created by Carlos on 11/7/2017.
 * Last updated by Josika on 6 Dec
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="Drive 9126 fuck", group="TeamCode")
@Disabled             //Enables or disables such OpMode (hide or show on Driver Station OpMode List

public class Drive9126 extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private Servo colorDistanceArm = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    private DcMotor leftArm = null;
    private DcMotor rightArm = null;
    private ColorSensor colorSensor = null;
    private DistanceSensor distanceSensor = null;

    // Setup a variable for each drive wheel to save power level for telemetry
    private double leftDrivePower;
    private double rightDrivePower;
    private double colorDistanceArmPOS = 0;
    private double clawsPOS = 0;
    private double leftArmPower;
    private double rightArmPower;

    // SERVO MAX AND MIN POS
    private double clawMAXPOS = 0.35;  //THESE ARE DONE DON'T PLAY WITH THESE VALUES
    private double clawMINPOS = 0;
    private double colorDistanceArmMAXPOS = 1;
    private double colorDistanceArmMINPOS = 0;

    //MISCELLANEOUS VARS
    private double driveSpeedMin = 0.25;
    private double driveSpeedMed = 0.5;
    private double driveSpeedMax = 1.0;
    private double driveSpeed = driveSpeedMed;
    private double drive = 0;
    private double turn = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("SYSTEM STATUS", "READY");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");   //LEFT FRONT WHEEL MOTOR
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");  //RIGHT FRONT WHEEL MOTOR
        //colorDistanceArm = hardwareMap.get(Servo.class, "colorArm");        //COLOR SENSOR ARM SERVO
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");      //LEFT CLAW SERVO
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");      //RIGHT CLAW SERVO
        leftArm = hardwareMap.get(DcMotor.class, "leftArm");      //LEFT ARM MOTOR
        rightArm = hardwareMap.get(DcMotor.class, "rightArm");      //RIGHT ARM MOTOR
        //colorSensor = hardwareMap.get(ColorSensor.class, "colorDistanceSensor");    //COLOR AND DISTANCE SENSOR
        //distanceSensor = hardwareMap.get(DistanceSensor.class, "colorDistanceSensor");

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        leftArm.setDirection(DcMotor.Direction.REVERSE);
        rightArm.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses PLAY)
        //SET INITIAL SERVO POSITIONS
        clawsPOS = 0.35;
        leftClaw.setPosition(clawsPOS);
        rightClaw.setPosition(0.91-clawsPOS);
        //colorDistanceArm.setPosition(0.275);

        telemetry.addData("Motor Power", "leftDrive: " + leftDrive.getPower() + " rightDrive: " + rightDrive.getPower() + " Arm (TOTAL): " + ((leftArm.getPower() + rightArm.getPower())/2));
        telemetry.addData("Servo POS", "leftClaw: " + leftClaw.getPosition() + " rightClaw: " + rightClaw.getPosition()); //+ " colorArm: " + colorDistanceArm.getPosition());
        //telemetry.addData("Color Sensor RGB Values: ", colorSensor.argb());
        //telemetry.addData("Distance Sensor Range: ", distanceSensor.getDistance(DistanceUnit.CM));
        telemetry.update();

        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            // Choose to drive using either Tank Mode, or POV Mode
            // Comment out the method that's not used.  The default below is POV.

            //MODIFIED SINGLE JOYSTICK DRIVING MODE
            //LEFT JOYSTICK ONLY! Controls both wheels and can go forward, backward, left, right, etc.
            drive = -gamepad1.left_stick_y;
            turn = gamepad1.left_stick_x;
            leftDrivePower = Range.clip(drive + turn, -1.0, 1.0);
            rightDrivePower = Range.clip(drive - turn, -1.0, 1.0);

            //Used to find better speeds
/*            if (gamepad1.a) {
                driveSpeed += .0001;
                if (driveSpeed >= driveSpeedMax) {
                    driveSpeed = driveSpeedMax;
                }
            }
            if (gamepad1.b) {
                driveSpeed -= .0001;
                if (driveSpeed <= driveSpeedMin) {
                    driveSpeed = driveSpeedMin;
                }
            }
*/
            if (gamepad1.a) {
                driveSpeed = driveSpeedMax;
            }
            if (gamepad1.b) {
                driveSpeed = driveSpeedMin;
            }
            if (gamepad1.x) {
                driveSpeed = driveSpeedMed;
            }

            leftDrivePower *= driveSpeed;
            rightDrivePower *= driveSpeed;
            // POV Mode uses left stick to go forward, and right stick to turn.
            // - This uses basic math to combine motions and is easier to drive straight.
            // double drive = -gamepad1.right_stick_y;
            // double turn  =  gamepad1.left_stick_x;
            // leftPower    = Range.clip(drive + turn, -1.0, 1.0) ;
            // rightPower   = Range.clip(drive - turn, -1.0, 1.0) ;

            //TURBO DRIVING MODE SECTION

            //Arm up or down on RIGHT JOYSTICK
            double armUpDown = gamepad1.right_stick_y * 0.2;
            leftArmPower = Range.clip(armUpDown, -1.0, 1.0);
            rightArmPower = Range.clip(armUpDown, -1.0, 1.0);

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

            //claw movement
            //SERVO positions range from 0 to 1! 0.5 would be the middle!
            if (gamepad1.left_bumper) {
                clawsPOS += 0.01;
            }
            if (gamepad1.right_bumper) {
                clawsPOS -= 0.01;
            }

            //colorArm movement
            if (gamepad1.dpad_left) {
                colorDistanceArmPOS -= 0.005;
            }
            if (gamepad1.dpad_right) {
                colorDistanceArmPOS += 0.005;
            }

            //SERVO MAX AND MIN POS
            //CLAWS
            if (clawsPOS >= clawMAXPOS) {
                clawsPOS = clawMAXPOS;
            }
            if (clawsPOS <= clawMINPOS) {
                clawsPOS = clawMINPOS;
            }

            // COLOR AND DISTANCE SENSOR ARM
            if (colorDistanceArmPOS >= colorDistanceArmMAXPOS) {
                colorDistanceArmPOS = colorDistanceArmMAXPOS;
            }
            if (colorDistanceArmPOS <= colorDistanceArmMINPOS) {
                colorDistanceArmPOS = colorDistanceArmMINPOS;
            }

            // Send calculated power to MOTORS
            leftDrive.setPower(leftDrivePower);
            rightDrive.setPower(rightDrivePower);
            leftArm.setPower(leftArmPower);
            rightArm.setPower(rightArmPower);

            // Send calculated position to SERVOS
            leftClaw.setPosition(clawsPOS);
            rightClaw.setPosition(0.91-clawsPOS);
            //colorDistanceArm.setPosition(colorDistanceArmPOS);

            //TELEMETRY SECTION
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Speed", driveSpeed);
            telemetry.addData("Motor Power", "leftDrive: " + leftDrive.getPower() + " rightDrive: " + rightDrive.getPower() + " Arm (TOTAL): " + ((leftArm.getPower() + rightArm.getPower())/2));
            telemetry.addData("Servo POS", "leftClaw: " + leftClaw.getPosition() + " rightClaw: " + rightClaw.getPosition()); //+ " colorArm: " + colorDistanceArm.getPosition());
            //telemetry.addData("Color Sensor RGB Values: ", colorSensor.argb());
            //telemetry.addData("Distance Sensor Range: ", distanceSensor.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
}
