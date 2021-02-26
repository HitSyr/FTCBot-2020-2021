package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.core.Lawnmower;

import static org.firstinspires.ftc.teamcode.core.MeasurementConstants.COUNTS_PER_INCH;

@Autonomous(name="Encoder Autonomous Test", group="Testing")
public class EncoderAutonomousTest extends LinearOpMode {

    private static Lawnmower lawnmower;

    public final void runOpMode() {
        // While this technically is an autonomous OpMode, this doesn't
        // use the cameras or anything so there's no need to initialize
        // that hardware and potentially waste a ton of battery.
        lawnmower = Lawnmower.init(hardwareMap, true, false);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        lawnmower.runtime.reset();

        driveWithEncoders(1, 6, 50, 50);

        // Ensure lawnmower is shut down.
        lawnmower.shutdown();

        requestOpModeStop();
    }

    private void driveWithEncoders(
            final double speed,
            final double timeInSeconds,
            final double leftInches,
            final double rightInches
    ) {
        if (!opModeIsActive())
            return;

        final int leftTarget  = lawnmower.frontLeft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
        final int rightTarget = lawnmower.frontRight.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

        lawnmower.frontLeft.setTargetPosition(leftTarget);
        lawnmower.frontRight.setTargetPosition(rightTarget);
        lawnmower.backLeft.setTargetPosition(leftTarget);
        lawnmower.backRight.setTargetPosition(rightTarget);

        lawnmower.frontLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lawnmower.frontRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lawnmower.backLeft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        lawnmower.backRight.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        lawnmower.runtime.reset();

        lawnmower.frontLeft.setPower(Math.abs(speed));
        lawnmower.frontRight.setPower(Math.abs(speed));
        lawnmower.backLeft.setPower(Math.abs(speed));
        lawnmower.backRight.setPower(Math.abs(speed));

        while (opModeIsActive() && lawnmower.runtime.seconds() < timeInSeconds && lawnmower.isDrivetrainBusy()) {
            // Display it for the driver.
            telemetry.addData("Path1", "Running to %7d :%7d", leftTarget, rightTarget);
            telemetry.addData(
                    "Path2",
                    "Running at %7d :%7d",
                    lawnmower.frontLeft.getCurrentPosition(),
                    lawnmower.frontRight.getCurrentPosition()
            );
            telemetry.update();
        }

        lawnmower.frontLeft.setPower(0);
        lawnmower.frontRight.setPower(0);
        lawnmower.backLeft.setPower(0);
        lawnmower.backRight.setPower(0);

        lawnmower.resetDrivetrainEncoders();
    }

}
