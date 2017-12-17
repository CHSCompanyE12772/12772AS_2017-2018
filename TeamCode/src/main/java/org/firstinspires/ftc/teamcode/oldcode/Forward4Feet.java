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

package org.firstinspires.ftc.teamcode.oldcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;


/**
 * Created by WHY SAYORI?! (Jose) on 21 Nov.
 * Last modified 6 Dec(on accident) by Jose :_(
 * Moves both wheels forward. Will be used as control to test integration of class hardware methods.
 */

@Autonomous(name = "Move 4 Feet", group = "Forward")
@Disabled                            // Comment this out to add to the opmode list
public class Forward4Feet extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    //Speed variables
    private double leftDrivePower;
    private double rightDrivePower;
    private double driveSpeed1 = 0.5;

    //Distance Variables
    private double numberOfFeet = 4.0;          //Distance desired to travel
    private double driveWheelDiamater = 3.5;    //Given in inches
    private int incrementsPerRevolution = 1500;            //Different motor types will read encoders at different constants
    private int targetPosition;



    @Override
    public void runOpMode() {
        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "leftDrive");
        rightDrive  = hardwareMap.get(DcMotor.class, "rightDrive");

        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrivePower = driveSpeed1;
        rightDrivePower = driveSpeed1;

        // wait for the start button to be pressed.
        waitForStart();
        runtime.reset();
        leftDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        rightDrive.setMode(DcMotor.RunMode.RESET_ENCODERS);
        leftDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        rightDrive.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        targetPosition = (int)( (numberOfFeet*incrementsPerRevolution)*(12.0) / (driveWheelDiamater*Math.PI) );
        leftDrive.setTargetPosition(targetPosition);
        rightDrive.setTargetPosition(targetPosition);

        leftDrive.setPower(leftDrivePower);
        rightDrive.setPower(rightDrivePower);
        sleep(20000);     // pause for servos to move
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

    }
}