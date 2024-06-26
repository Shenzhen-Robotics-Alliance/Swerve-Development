package frc.robot.HardwareIO.Helpers;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.Helpers.ArrayHelpers;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedGyro implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String sensorPath;
    private boolean isEncoderThreaded;
    private final ThreadedEncoder threadedEncoder;
    private final ThreadedEncoder.ThreadedEncoderInputs inputs;

    private Rotation2d[] robotFacings = new Rotation2d[0];
    private double rawReadingAtZeroPosition;

    public LoggedGyro(String name) {
        this(name, new ThreadedEncoder(null, new RawEncoder() {}));
    }

    public LoggedGyro(String name, RawEncoder rawEncoder) {
        this(name, new ThreadedEncoder(null, rawEncoder));
        this.isEncoderThreaded = false;
    }

    public LoggedGyro(String name, ThreadedEncoder threadedEncoder) {
        this.sensorPath = "RelativePositionEncoders/" + name;
        this.threadedEncoder = threadedEncoder;
        this.inputs = new ThreadedEncoder.ThreadedEncoderInputs();
        this.isEncoderThreaded = true;

        this.rawReadingAtZeroPosition = 0;
    }


    @Override
    public void update() {
        if (!isEncoderThreaded) this.threadedEncoder.pollReadingsFromEncoder();
        this.threadedEncoder.processCachedInputs(inputs);
        processCachedInputs();

        Logger.processInputs("RawInputs/" + sensorPath, inputs);
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/rawReadingAtZeroPosition", rawReadingAtZeroPosition);
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/getAngularVelocity", getAngularVelocity());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/latestRobotRotation2d", getLatestRobotRotation2d());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/robotFacings", getRobotFacings());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/timeStamps", getTimeStamps());
    }

    private void processCachedInputs() {
        this.robotFacings = new Rotation2d[inputs.uncalibratedEncoderPosition.size()];

        for (int i = 0; i < inputs.uncalibratedEncoderPosition.size(); i++)
            robotFacings[i] = getRobotRotation2d(inputs.uncalibratedEncoderPosition.get(i));
    }

    public Rotation2d[] getRobotFacings() {
        return robotFacings;
    }

    public double[] getTimeStamps() {
        return ArrayHelpers.toDoubleArray(inputs.timeStamps);
    }

    public Rotation2d getLatestRobotRotation2d() {
        return Rotation2d.fromRadians(AngleHelpers.getActualDifference(rawReadingAtZeroPosition, inputs.latestUncalibratedPosition));
    }

    public Rotation2d getRobotRotation2d(double uncalibratedEncoderPosition) {
        return Rotation2d.fromRadians(AngleHelpers.getActualDifference(rawReadingAtZeroPosition, uncalibratedEncoderPosition));
    }

    public double getAngularVelocity() {
        return inputs.encoderVelocity;
    }

    public void setCurrentRotation2d(Rotation2d currentRotation2d) {
        // the robot is at currentRotation2d.getRadians() when the raw reading of the sensor is inputs.getUncalibratedEncoderPosition()
        // how much should the raw reading be when it is at zero position?
        this.rawReadingAtZeroPosition = AngleHelpers.simplifyAngle(
                inputs.latestUncalibratedPosition + AngleHelpers.getActualDifference(currentRotation2d.getRadians(), 0)
        );
    }
}
