package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;



@Autonomous(name = "AutoOD Vuforia Blue Short-Long", group = "OD_VF")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaBlueShortLong extends AutoODVuforia
{
    @Override double[][] getLeftSideProcedures(){
        return proceduresForLongSide(true, 2);
    }
    @Override double[][] getCenterSideProcedures(){
        return proceduresForLongSide(true,1);
    }
    @Override double[][] getRightSideProcedures(){
        return proceduresForLongSide(true,0);
    }
}