package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
// import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

@TeleOp(name="Jack's Super Secret Test Code", group="Iterative Opmode")
//@Disabled
public class D1 extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;
    private DcMotor winchMotor;
    private DcMotor liftingArm;
    private DcMotor armMotor;
    private DcMotor spinMotor;
    private double spinnerPower = 0;
    private double servo6position = 0;
    private double servo5position = 0;
    private Servo servo5;
    private Servo servo6;
    double dampener = 1;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");

       /* Initialize the hardware variables. Note that the strings used here as parameters
        to 'get' must correspond to the names assigned during the robot configuration
        step (using the FTC Robot Controller app on the phone). */
        frontLeft  = hardwareMap.get(DcMotor.class, "front_left");
        frontRight = hardwareMap.get(DcMotor.class, "front_right");
        backLeft = hardwareMap.get(DcMotor.class, "back_left");
        backRight = hardwareMap.get(DcMotor.class, "back_right");
        winchMotor = hardwareMap.get(DcMotor.class, "winch_motor");
        liftingArm = hardwareMap.get(DcMotor.class,"lifter_motor");
        armMotor = hardwareMap.get(DcMotor.class, "arm_motor");
        spinMotor = hardwareMap.get(DcMotor.class, "spin_motor");
        servo5 = hardwareMap.get(Servo.class, "servo5");
        servo6 = hardwareMap.get(Servo.class, "servo6");


        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        frontLeft.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.FORWARD);
        winchMotor.setDirection(DcMotor.Direction.FORWARD);
        liftingArm.setDirection(DcMotor.Direction.FORWARD);
        armMotor.setDirection(DcMotor.Direction.FORWARD);
        spinMotor.setDirection(DcMotor.Direction.REVERSE);



        // Tell the driver that initialization is complete.
        telemetry.addData("Status", "Initialized");
    }
    /*
     * Code to run ONCE when the driver hits PLAY
     */


    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // Wheel motor power values. Right motor direction is reversed.

        double backleftWheelPower = ((gamepad1.left_stick_y + gamepad1.left_stick_x) - (gamepad1.right_stick_x))*dampener;
        double frontrightWheelPower = ((gamepad1.left_stick_y - gamepad1.left_stick_x) + (gamepad1.right_stick_x))* dampener;
        double frontleftWheelPower = ((gamepad1.left_stick_y - gamepad1.left_stick_x) - (gamepad1.right_stick_x))* dampener;
        double backrightWheelPower = ((gamepad1.left_stick_y + gamepad1.left_stick_x) + (gamepad1.right_stick_x))* dampener;
        double lifter;
        double dampener = (1- gamepad1.left_trigger);
        if(gamepad1.dpad_down) lifter = -1;
        else if (gamepad1.dpad_up) lifter = 1;
        else  lifter = 0;

        boolean spinnerButton = gamepad1.b;
        boolean reversespinnerButton = gamepad1.x;
        double armpower;
        if (gamepad1.right_bumper) armpower = 0.3;
        else if (gamepad1.left_bumper) armpower = -0.6;
        else armpower = 0;
        double winchpower = (gamepad1.right_trigger-gamepad1.left_trigger);
        double dampener2 = gamepad2.right_trigger;


        //Variable for the power of the arm motors. One motor's direction was reversed at the beginning of this class
        // double armPower = gamepad2.right_stick_y; //This will need to be tested to see if this has to be * -1

        //A value that motor power will be divided by to lower it.
        //The more the left trigger is pulled, the greater this value gets so the lower the motor power will be
        double powerReduction = (gamepad1.left_trigger*1.2) + 1;

        spinnerPower = 0.0;
        if(spinnerButton)
            spinnerPower = 0.2;
        else if(reversespinnerButton)
            spinnerPower = -1;

        // clip the input to a certain motors - they only take values [-1,1]
        frontleftWheelPower = Range.clip((frontleftWheelPower/powerReduction)+(gamepad1.right_trigger*.1), -1, 1);
        frontrightWheelPower = Range.clip((frontrightWheelPower/powerReduction)+(gamepad1.right_trigger*.1), -1, 1);
        //armPower = Range.clip((armPower * .25), -1, 1);

        // Send calculated power to all motors
        frontLeft.setPower(-frontleftWheelPower);
        frontRight.setPower(-frontrightWheelPower);
        backLeft.setPower(+backleftWheelPower);
        backRight.setPower(+backrightWheelPower);
        liftingArm.setPower(lifter);
        spinMotor.setPower(spinnerPower-dampener2*0.8);
        armMotor.setPower(armpower);
        winchMotor.setPower(winchpower);

       /*
       servo6.setPosition((gamepad2.right_stick_x * 160) - 90);
       servo5.setPosition((gamepad2.right_stick_x * -150) + 90);
       */

        if (gamepad1.a)
        {
            servo6.setPosition(0.4);
            servo5.setPosition(0.6);
        }
        else if (gamepad1.y)
        {


            servo6.setPosition(0.55);
            servo5.setPosition(0.45);

        }
        else
        {
            servo6.setPosition(0.04);
            servo5.setPosition(0.96);
        }


        // Show the elapsed game time, and other stats for nerds
        telemetry.addData("Status", "Run Time: " + runtime.toString());
        telemetry.addData("Motors", "left (%.2f), right (%.2f)", frontleftWheelPower, frontrightWheelPower);
        telemetry.addData("Arm Power", armpower);
        telemetry.addData("Spinner", spinnerPower);
        telemetry.addData("Servo5", servo5.getPosition());
        telemetry.addData("Servo6", servo6.getPosition());
        telemetry.addData("Dampener", dampener);


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() { }



/*
       servo1.setPosition(servo1Power);
       servo2.setPosition(servo2Power);
*/


}

