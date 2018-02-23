package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled; // Leave this line here even when not used, please


/**
 * Autonomous OP mode to identify and act using vuforia mark identification,
 * child for Blue corner with cryptobox on short side.
 */

@Autonomous(name = "AutoOD Vuforia Blue Short", group = "OD_VF")
@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaBlueShort extends AutoODVuforia
{
    @Override double[][] getLeftSideProcedures(){
        return proceduresForShortSide(false, 0);
    }
    @Override double[][] getCenterSideProcedures(){
        return proceduresForShortSide(false, 1);
    }
    @Override double[][] getRightSideProcedures(){
        return proceduresForShortSide(false, 2);
    }
}