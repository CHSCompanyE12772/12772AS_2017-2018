package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Created by Jose on 28 Nov.
 * Last modified 17 Dec by Carlos
 * Main TeleOp Program (or soon to be). Godspeed.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

@TeleOp(name="Main SW Drive", group="TeamCode")
//@Disabled             //Enables or disables such OpMode (hide or show on Driver Station OpMode List


public class SWDefaultDrive extends LinearOpMode {
    // Declare OpMode members.
    Hardware12772 r = new Hardware12772(); //Use the shared hardware and function code.

    @Override
    public void runOpMode() {
        r.init(hardwareMap, false);
        r.clawsPOS = 0.5;
//        r.initClawServosPOS(r.clawsPOS);
        //Can't get r.initClawServosPOS to work, so manually set offsets below. See method for details on not working.
        r.leftClawOffset = 0.1;
        r.rightClawOffset = 1.0;

        waitForStart();
        r.runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            r.setDriveSpeedWithButtons(gamepad1.a,gamepad1.b,gamepad1.x);
            r.povDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, r.driveSpeedStick);
            r.setArmPositionDPad(gamepad1.dpad_up,gamepad1.dpad_right,gamepad1.dpad_down,gamepad1.dpad_left); //Disable when reading arm position
//            r.setArmPositionCareful(gamepad1.dpad_up,gamepad1.dpad_right,gamepad1.dpad_down,gamepad1.dpad_left); //Use to read arm position.
            r.setArmPositionJoystick(gamepad1.right_stick_y,gamepad1.right_stick_x,r.debounceGamepad1Button(gamepad1.right_stick_button,11));
            r.setServoPositionTwoButton(gamepad1.left_bumper, gamepad1.right_bumper);
            r.update();


            //BEGIN TELEMETRY SECTION. TELEMETRY WILL NOT WORK IF REFERENCED TO Hardware12772.java FOR SOME REASON!
            telemetry.addData("Status", "Run Time: " + r.runtime.toString());
            telemetry.addData("Drive Speed", r.driveSpeedStick);
            telemetry.addData("Motor Power", "leftDrive: " + r.leftDrive.getPower() + " rightDrive: " + r.rightDrive.getPower() + " Arm: " + r.mainArm.getPower());
            telemetry.addData("Servo POS", " clawPOS: " + r.clawsPOS + "leftClaw: " + r.leftClaw.getPosition() + " rightClaw: " + r.rightClaw.getPosition());
            //telemetry.addData("Claw Adjustment Coefficient/Offset", r.clawsOffset);
            telemetry.addData("LeftClawAdjustments","Offset: " + r.leftClawOffset);
            telemetry.addData("RightClawAdjustments","Offset: " + r.rightClawOffset);
//            telemetry.addData("Arm ACT POS", r.mainArm.getCurrentPosition());
            telemetry.update();

        }

    }
}