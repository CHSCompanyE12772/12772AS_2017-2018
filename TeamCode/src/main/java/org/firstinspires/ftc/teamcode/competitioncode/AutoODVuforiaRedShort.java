package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * Autonomous OP mode to identify and act using vuforia mark identification,
 * child for Red corner with cryptobox on short side.
 */

@Autonomous(name = "AutoOD Vuforia Red Short", group = "OD")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaRedShort extends AutoODVuforia
{
    double[][] getLeftSideProcedures(){
        return proceduresForShortSide(true, 2);
    }
    double[][] getCenterSideProcedures(){
        return proceduresForShortSide(true, 1);
    }
    double[][] getRightSideProcedures(){ return proceduresForShortSide(true, 0);
    }
}