/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Test autonomous mode.
 */

@Autonomous(name = "SWForward4Feet", group = "TeamCode")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class SWForward4Feet extends LinearOpMode {

    Hardware12772 r = new Hardware12772(); //Use the shared hardware and function code.

    //Distance Variables
    private double numberOfFeet = 2.5;          //Distance desired to travel
    private double driveWheelDiamater = 3.5;    //Given in inches
    private int incrementsPerRevolution = 1500;            //Motor types read encoders at different rates. This is a rough empirical value
    private int targetPosition;

    @Override
    public void runOpMode() {
        r.init(hardwareMap, true);  //Initialization with safe space for shakes.

        // Wait for the game to start (driver presses PLAY)
        waitForStart();
        r.runtime.reset();
        r.leftDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        r.rightDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        r.leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        r.rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        targetPosition = (int)( (numberOfFeet*incrementsPerRevolution)*(12.0) / (driveWheelDiamater*Math.PI) );
        r.setDriveSpeed(r.driveSpeedMed);
        r.leftDrive.setTargetPosition(targetPosition);
        r.rightDrive.setTargetPosition(targetPosition);

        r.update();
        //To prevent major seizure near target destination, this code cuts off the motors once it is close enough
        while (Math.abs(r.leftDrive.getCurrentPosition()-r.leftDrive.getTargetPosition())>50){ //checks if close enough
            sleep(200);     // time interval between checking if close enough
        }
//        sleep(20000);     // give servos time to move before opMode ends, use if anti-seize code is not.
        r.setDriveSpeed(0.0);
    }
}