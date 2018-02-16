package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;



@Autonomous(name = "AutoOD Vuforia Red Short-Long", group = "OD_VF")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaRedShortLong extends AutoODVuforia
{
    double[][] getLeftSideProcedures(){
        return proceduresForLongSide(false, 0);
    }
    double[][] getCenterSideProcedures(){
        return proceduresForLongSide(false, 1);
    }
    double[][] getRightSideProcedures(){
        return proceduresForLongSide(false,2);
    }
}