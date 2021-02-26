package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Singular Motor Test", group="Testing")
public final class SingularMotorTest extends OpMode {

    private static final ElapsedTime runtime = new ElapsedTime();
    private static DcMotor testMotor;

    @Override
    public final void init() {
        testMotor = hardwareMap.get(DcMotor.class, "Test Motor");
        testMotor.setPower(0);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public final void start() {
        runtime.reset();
    }

    @Override
    public final void loop() {
        telemetry.addData("Status", "Runtime: " + runtime.toString());

        if (gamepad1.x)
            testMotor.setPower(1);
        else
            testMotor.setPower(0);

        if (gamepad1.b)
            testMotor.setPower(-1);
        else
            testMotor.setPower(0);
    }

}
