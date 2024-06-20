package frc.robot.HardwareIOs.Helpers;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.HardwareIOs.Abstractions.RawEncoder;
import frc.robot.Helpers.MathHelpers.AngleHelpers;

public class LoggedGyro implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String name;
    /* the position and velocity feeder (not calibrated but already inverted as needed) */
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs inputs;
    private double rawReadingAtZeroPosition;

    public LoggedGyro(String name, RawEncoder rawEncoder) {
        this.name = name;
        this.rawEncoder = rawEncoder;
        this.inputs = new RawEncoder.RawEncoderInputs();

        this.rawReadingAtZeroPosition = 0;
    }

    @Override
    public void update() {
        this.rawEncoder.updateEncoderInputs(inputs);
    }

    public Rotation2d getRobotRotation2d() {
        return Rotation2d.fromRadians(AngleHelpers.getActualDifference(rawReadingAtZeroPosition, inputs.getUncalibratedEncoderPosition()));
    }

    public void setCurrentRotation2d(Rotation2d currentRotation2d) {
        // the robot is at currentRotation2d.getRadians() when the raw reading of the sensor is inputs.getUncalibratedEncoderPosition()
        // how much should the raw reading be when it is at zero position?
        this.rawReadingAtZeroPosition = AngleHelpers.simplifyAngle(
                inputs.getUncalibratedEncoderPosition() + AngleHelpers.getActualDifference(currentRotation2d.getRadians(), 0)
        );
    }
}