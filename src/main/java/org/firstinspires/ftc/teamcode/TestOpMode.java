/*
 * This is the code written in order to do some tests with the drivetrain.
 * This may or may not be rewritten and morphed to be used in the actual competition.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="DT/Intake Test", group="Testing")
public class TestOpMode extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    // Declare the motors.
    // NOTE: Have to declare them all as null because this will crash otherwise.
    private DcMotor frontLeft    = null;
    private DcMotor frontRight   = null;
    private DcMotor backLeft     = null;
    private DcMotor backRight    = null;
    private DcMotor frontIntake  = null;
    private DcMotor conveyorBelt = null;

    /*
     * Stops all motors on the bot.
     */
    public void stopAllMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        frontIntake.setPower(0);
        conveyorBelt.setPower(0);
    }

    /*
     * Runs ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");

        // Motor setup
        frontLeft    = hardwareMap.get(DcMotor.class, "front_left_motor");
        frontRight   = hardwareMap.get(DcMotor.class, "front_right_motor");
        backLeft     = hardwareMap.get(DcMotor.class, "back_left_motor");
        backRight    = hardwareMap.get(DcMotor.class, "back_right_motor");
        frontIntake  = hardwareMap.get(DcMotor.class, "front_intake_wheels");
        conveyorBelt = hardwareMap.get(DcMotor.class, "conveyor_belt");

        // TODO: Set the directions of all of the motors.

        // Ensure that none of our motors move during the initialization stage.
        stopAllMotors();

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Runs REPEATEDLY after the driver hits INIT, but before they hit PLAY.
     */
    @Override
    public void init_loop() {
    }

    /*
     * Runs ONCE when the driver hits PLAY.
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Runs REPEATEDLY after the driver hits PLAY but before they hit STOP.
     */
    @Override
    public void loop() {
        // FIXME: As of right now, this is set to basic tank drive for testing
        //  purposes, I do plan on replacing this with a better drive system.

        double leftPower  = -gamepad1.left_stick_y;
        double rightPower = -gamepad1.right_stick_y;

        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);

//        // WIP: Proper mechanum driving.
//        // FIXME: Counteract imperfect strafing.
//        //  This can be done via `leftHorizPos * a`.
//        //  Also, optionally, we could try to preserve the ratios needed.
//        double leftVertPos   = -gamepad1.left_stick_y;
//        double leftHorizPos  = gamepad1.left_stick_x;
//        double rightHorizPos = gamepad1.right_stick_x;
//
//        frontLeft.setPower(leftVertPos + leftHorizPos + rightHorizPos);
//        backLeft.setPower(leftVertPos - leftHorizPos + rightHorizPos);
//        frontRight.setPower(leftVertPos - leftHorizPos - rightHorizPos);
//        backRight.setPower(leftVertPos + leftHorizPos - rightHorizPos);

        // FIXME: Remap these to something that makes more sense.
        //  Again, this is here for testing purposes only.
        if (gamepad1.x)
            frontIntake.setPower(0.5);

        if (gamepad1.y)
            conveyorBelt.setPower(0.5);

        telemetry.addData("Status", "Runtime: " + runtime.toString());
    }

    /*
     * Runs ONCE after the driver hits STOP.
     */
    @Override
    public void stop() {
        // Ensure the motors actually stop when the stop button is pressed.
        stopAllMotors();

        telemetry.addData("Status", "Stopped");
    }

}
