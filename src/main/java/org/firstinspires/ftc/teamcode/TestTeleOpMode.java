/*
 * This is the code written in order to do some tests with the drivetrain.
 * This may or may not be rewritten and morphed to be used in the actual competition.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOp Test", group="Testing")
public class TestTeleOpMode extends OpMode {

    private static final ElapsedTime runtime = new ElapsedTime();

    private DcMotor frontLeft     = null;
    private DcMotor frontRight    = null;
    private DcMotor backLeft      = null;
    private DcMotor backRight     = null;
    private DcMotor intake        = null;
    private DcMotor revolvingDoor = null;

    @Override
    public void init() {
        telemetry.addData("Status", "Initializing...");

        frontLeft     = hardwareMap.get(DcMotor.class, "frontLeft");
        frontRight    = hardwareMap.get(DcMotor.class, "frontRight");
        backLeft      = hardwareMap.get(DcMotor.class, "backLeft");
        backRight     = hardwareMap.get(DcMotor.class, "backRight");
        intake        = hardwareMap.get(DcMotor.class, "intake");
        revolvingDoor = hardwareMap.get(DcMotor.class, "revolvingDoor");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backRight.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        revolvingDoor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Ensure that no motors move during the initialization stage.
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        intake.setPower(0);
        revolvingDoor.setPower(0);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void start() {
        runtime.reset();
    }

    @Override
    public void loop() {
        // WIP: Proper mechanum driving.
        // FIXME: Counteract imperfect strafing.
        //  This can be done via `leftHorizPos * a`.
        //  Also, optionally, we could try to preserve the ratios needed.
        final double leftVertPos   = -gamepad1.left_stick_y;
        final double leftHorizPos  = gamepad1.left_stick_x;
        final double rightHorizPos = gamepad1.right_stick_x;

        frontLeft.setPower(leftVertPos + leftHorizPos + rightHorizPos);
        backLeft.setPower(leftVertPos - leftHorizPos + rightHorizPos);
        frontRight.setPower(leftVertPos - leftHorizPos - rightHorizPos);
        backRight.setPower(leftVertPos + leftHorizPos - rightHorizPos);

        // FIXME: Remap these to something that makes more sense.
        //  Again, this is here for testing purposes only.
        if (gamepad2.x) {
            intake.setPower(1);
            revolvingDoor.setPower(0.5);
        } else {
            intake.setPower(0);
            revolvingDoor.setPower(0);
        }

        if (gamepad2.b)
            intake.setPower(-1);
        else
            intake.setPower(0);

        telemetry.addData("Status", "Runtime: " + runtime.toString());
        telemetry.addData("Power", "Left (%.2f) | Right (%.2f)", leftPower, rightPower);
    }

}
