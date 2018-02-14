package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * Autonomous OP mode to identify and act using vuforia mark identification,
 * child for Blue corner with cryptobox on long side.
 */

@Autonomous(name = "AutoOD Vuforia Blue Long", group = "OD")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaBlueLong extends AutoODVuforia
{
    double[][] getLeftSideProcedures(){
        return proceduresForLongSide(false, 0);
    }
    double[][] getCenterSideProcedures(){
        return proceduresForLongSide(false, 1);
    }
    double[][] getRightSideProcedures(){ return proceduresForLongSide(false,2);
    }
}