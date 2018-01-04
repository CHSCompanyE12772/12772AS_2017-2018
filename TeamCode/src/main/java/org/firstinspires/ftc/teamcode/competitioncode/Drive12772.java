package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Legacy OP Mode to control robot in TeleOP. Does not use a separate hardware class.
*/

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Legacy Drive", group="TeamCode")
//@Disabled             //Enables or disables such OpMode (hide or show on Driver Station OpMode List

public class Drive12772 extends LinearOpMode {
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;
    private Servo leftClaw = null;
    private Servo rightClaw = null;
    private DcMotor mainArm = null;

    //Motor power variables
    private double leftDrivePower;
    private double rightDrivePower;
    private double mainArmPower;
    //Max speeds which motors will be set to move
    private double mainArmPowerMax = 0.75;
    private double driveSpeedMin = 0.25;
    private double driveSpeedMed = 0.5;
    private double driveSpeedMax = 1.0;
    private double driveSpeed = driveSpeedMed;
    //POV drive variables
    private double drive = 0;
    private double turn = 0;

    //Claw servo variables
    private double clawsPOS = 0;
    //these variables control how far claw is allowed to  open.
    //Zero is fully closed, One is fully opened 180 degrees around. Change these to limit that motion.
    private double clawMAXPOS = 1;
    private double clawMINPOS = 0;

    @Override
    public void runOpMode() {
        telemetry.addData("SYSTEM STATUS", "READY");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");   //LEFT DRIVE WHEEL MOTOR
        rightDrive = hardwareMap.get(DcMotor.class, "rightDrive");  //RIGHT DRIVE WHEEL MOTOR
        leftClaw = hardwareMap.get(Servo.class, "leftClaw");      //LEFT CLAW SERVO
        rightClaw = hardwareMap.get(Servo.class, "rightClaw");      //RIGHT CLAW SERVO
        mainArm = hardwareMap.get(DcMotor.class, "mainArm");      //ARM MOTOR

        // Since motors face opposite on each side, one drive motor needs to be reversed.
        // Reverse the motor that runs backwards when connected directly to the battery
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);
        // This arm is backwards too, probably.
        mainArm.setDirection(DcMotor.Direction.REVERSE);

        //SET INITIAL SERVO POSITIONS
        clawsPOS = 0;
        leftClaw.setPosition(0.1+clawsPOS);
        rightClaw.setPosition(1-clawsPOS);

        telemetry.addData("Motor Power",
                "leftDrive: " + leftDrive.getPower() +
                       " rightDrive: " + rightDrive.getPower() +
                       " Arm: " + mainArm.getPower()
        );
        telemetry.addData("Servo POS",
                "leftClaw: " + leftClaw.getPosition() +
                       " rightClaw: " + rightClaw.getPosition()
        );
        telemetry.update();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            //SINGLE JOYSTICK DRIVING MODE
            //Controls both wheels and can go forward, backward, turn left or right.
            drive = -gamepad1.left_stick_y;
            turn = gamepad1.left_stick_x;
            leftDrivePower = Range.clip(drive + turn, -1.0, 1.0);
            rightDrivePower = Range.clip(drive - turn, -1.0, 1.0);

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
            mainArmPower = Range.clip(armUpDown, -1.0, 1.0); //I don't think this line is necessary.


            //claw movement
            //SERVO positions range from 0 to 1! 0.5 would be the middle!
            if (gamepad1.left_bumper) {
                clawsPOS -= 0.025;
            }
            if (gamepad1.right_bumper) {
                clawsPOS += 0.025;
            }

            //SERVO MAX AND MIN POS
            //I think this code can be simplified using:
            //clawPOS = Range.clip(clawPOS, clawMINPOS, clawMAXPOS);
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
            //I gag at this old code
            leftClaw.setPosition(0.1+clawsPOS);
            rightClaw.setPosition(1-clawsPOS);

            //TELEMETRY SECTION
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Drive Speed", driveSpeed);
            telemetry.addData("Motor Power", "leftDrive: " + leftDrive.getPower() + " rightDrive: " + rightDrive.getPower() + " Arm: " + mainArm.getPower());
            telemetry.addData("Servo POS", "leftClaw: " + leftClaw.getPosition() + " rightClaw: " + rightClaw.getPosition());
            telemetry.update();
        }
    }
}