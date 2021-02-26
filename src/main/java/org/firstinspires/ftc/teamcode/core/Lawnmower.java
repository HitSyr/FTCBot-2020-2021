package org.firstinspires.ftc.teamcode.core;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaCurrentGame;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TfodCurrentGame;

import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.YZX;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase.MM_PER_INCH;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.CameraDirection.BACK;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer.Parameters.CameraMonitorFeedback.AXES;
import static org.firstinspires.ftc.teamcode.core.MeasurementConstants.HALF_FIELD_WIDTH_MM;
import static org.firstinspires.ftc.teamcode.core.MeasurementConstants.QUARTER_FIELD_WIDTH_MM;
import static org.firstinspires.ftc.teamcode.core.MeasurementConstants.TARGET_HEIGHT_MM;

/**
 * The hardware definitions class used for the competition
 * robot, a.k.a. "The Lawnmower."
 *
 * Users should not construct instances of this class, use
 * {@link #init(HardwareMap, boolean)} instead.
 *
 * This hardware class assumes the following device names
 * have been configured on the robot:
 *
 * Motor channel:  Front left drive motor:     "Front Left"
 * Motor channel:  Back left drive motor:      "Back Left"
 * Motor channel:  Front right drive motor:    "Front Right"
 * Motor channel:  Back right drive motor:     "Back Right"
 * Motor channel:  Ring intake motor:          "Intake"
 * Motor channel:  Ring revolving door:        "Revolving Door"
 * Webcam:         Logitech C270:              "Webcam 1"
 */
public final class Lawnmower {

    public final ElapsedTime runtime        = new ElapsedTime();
    public final VuforiaCurrentGame vuforia = new VuforiaCurrentGame();
    public final TfodCurrentGame tfod       = new TfodCurrentGame();

    public VuforiaTrackables trackables;
    public final HardwareMap hardwareMap;

    public final DcMotor frontLeft;
    public final DcMotor frontRight;
    public final DcMotor backLeft;
    public final DcMotor backRight;
    public final DcMotor intake;
    public final DcMotor revolvingDoor;

    private final boolean usingCameras;

    private Lawnmower(final HardwareMap hardwareMap, final boolean useEncoders, final boolean enableCameras) {
        this.hardwareMap    = hardwareMap;
        usingCameras        = enableCameras;

        frontLeft     = hardwareMap.get(DcMotor.class, "Front Left");
        frontRight    = hardwareMap.get(DcMotor.class, "Front Right");
        backLeft      = hardwareMap.get(DcMotor.class, "Back Left");
        backRight     = hardwareMap.get(DcMotor.class, "Back Right");
        intake        = hardwareMap.get(DcMotor.class, "Intake");
        revolvingDoor = hardwareMap.get(DcMotor.class, "Revolving Door");

        frontRight.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        revolvingDoor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Ensure that no motors move during the initialization stage.
        stopAllMotors();

        if (useEncoders) {
            // Ensure the encoders are set to 0 before use.
            resetDrivetrainEncoders();
        }

        if (!enableCameras)
            return;

        vuforia.initialize(
                "--- VUFORIA KEY REMOVED IN REBASE; PUT YOUR OWN VUFORIA KEY HERE ---",
                hardwareMap.get(WebcamName.class, "Webcam 1"),
                "teamwebcamcalibrations",
                false,
                false,
                AXES,
                0,
                0,
                0,
                0,
                0,
                0,
                true
        );
        tfod.initialize(vuforia, 0.75F, true, false);

        trackables = vuforia.getVuforiaLocalizer().loadTrackablesFromAsset("UltimateGoal");

        final VuforiaTrackable blueTowerTarget    = trackables.get(0);
        final VuforiaTrackable redTowerTarget     = trackables.get(1);
        final VuforiaTrackable redAllianceTarget  = trackables.get(2);
        final VuforiaTrackable blueAllianceTarget = trackables.get(3);
        final VuforiaTrackable frontWallTarget    = trackables.get(4);

        blueTowerTarget.setName("Blue Tower Target");
        redTowerTarget.setName("Red Tower Target");
        redAllianceTarget.setName("Red Alliance Target");
        blueAllianceTarget.setName("Blue Alliance Target");
        frontWallTarget.setName("Front Wall Target");

        // Set the position of the perimeter targets with relation
        // to origin (center of field)
        redAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, -HALF_FIELD_WIDTH_MM, TARGET_HEIGHT_MM)
                .multiplied(Orientation.getRotationMatrix(
                        EXTRINSIC,
                        XYZ,
                        DEGREES,
                        90,
                        0,
                        180
                        )
                ));
        blueAllianceTarget.setLocation(OpenGLMatrix
                .translation(0, HALF_FIELD_WIDTH_MM, TARGET_HEIGHT_MM)
                .multiplied(Orientation.getRotationMatrix(
                        EXTRINSIC,
                        XYZ,
                        DEGREES,
                        90,
                        0,
                        0
                        )
                ));

        frontWallTarget.setLocation(OpenGLMatrix
                .translation(-HALF_FIELD_WIDTH_MM, 0, TARGET_HEIGHT_MM)
                .multiplied(Orientation.getRotationMatrix(
                        EXTRINSIC,
                        XYZ,
                        DEGREES,
                        90,
                        0,
                        90
                        )
                ));

        // The tower goal targets are located a quarter field length
        // from the ends of the back perimeter wall.
        blueTowerTarget.setLocation(OpenGLMatrix
                .translation(HALF_FIELD_WIDTH_MM, QUARTER_FIELD_WIDTH_MM, TARGET_HEIGHT_MM)
                .multiplied(Orientation.getRotationMatrix(
                        EXTRINSIC,
                        XYZ,
                        DEGREES,
                        90,
                        0,
                        -90
                        )
                ));
        redTowerTarget.setLocation(OpenGLMatrix
                .translation(HALF_FIELD_WIDTH_MM, QUARTER_FIELD_WIDTH_MM, TARGET_HEIGHT_MM)
                .multiplied(Orientation.getRotationMatrix(
                        EXTRINSIC,
                        XYZ,
                        DEGREES,
                        90,
                        0,
                        -90
                        )
                ));

        OpenGLMatrix cameraLocation = OpenGLMatrix
                .translation(4 * MM_PER_INCH, 0, 8 * MM_PER_INCH)
                .multiplied(Orientation.getRotationMatrix(
                        EXTRINSIC,
                        YZX,
                        DEGREES,
                        -90, // Phone Y Rotation
                        0,   // Phone Z Rotation
                        90)  // Phone X Rotation
                );

        for (final VuforiaTrackable trackable : trackables) {
            ((VuforiaTrackableDefaultListener) trackable.getListener()).setPhoneInformation(cameraLocation, BACK);
        }

        // Activate this stuff here so it's visible in the init preview.
        tfod.activate();
        trackables.activate();
    }

    /**
     * Initializes the lawnmower's hardware interfaces.
     * @param hardwareMap The OpMode's hardware map.
     * @param runAutonomously Whether or not to initialize the hardware related to autonomous.
     * @return A {@link Lawnmower} with the necessary components initialized.
     */
    public static Lawnmower init(final HardwareMap hardwareMap, final boolean useEncoders, final boolean enableCameras) {
        return new Lawnmower(hardwareMap, useEncoders, enableCameras);
    }

    /**
     * Whether or not the drivetrain motors are currently advancing or
     * retreating to a target position.
     * @return true if and only if ALL drivetrain motors are currently
     * advancing or retreating to a target position.
     */
    public final boolean isDrivetrainBusy() {
        return frontLeft.isBusy() && frontRight.isBusy() && backLeft.isBusy() && backRight.isBusy();
    }

    /**
     * Shuts down the robot.
     */
    public final void shutdown() {
        stopAllMotors();

        if (!usingCameras)
            return;

        tfod.deactivate();
        trackables.deactivate();

        vuforia.close();
        tfod.close();
    }

    /**
     * Stops all motors on the robot.
     */
    public final void stopAllMotors() {
        frontLeft.setPower(0);
        frontRight.setPower(0);
        backLeft.setPower(0);
        backRight.setPower(0);
        intake.setPower(0);
        revolvingDoor.setPower(0);
    }


    /**
     * Resets all encoders on the drive train motors.
     */
    public final void resetDrivetrainEncoders() {
        frontLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backLeft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backRight.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backLeft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backRight.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

}
