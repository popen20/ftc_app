package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

/** Move_Engine is a super class we use to hold all of our movement-based methods
 *
 **/
public abstract class Move_Engine extends LinearOpMode {

    public static final String TFOD_MODEL_ASSET = "RoverRuckus.tflite";
    public static final String LABEL_GOLD_MINERAL = "Gold Mineral";
    public static final String LABEL_SILVER_MINERAL = "Silver Mineral";
    public DcMotor frontLeft;
    public DcMotor frontRight;
    public DcMotor backLeft;
    public DcMotor backRight;
    public DcMotor winchMotor;
    public DcMotor liftingArm;
    public DcMotor armMotor;
    public DcMotor spinMotor;
    public Servo servo5;
    public Servo servo6;
    public Servo servo4;
    public static final String VUFORIA_KEY = " AQ116pH/////AAABmYKZkP6ruU4ukxsm+1osPgFEdpQlf84kCDF5xhQQR5sUCugeVkoDJNSxZMmVG4iI/ZgvMc93IktiBsRMg2r0bdh1o/t5tOkiiSl+mdsccx1SES40H4FEoIf/D1n5qhQLpT+W656H1Ffe16Hbb86/FC2mJDAes22Ddq7fEeGSTzvFXAjrMnCwZSK90opojAxPitQxTFgjaXz+ZYPfcn7ciJ8DReEBuhcqdiVs54N0Szf0b0fdO5Wo201+rWhI9UVjOli7shY3vQyokxnhjPzVROsYmuKe05llHFPNpuVkWJiEPTBSwPqTQ40mwdX0RV5ortDPcG62bYx3v8hZY/dUVMWjFzdPr+sHJK1Cp2KAbbXj";
    public VuforiaLocalizer vuforia;
    public TFObjectDetector tfod;

    public Move_Engine(){}

    public void setConfig(){
        frontLeft = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");
        winchMotor = hardwareMap.get(DcMotor.class, "winch_motor");
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        liftingArm = hardwareMap.get(DcMotor.class, "lifter_motor");
        spinMotor = hardwareMap.get(DcMotor.class, "spin_motor");
        servo5 = hardwareMap.get(Servo.class, "servo5");
        servo6 = hardwareMap.get(Servo.class, "servo6");
        servo4 = hardwareMap.get(Servo.class, "servo4");
        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.REVERSE);
        winchMotor.setDirection(DcMotor.Direction.FORWARD);
        liftingArm.setDirection(DcMotor.Direction.FORWARD);
        spinMotor.setDirection(DcMotor.Direction.REVERSE);
    }
    public void Standby(){
        while (!isStarted()) {
            telemetry.addData("Garamond:", " Waiting for Start.");
            telemetry.update();
            sleep(1000);
            telemetry.clear();
            telemetry.addData("Garamond:", " Waiting for Start..");
            telemetry.update();
            sleep(1000);
            telemetry.clear();
            telemetry.addData("Garamond:", " Waiting for Start...");
            telemetry.update();
            sleep(1000);
            telemetry.clear();
        }
    }
    /** dontMove is an overloaded method which stop all drive motors
     *
     * @param time a pause (in milliseconds) before the next movement
     */
    public void dontMove(int time){
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        sleep(time);
    }
    /** dontMove is an overloaded method which stop all drive motors
     *
     */
    public void dontMove(){
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
    }

    /** One of two strafe methods, this one strafes left
     *
     * @param time The time (in milliseconds) the motors runs for.
     * @param pow The percent power (as a decimal) the drive motors run at.
     */
    public void strafeLeft(int time, double pow){
        frontLeft.setPower(-1 * pow);
        backLeft.setPower(1 * pow);
        frontRight.setPower(-1 * pow);
        backRight.setPower(1 * pow);
        sleep(time);
    }

    /** One of two strafe methods, this one strafes right
     *
     * @param time The time (in milliseconds) the motors runs for.
     * @param pow The percent power (as a decimal) the drive motors run at.
     */
    public void strafeRight(int time, double pow){
        frontLeft.setPower(1 * pow);
        backLeft.setPower(-1 * pow);
        frontRight.setPower(1 * pow);
        backRight.setPower(-1 * pow);
        sleep(time);
    }

    /** The Forward method drives Garamond...forward.
     * @param time The time in milliseconds to drive for.
     * @param pow The percent power (as a decimal) the drive motors run at.
     */
    public void Forward(int time,double pow){
        frontLeft.setPower(1 * pow);
        backLeft.setPower(1 * pow);
        frontRight.setPower(1 * pow);
        backRight.setPower(1 * pow);
        sleep(time);
    }
    /** The Backward method drives Garamond backwards (surprise surprise..)
     * @param time The time in milliseconds to drive for.
     * @param pow The percent power (as a decimal) the drive motors run at.
     */
    public void Backward(int time,double pow){
        frontLeft.setPower(-1 * pow);
        backLeft.setPower(-1 * pow);
        frontRight.setPower(-1 * pow);
        backRight.setPower(-1 * pow);
        sleep(time);
    }
    /** rightAngleTurn is a method that turns right or left.
     * @param direction  Turns Garamond Left or Right based on the String given ("L" or "R").
     */
    public void rightAngleTurn(String direction)
    {
        if(direction.startsWith("R")){
            frontLeft.setPower(-1);
            backLeft.setPower(1);
            frontRight.setPower(1);
            backRight.setPower(-1);
            sleep(450);
        } else if(direction.startsWith("L")){
            frontLeft.setPower(-1);
            backLeft.setPower(-1);
            frontRight.setPower(1);
            backRight.setPower(1);
            sleep(600);
        } else telemetry.addLine("lmao who wrote this it doesn't work");
    }

    /** The moveArm Method controls the movement of Garamond's collector arm.
     * @param pow The power to set the motor to.
     * @param sec The time (in milliseconds) the motor runs for.
     */
    public void moveArm(double pow, int sec){
        armMotor.setPower(pow * 0.5);
        sleep(sec);
        armMotor.setPower(0);
        dontMove();
    }
    /**
     * Method to Lower Garamond from the Lander.
     */
    public void Land() {

        liftingArm.setPower(-1);
        frontLeft.setPower(-0.2);
        backLeft.setPower(0.2);
        frontRight.setPower(-0.2);
        backRight.setPower(0.2);
        sleep(3500);
        liftingArm.setPower(0);
        frontLeft.setPower(0);
        backLeft.setPower(0);
        frontRight.setPower(0);
        backRight.setPower(0);
        Backward(600, 0.3);
        strafeLeft(1500,0.5);
        rightAngleTurn("L");


    }
    public void TurnAround()
    {

        frontLeft.setPower(-1);
        backLeft.setPower(-1);
        frontRight.setPower(1);
        backRight.setPower(1);
        sleep(1650);

    }

    public void spinSpinner(int time, double pow){
        spinMotor.setPower(pow);
        sleep(time);
        spinMotor.setPower(0);
    }

    public void Sample(){
        boolean found = false;
        int pos = 0;
        while (opModeIsActive()&& !found) {
            if (tfod != null) {
                // getUpdatedRecognitions() will return null if no new information is available since
                // the last time that call was made.
                List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                if (updatedRecognitions != null) {
                    telemetry.addData("# Object Detected", updatedRecognitions.size());
                    if (updatedRecognitions.size() == 2)
                    {
                        int goldMineralX = -1;
                        int silverMineral1X = -1;
                        int silverMineral2X = -1;
                        for (Recognition recognition : updatedRecognitions)
                        {
                            if (recognition.getLabel().equals(LABEL_GOLD_MINERAL))
                            {
                                goldMineralX = (int) recognition.getLeft();
                            }
                            else if (silverMineral1X == -1)
                            {
                                silverMineral1X = (int) recognition.getLeft();
                            }
                            else
                            {
                                silverMineral2X = (int) recognition.getLeft();
                            }
                        }

                        if (goldMineralX == -1)
                        {
                            telemetry.addData("Gold Mineral Position", "Right");
                            found = true;
                            pos = 3;
                        }
                        else if (goldMineralX > silverMineral1X )
                        {
                            telemetry.addData("Gold Mineral Position", "Center");
                            found = true;
                            pos = 2;
                        }
                        else
                        {
                            telemetry.addData("Gold Mineral Position", "Left");
                            found = true;
                            pos = 1;
                        }
                    }
                    telemetry.update();
                }
            }
        }
        if (pos ==3) //Right
        {
            servo4.setPosition(0.5);
            Land();
            strafeRight(1500, 1);
            Forward(800, 0.5);
            Backward(500,0.5);
        }
        if (pos ==2) //Center
        {
            Land();
            strafeRight(200, 1);
            Forward(800, 0.5);
            Backward(500,0.5);
        }
        if (pos ==1) //Left
        {
            Land();
            strafeLeft(900, 1);
            Forward(800, 0.5);
            Backward(500,0.5);
        }
    }


    /**
     * Initialize the Vuforia localization engine.
     */
    public void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = CameraDirection.BACK;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Loading trackables is not necessary for the Tensor Flow Object Detection engine.
    }

    /**
     * Initialize the Tensor Flow Object Detection engine.
     */
    public void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_GOLD_MINERAL, LABEL_SILVER_MINERAL);
    }
}

