package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;



@Autonomous(name = "AutoOD Vuforia Red Short-Long", group = "OD_VF")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaRedShortLong extends AutoODVuforia
{
    @Override double[][] getLeftSideProcedures(){
        return proceduresForLongSide(false, 0);
    }
    @Override double[][] getCenterSideProcedures(){
        return proceduresForLongSide(false, 1);
    }
    @Override double[][] getRightSideProcedures(){
        return proceduresForLongSide(false,2);
    }
}