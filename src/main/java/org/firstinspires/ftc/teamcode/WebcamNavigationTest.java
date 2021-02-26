package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.teamcode.core.Lawnmower;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase.MM_PER_INCH;

@Autonomous(name="Webcam Navigation Test", group="Testing")
public final class WebcamNavigationTest extends OpMode {

    private static Lawnmower lawnmower;

    @Override
    public final void init() {
        lawnmower = Lawnmower.init(hardwareMap, false, true);
        telemetry.addData("Status", "Initialized");
    }

    // TODO: Actually do something about the found trackables.
    @Override
    public final void loop() {
        telemetry.addData("Status", "Runtime: " + lawnmower.runtime.toString());

        OpenGLMatrix location = null;

        for (final VuforiaTrackable trackable : lawnmower.trackables) {
            VuforiaTrackableDefaultListener listener = (VuforiaTrackableDefaultListener) trackable.getListener();

            if (listener.isVisible()) {
                telemetry.addData("Visible Target", trackable.getName());
                location = listener.getUpdatedRobotLocation();
                break;
            }
        }

        if (location == null) {
            telemetry.addData("Position (in)", "Unknown");
            telemetry.addData("Rotation (deg)", "Unknown");
            return;
        }

        final VectorF translation = location.getTranslation();
        telemetry.addData(
                "Position (in)",
                "{X, Y, Z} = %.1f, %.1f, %.1f",
                translation.get(0) / MM_PER_INCH,
                translation.get(1) / MM_PER_INCH,
                translation.get(2) / MM_PER_INCH
        );

        final Orientation rotation = Orientation.getOrientation(location, EXTRINSIC, XYZ, DEGREES);
        telemetry.addData(
                "Rotation (deg)",
                "{Roll, Pitch, Heading} = %.0f, %.0f, %.0f",
                rotation.firstAngle,
                rotation.secondAngle,
                rotation.thirdAngle
        );
    }

    @Override
    public final void start() {
        lawnmower.runtime.reset();
    }

    @Override
    public final void stop() {
        lawnmower.shutdown();
    }

}
