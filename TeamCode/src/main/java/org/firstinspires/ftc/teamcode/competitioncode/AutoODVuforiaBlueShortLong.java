package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;



@Autonomous(name = "AutoOD Vuforia Blue Short-Long", group = "OD_VF")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaBlueShortLong extends AutoODVuforia
{
    double[][] getLeftSideProcedures(){
        return proceduresForLongSide(true, 2);
    }
    double[][] getCenterSideProcedures(){
        return proceduresForLongSide(true,1);
    }
    double[][] getRightSideProcedures(){
        return proceduresForLongSide(true,0);
    }
}