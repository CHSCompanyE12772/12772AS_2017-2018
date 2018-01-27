package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * Main TeleOP mode, currently (and probably forever will) uses Hardware12772.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="XXXX", group="TeamCode")
//@Disabled         //Enables or disables such OpMode (hide or show on Driver Station OpMode List)

public class SWFourByFour extends LinearOpMode {

    Hardware4x4 r = new Hardware4x4(); //Use the shared hardware and function code.
    General12772 g = new General12772(); //Use the shared general robot code.

    @Override //Does anyone know what this is or what it does?
    public void runOpMode() {
        r.init(hardwareMap, false); //initialization for non-autonomous code. NO SHAKES ALLOWED >:(
        g.init();

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        r.runtime.reset();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //Control drive motors
            r.setDriveSpeedWithButtons(
                    g.debounce(gamepad1.a,1,8),
                    g.debounce(gamepad1.b,1,7));
            r.povDrive(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.left_trigger, gamepad1.right_trigger, r.driveSpeedStick);

            //All runtime code in Hardware12772
            r.update();

            //BEGIN TELEMETRY SECTION. TELEMETRY WILL NOT WORK IF REFERENCED TO Hardware12772.java FOR SOME REASON!
            //I think its because telemetry is provided by TeleOP library, which only OP mode classes can use.
            telemetry.addData("Status",
                    "Run Time: " + r.runtime.toString()
            );
            telemetry.addData("Drive Speed", r.driveSpeedStick);
/*            telemetry.addData("Motor Power",
                    " leftDrive: " + r.leftDrive.getPower() +
                           " rightDrive: " + r.rightDrive.getPower() +
                           " Arm: " + r.mainArm.getPower()
            );
            telemetry.addData("Servo POS",
                    " clawPOS: " + r.clawsPOS +
                           " leftBottomClaw: " + r.leftBottomClaw.getPosition() +
                           " rightBottomClaw: " + r.rightBottomClaw.getPosition()
            );
            telemetry.addData("ClawOffsets",
                    "left: " + r.leftBottomClawOffset +
                           " right: " + r.rightBottomClawOffset
            );
*/            telemetry.update();
        }
    }
}