package org.firstinspires.ftc.teamcode.competitioncode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled; // Leave this line here even when not used, please

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuMarkInstanceId;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import java.util.Arrays;


/**
 * Autonomous "OP mode" to identify and act using vuforia mark identification, parent to each corner's OP mode.
 */

@Autonomous(name = "[Error! Please disable AutoODVuforia]", group = "OD_VF") //The name should never be seen, thus it is an error me.

/**Because this is an abstract class and will not run, we will disable it to prevent it from being
 * seen on Driver Station. Children of this superclass do not inherit @Disabled.*/
@Disabled

public abstract class AutoODVuforia extends LinearOpMode {

    Hardware_OD_OmniDirection r = new Hardware_OD_OmniDirection();
    General12772 g = new General12772(); //Use the shared general robot code.

    VuforiaLocalizer vuforia;   //Variable is a reference to the instance of the Vuforia localization/tracking engine

    @Override
    public void runOpMode() {
        r.mainArmPower = 0;
        r.init(hardwareMap, false);  //Initialization with safe space for snowflake-shakes.
        r.isAutoWorkAround = true;
        r.clawsPOS = 0.1;  //Claws are set to a closed position
//        r.initClawServosPOS(r.clawsPOS); //"When you try your best but you don't succeed..."
        r.leftBottomClawOffset = 0.1;
        r.rightBottomClawOffset = 1.0;

        //Use these two lines of code below for displaying camera, OR use parameterless line below that for non-displayed camera.
        int cameraMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);

        /*
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        */

        parameters.vuforiaLicenseKey = g.ourVuforiaLicenseKey;

        /** Indicate which camera on the RC to use. Here we chose the back (HiRes) camera (for
         * greater range), but the front camera might be more convenient. */
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        /**Load the data set containing the VuMarks for Relic Recovery. There's only one trackable
         * in this data set: all three of the VuMarks in the game were created from this one template,
         * but differ in their instance id information.
         * @see VuMarkInstanceId */
        VuforiaTrackables relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        VuforiaTrackable relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary

        telemetry.update();
        //Wait for the game to start (driver presses PLAY)
        waitForStart();
        r.runtime.reset();
        r.update();

        relicTrackables.activate();   //Begin looking for and identifying set of VuMarks


        /**
         * See if any of the instances of {@link relicTemplate} are currently visible.
         * {@link RelicRecoveryVuMark} is an enum which can have the following values:
         * UNKNOWN, LEFT, CENTER, and RIGHT. When a VuMark is visible, something other than
         * UNKNOWN will be returned by {@link RelicRecoveryVuMark#from(VuforiaTrackable)}.
         */
        RelicRecoveryVuMark vuMark;
        do {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            telemetry.addData("VuMark:", vuMark);
            telemetry.update();
        } while (vuMark == RelicRecoveryVuMark.UNKNOWN && r.runtime.seconds() <= 5); //Wait until mark is found or time is running out

        double[][] fieldMotions = new double[12][5];
        for (int i = fieldMotions.length - 1; i >= 0; i--) //initializes empty procedure list.
            Arrays.fill(fieldMotions[i], 0.0);

        r.raiseArmSlightly(true);
        r.isAutoWorkAround = false;
        r.update();
        sleep(200);
        r.raiseArmSlightly(false);
        r.update();

        if (vuMark == RelicRecoveryVuMark.LEFT) {
            fieldMotions = getLeftSideProcedures();

        } else if (vuMark == RelicRecoveryVuMark.CENTER) {
            fieldMotions = getCenterSideProcedures();

        } else if (vuMark == RelicRecoveryVuMark.RIGHT) {
            fieldMotions = getRightSideProcedures();

        } else { //If it cannot determine the mark, guess the middle one.
            fieldMotions = getCenterSideProcedures();
        }
        /**fieldMotions should contain [i, j, cw, speed, time]*/
        for (double[] motion : fieldMotions) {
            //i,j,acw(0),cw,speed; time
            r.povDrive(motion[0], motion[1], 0, motion[2], motion[3]);
            r.update();
            sleep((long) motion[4]);
        }
        r.lowerArmSlightly(true);
        r.update();
        sleep(300);
        r.lowerArmSlightly(false);
        r.update();
        sleep(300);
        //FIXME: Sorry about this...
        //TODO: Concat fieldMotions to the following procedures before execution.
        //Move backwards after cube dropped
        double[] lastCoords = g.rotateCoords(0, -1);
        r.povDrive(lastCoords[0],lastCoords[1],0,0, r.driveSpeedMin);
        r.update();
        sleep(300);
        //Hold up, while arm does the opposite.
        r.povDrive(0,0,0,0,0);
        r.update();
        sleep(1000);
        //Ram cube into slot!!
        lastCoords = g.rotateCoords(0, 1);
        r.povDrive(lastCoords[0],lastCoords[1],0,0, r.driveSpeedMed);
        r.update();
        sleep(200);
        //Move backwards after cube is rammed
        lastCoords = g.rotateCoords(0, -1);
        r.povDrive(lastCoords[0],lastCoords[1],0,0, r.driveSpeedMin);
        r.update();
        sleep(300);
        //It's time to STOP.
        r.povDrive(0,0,0,0,0);
        r.update();

    }
    /**Rotate and prepare inputs to be used by POV drive method for translating*/
    double[] fieldTranslate(double x, double y, double speed, long time){
        //+y is forward, +x is right.
        //coordinates returned are rotated to i-j axes. See povDrive method in hardware class for details.
        return g.concat(g.rotateCoords(x, y), new double[]{0, speed, time});
    }
    /**Prepare inputs to be used by POV drive method for translating, easier for user.*/
    double[] fieldRotate(boolean clockwise, double speed, long time){
        if (clockwise) speed *= -1;
        return new double[]{0, 0, 1, speed, time};
    }
    abstract double[][] getLeftSideProcedures();
    abstract double[][] getCenterSideProcedures();
    abstract double[][] getRightSideProcedures();


    final double[][] testProcedures = new double[][]{
            fieldTranslate(-1,0, r.driveSpeedMin,1500),
            fieldTranslate(1,0, r.driveSpeedMin,1500),
            fieldTranslate(0,1, r.driveSpeedMin,1500),
            fieldTranslate(0,-1, r.driveSpeedMin,1500),
            fieldRotate(true,0.5 * r.driveSpeedMin, 2000),
            fieldRotate(false,0.5 * r.driveSpeedMin, 2000),
    };
    double[][] proceduresForLongSide(boolean isRed, int posDist) {
        /**Procedures for red side.*/
        /**posDist determines farness of column.*/
        double[][] procedures = new double[][]{
                fieldTranslate(1,0, r.driveSpeedMin,1500), /**Overridden by switch*/
                fieldTranslate(0,1,r.driveSpeedMin,300),
                fieldRotate(true,0.5 * r.driveSpeedMin, 1700),
                fieldTranslate(0,1,r.driveSpeedMin,1000),
                fieldTranslate(0,0,0,500),
        };
        switch (posDist) {
            case 0: /**Closest column, could be right or left.*/
                procedures[0] = fieldTranslate(1,0, r.driveSpeedMin,1000); // 800
                break;
            case 1: /**Center column.*/
                procedures[0] = fieldTranslate(1,0, r.driveSpeedMin,1400); // 1200
                break;
            case 2: /**Farthest column, could be left or right.*/
                procedures[0] = fieldTranslate(1,0, r.driveSpeedMin,1800); // 1600
                break;
        }
        /**Parallel to procedures array. Stores which motions are mirrored for opposite color.*/
        boolean[] mirroredWhenBlue = new boolean[]{
                true,
                false,
                true,
                false,
                false
        };
        /**Mirror appropriate values for blue side.*/
        if (!isRed) procedures = mirrorProcedures(procedures,mirroredWhenBlue);

        return procedures;
    }

    double[][] proceduresForShortSide(boolean isRed, int posDist) {
        /**Same as proceduresForLongSide but with different procedures and mirroredWhenBlue. Could
         * probably be condensed, but doesn't really need to be.*/
        double[][] procedures = new double[][]{
                fieldTranslate(0,1, r.driveSpeedMin,1500), /**Overridden by switch*/
                fieldRotate(true,0.5 * r.driveSpeedMin, 1000),
                fieldTranslate(0,1,r.driveSpeedMin,500),
        };
        switch (posDist) {
            case 0: /**Closest column, could be right or left.*/
                procedures[0] = fieldTranslate(1,0, r.driveSpeedMin,1500);
                break;
            case 1: /**Center column.*/
                procedures[0] = fieldTranslate(1,0, r.driveSpeedMin,3000);
                break;
            case 2: /**Farthest column, could be left or right.*/
                procedures[0] = fieldTranslate(1,0, r.driveSpeedMin,4500);
                break;
        }
        /**Parallel to procedures array. Stores which motions are mirrored for opposite color.*/
        boolean[] mirroredWhenBlue = new boolean[]{
                true,
                true,
                false,
        };
        /**Mirror appropriate values for blue side.*/
        if (!isRed) procedures = mirrorProcedures(procedures,mirroredWhenBlue);

        return procedures;
    }
    /**Method that takes procedures list and parallel list of which motions need to be reversed,
     * and returns mirrored procedures.*/
    double[][] mirrorProcedures(double[][] procedures, boolean[] mirrorThese){
        for (int i = 0; i < procedures.length; i++) {
            if (mirrorThese[i]) {
                /**note that negating both i and j will produce anti-parallel motion.*/
                /**I did not use a loop for negating these, since they just happen to be consecutive.*/
                procedures[i][0] *= -1.0; //i rotation
                procedures[i][1] *= -1.0; //j rotation
                procedures[i][2] *= -1.0; //"cw" rotation
            }
        }

        return procedures;
    }
}