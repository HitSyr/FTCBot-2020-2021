package org.firstinspires.ftc.teamcode.core;

import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase.MM_PER_INCH;
import static org.firstinspires.ftc.robotcore.external.navigation.VuforiaBase.MM_FTC_FIELD_WIDTH;

/**
 * Contains some necessary and useful measurements.
 *
 * The units of each measurement is made explicit through
 * the naming of the constant. For example, if a measurement
 * is given in millimetres, then the constant's name should
 * be suffixed with "_MM". Most measurements here are given
 * in millimetres in order to work seamlessly with Vuforia.
 */
public final class MeasurementConstants {

    private MeasurementConstants() {}

    public static final float HALF_FIELD_WIDTH_MM    = MM_FTC_FIELD_WIDTH / 2;
    public static final float QUARTER_FIELD_WIDTH_MM = MM_FTC_FIELD_WIDTH / 4;

    // The distance the launch line is from the front wall.
    public static final float LAUNCH_LINE_DISTANCE_MM = 80 * MM_PER_INCH;

    // The height of the center of the target image above the floor.
    public static final float TARGET_HEIGHT_MM = 6 * MM_PER_INCH;

    // From the REV website -> 28 encoder counts per motor revolution.
    public static final byte COUNTS_PER_MOTOR_REV = 28;

    // From the REV website -> REV Mechanum wheels are 75mm diameter.
    // This is much more useful when converted to inches.
    public static final float MECHANUM_WHEEL_DIAMETER_INCHES = 75 / MM_PER_INCH;

    // How many rotations the motor spins per rotation of the wheel.
    // Since we're not using direct drive, but, in our case, a 5:1
    // and 4:1 gearbox (20:1 total), then this needs to be 20.
    public static final byte DRIVE_GEAR_REDUCTION = 20;

    // The amount of encoder counts equal to translating 1 inch.
    public static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
                                                 (MECHANUM_WHEEL_DIAMETER_INCHES * Math.PI);

}
