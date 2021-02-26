package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Launcher Test", group="Testing")
public final class LauncherTest extends OpMode {

    private static final ElapsedTime runtime = new ElapsedTime();
    private static DcMotor launcher;

    @Override
    public final void init() {
        launcher = hardwareMap.get(DcMotor.class, "Launcher");

        launcher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        launcher.setDirection(DcMotor.Direction.REVERSE);

        launcher.setPower(0);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public final void start() {
        runtime.reset();
    }

    @Override
    public final void loop() {
        telemetry.addData("Status", "Runtime: " + runtime.toString());
        launcher.setPower(gamepad1.right_trigger);
    }

}
