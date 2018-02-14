package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Arrays;


/**
 * Autonomous OP mode to identify and act using vuforia mark identification,
 * child for Red corner with cryptobox on long side.
 */

@Autonomous(name = "AutoOD Vuforia Red Long", group = "OD")
//@Disabled                            //Enables or disables such OpMode (hide or show on Driver Station OpMode List)
public class AutoODVuforiaRedLong extends AutoODVuforia
{
    double[][] getLeftSideProcedures(){
        return proceduresForLongSide(true);
    }
    double[][] getCenterSideProcedures(){
        return proceduresForLongSide(true);
    }
    double[][] getRightSideProcedures(){ return proceduresForLongSide(true);
    }
}