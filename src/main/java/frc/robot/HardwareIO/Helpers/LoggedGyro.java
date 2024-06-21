package frc.robot.HardwareIO.Helpers;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedGyro implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String sensorPath;
    /* the position and velocity feeder (not calibrated but already inverted as needed) */
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs inputs;
    private double rawReadingAtZeroPosition;

    public LoggedGyro(String name, RawEncoder rawEncoder) {
        this.sensorPath = "RelativePositionEncoders/" + name;
        this.rawEncoder = rawEncoder;
        this.inputs = new RawEncoder.RawEncoderInputs();

        this.rawReadingAtZeroPosition = 0;
    }

    @Override
    public void update() {
        this.rawEncoder.updateEncoderInputs(inputs);
        Logger.processInputs("RawInputs/" + sensorPath, inputs);
        Logger.recordOutput("ProcessedInputs/" + sensorPath + "/absoluteRotationRadian", getRobotRotation2d());
    }

    public Rotation2d getRobotRotation2d() {
        return Rotation2d.fromRadians(AngleHelpers.getActualDifference(rawReadingAtZeroPosition, inputs.uncalibratedEncoderPosition));
    }

    public double getAngularVelocity() {
        return inputs.encoderVelocity;
    }

    public void setCurrentRotation2d(Rotation2d currentRotation2d) {
        // the robot is at currentRotation2d.getRadians() when the raw reading of the sensor is inputs.getUncalibratedEncoderPosition()
        // how much should the raw reading be when it is at zero position?
        this.rawReadingAtZeroPosition = AngleHelpers.simplifyAngle(
                inputs.uncalibratedEncoderPosition + AngleHelpers.getActualDifference(currentRotation2d.getRadians(), 0)
        );
    }
}
