package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Created by Josie on 10 Nov.
 * Last modified 10 Nov by Josiphina
 * Main Program for driving Company E robot
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Drive 12772", group="TeamCode")
//@Disabled             //Enables or disables such OpMode (hide or show on Driver Station OpMode List

public class Drive12772 extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    private DcMotor mainArm = null;
    //private ColorSensor colorDistanceSensor = null;

    // Setup a variable for each drive wheel to save power level for telemetry
    private double leftDrivePower;
    private double rightDrivePower;
    private double clawsPOS = 0;
    private double mainArmPower;

    // SERVO MAX AND MIN POS
    private double clawMAXPOS = 1;
    private double clawMINPOS = 0;

    //MISCELLANEOUS VARS
    private double mainArmPowerMax = 0.75;
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
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");      //LEFT CLAW SERVO
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");      //RIGHT CLAW SERVO
        mainArm = hardwareMap.get(DcMotor.class, "mainArm");      //ARM MOTOR

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        mainArm.setDirection(DcMotor.Direction.REVERSE);

        // Wait for the game to start (driver presses PLAY)
        //SET INITIAL SERVO POSITIONS
        clawsPOS = 0;
        leftClaw.setPosition(0.1+clawsPOS);
        rightClaw.setPosition(1-clawsPOS);

        telemetry.addData("Motor Power", "leftDrive: " + leftDrive.getPower() + " rightDrive: " + rightDrive.getPower() + " Arm: " + mainArm.getPower());
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

            // Tank Mode uses one stick to control each wheel.
            // - This requires no math, but it is hard to drive forward slowly and keep straight.
            // leftPower  = -gamepad1.left_stick_y ;
            // rightPower = -gamepad1.right_stick_y ;

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

            //Arm up or down on RIGHT JOYSTICK
            double armUpDown = gamepad1.right_stick_y * mainArmPowerMax;
            mainArmPower = Range.clip(armUpDown, -1.0, 1.0);


            //claw movement
            //SERVO positions range from 0 to 1! 0.5 would be the middle!
            if (gamepad1.left_bumper) {
                clawsPOS -= 0.025;
            }
            if (gamepad1.right_bumper) {
                clawsPOS += 0.025;
            }

            //SERVO MAX AND MIN POS
            //CLAWS
            if (clawsPOS >= clawMAXPOS) {
                clawsPOS = clawMAXPOS;
            }
            if (clawsPOS <= clawMINPOS) {
                clawsPOS = clawMINPOS;
            }


            // Send calculated power to MOTORS
            leftDrive.setPower(leftDrivePower);
            rightDrive.setPower(rightDrivePower);
            mainArm.setPower(mainArmPower);

            // Send calculated position to SERVOS
            leftClaw.setPosition(0.1+clawsPOS);
            rightClaw.setPosition(1-clawsPOS);

            //TELEMETRY SECTION
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Speed", driveSpeed);
            telemetry.addData("Motor Power", "leftDrive: " + leftDrive.getPower() + " rightDrive: " + rightDrive.getPower() + " Arm: " + mainArm.getPower());
            telemetry.addData("Servo POS", "leftClaw: " + leftClaw.getPosition() + " rightClaw: " + rightClaw.getPosition());
            //telemetry.addData("Color Sensor RGB Values: ", colorDistanceSensor.sensorColor.argb());
            //telemetry.addData("Distance Sensor Range: ", colorDistanceSensor.sensorDistance.getDistance(DistanceUnit.CM));
            telemetry.update();
        }
    }
}
