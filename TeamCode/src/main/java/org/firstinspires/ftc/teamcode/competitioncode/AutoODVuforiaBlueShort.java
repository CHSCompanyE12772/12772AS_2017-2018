package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


/**
 * Autonomous OP mode to identify and act using vuforia mark identification,
 * child for Blue corner with cryptobox on short side.
 */

@Autonomous(name = "AutoOD Vuforia Blue Short", group = "OD")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaBlueShort extends AutoODVuforia
{
    double[][] getLeftSideProcedures(){
        return proceduresForShortSide(false);
    }
    double[][] getCenterSideProcedures(){
        return proceduresForShortSide(false);
    }
    double[][] getRightSideProcedures(){ return proceduresForShortSide(false);
    }
}