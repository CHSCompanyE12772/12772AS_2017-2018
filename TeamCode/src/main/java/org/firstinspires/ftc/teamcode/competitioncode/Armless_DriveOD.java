package org.firstinspires.ftc.teamcode.competitioncode;

/**
 * TeleOP that uses Hardware_OD_OmniDirection.
 */

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name="CHRISTIAN DriveOD", group="OD")
//@Disabled         //Enables or disables such OpMode (hide or show on Driver Station OpMode List)

public class Armless_DriveOD extends LinearOpMode {

    Armless_Hardware_OD_OmniDirection r = new Armless_Hardware_OD_OmniDirection(); //Use the shared hardware and function code.
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
            double[] motionCoords = g.rotateCoords(gamepad1.left_stick_x, gamepad1.left_stick_y);
            r.povDrive(motionCoords[0], motionCoords[1], gamepad1.left_trigger, gamepad1.right_trigger, r.driveSpeedStick);

            //Control claw position
            r.setServoPositionTwoButton(gamepad1.left_bumper, gamepad1.right_bumper, gamepad1.left_stick_button);

            r.update();

            telemetry.addData("Status",
                    "Run Time: " + r.runtime.toString()
            );
            telemetry.addData("Drive Speed", r.driveSpeedStick);
            telemetry.update();
        }
    }
}