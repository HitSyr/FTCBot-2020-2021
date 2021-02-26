/*
 * This is the code written in order to do some tests with the drivetrain.
 * This may or may not be rewritten and morphed to be used in the actual competition.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.core.Lawnmower;

@TeleOp(name="TeleOp Test", group="Testing")
public final class TeleOpTest extends OpMode {

    private static Lawnmower lawnmower;

    @Override
    public final void init() {
        lawnmower = Lawnmower.init(hardwareMap, false, false);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public final void start() {
        lawnmower.runtime.reset();
    }

    @Override
    public final void loop() {
        telemetry.addData("Status", "Runtime: " + lawnmower.runtime.toString());

        // WIP: Proper mechanum driving.
        // FIXME: Counteract imperfect strafing.
        //  This can be done via `leftHorizPos * a`.
        //  Also, optionally, we could try to preserve the ratios needed.
        final double leftVertPos   = -gamepad1.left_stick_y;
        final double leftHorizPos  = gamepad1.left_stick_x;
        final double rightHorizPos = gamepad1.right_stick_x;

        lawnmower.frontLeft.setPower(leftVertPos + leftHorizPos + rightHorizPos);
        lawnmower.backLeft.setPower(leftVertPos - leftHorizPos + rightHorizPos);
        lawnmower.frontRight.setPower(leftVertPos - leftHorizPos - rightHorizPos);
        lawnmower.backRight.setPower(leftVertPos + leftHorizPos - rightHorizPos);

        // FIXME: Remap these to something that makes more sense.
        //  Again, this is here for testing purposes only.
        if (gamepad2.x) {
            lawnmower.intake.setPower(1);
            lawnmower.revolvingDoor.setPower(0.25);
        } else {
            lawnmower.intake.setPower(0);
            lawnmower.revolvingDoor.setPower(0);
        }

        if (gamepad2.b)
            lawnmower.intake.setPower(-1);
        else
            lawnmower.intake.setPower(0);
    }

}
