/*
 * This is the code written in order to do some tests in autonomous mode.
 * This may or may not be rewritten and morphed to be used in the actual competition.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.core.Lawnmower;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase.MM_PER_INCH;

@Autonomous(name="Autonomous Test", group="Testing")
public final class AutonomousTest extends OpMode {

    private static Lawnmower lawnmower;

    @Override
    public final void init() {
        lawnmower = Lawnmower.init(hardwareMap, true, true);
        telemetry.addData("Status", "Initialized");
    }

    @Override
    public final void start() {
        lawnmower.runtime.reset();
    }

    @Override
    public final void loop() {
        telemetry.addData("Status", "Runtime: " + lawnmower.runtime.toString());
        telemetry.addData("Visible Target", "No visible target.");

        // TODO: Actually do something about the ring stack detection.
        //  For reference, the configurations are 0-A, 1-B, 4-C.
        final List<Recognition> recognitions = lawnmower.tfod.getRecognitions();
        final int recognitionsSize = recognitions.size();

        telemetry.addData("Recognitions", recognitionsSize);

        // TODO: Actually do something about the found trackables.
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

        for (int i = 0; i < recognitionsSize; i++) {
            final Recognition recognition = recognitions.get(i);
            // Display the label and index number for the recognition.
            telemetry.addData("LABEL " + i, recognition.getLabel());
            // Display the location of the top left corner
            // of the detection boundary for the recognition
            telemetry.addData(
                    "Left, Top " + i,
                    recognition.getLeft() + ", " + recognition.getTop()
            );
            // Display the location of the bottom right corner
            // of the detection boundary for the recognition
            telemetry.addData(
                    "Right, Bottom " + i,
                    recognition.getRight() + ", " + recognition.getBottom()
            );
        }
    }

    @Override
    public final void stop() {
        lawnmower.shutdown();
    }

}
