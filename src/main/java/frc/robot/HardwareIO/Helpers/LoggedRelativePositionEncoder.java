package frc.robot.HardwareIO.Helpers;

import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.HardwareIO.Abstractions.TimeStampedEncoder;
import frc.robot.Helpers.ArrayHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedRelativePositionEncoder implements PrePeriodicUpdatedInputs.PrePeriodicUpdateInput {
    private final String sensorPath;
    private boolean isEncoderThreaded;
    private final TimeStampedEncoder timeStampedEncoder;
    private final TimeStampedEncoder.TimeStampedEncoderInputs inputs;

    private double[] relativePositions = new double[] {};
    private double zeroPosition;
    public LoggedRelativePositionEncoder(String name) {
        this(name, new TimeStampedEncoderReplay());
    }

    public LoggedRelativePositionEncoder(String name, RawEncoder rawEncoder) {
        this(name, new TimeStampedEncoderReal(null, rawEncoder));
        this.isEncoderThreaded = false;
    }

    public LoggedRelativePositionEncoder(String name, TimeStampedEncoder timeStampedEncoder) {
        this.sensorPath = "RelativePositionEncoder/" + name;
        this.timeStampedEncoder = timeStampedEncoder;
        this.inputs = new TimeStampedEncoder.TimeStampedEncoderInputs();
        this.zeroPosition = 0;
        this.isEncoderThreaded = true;

        PrePeriodicUpdatedInputs.register(name, this);
    }

    @Override
    public void update() {
        if (!isEncoderThreaded) timeStampedEncoder.pollPositionReadingToCache();

        this.timeStampedEncoder.processInputsUsingCachedReadings(inputs);
        Logger.processInputs(Constants.LogConfigs.SENSORS_INPUTS_PATH + sensorPath, inputs);

        processCachedInputs();

        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/zeroPosition", zeroPosition);
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/velocity", getVelocity());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/latestPosition", getLatestPosition());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/positions", getPositions());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/timeStamps", getTimeStamps());
    }

    private void processCachedInputs() {
        this.relativePositions = new double[inputs.uncalibratedEncoderPosition.size()];

        for (int i = 0; i < inputs.uncalibratedEncoderPosition.size(); i++)
            relativePositions[i] = getCurrentPosition(inputs.uncalibratedEncoderPosition.get(i));
    }

    public void setCurrentPositionAs(double desiredReading) {
        this.zeroPosition = inputs.latestUncalibratedPosition - desiredReading;
    }

    public double[] getPositions() {
        return relativePositions;
    }

    public double[] getTimeStamps() {
        return ArrayHelpers.toDoubleArray(inputs.timeStamps);
    }

    public double getLatestPosition() {
        return getCurrentPosition(inputs.latestUncalibratedPosition);
    }
    private double getCurrentPosition(double uncalibratedEncoderPosition) {
        return uncalibratedEncoderPosition - zeroPosition;
    }

    public double getVelocity() {
        return inputs.encoderVelocity;
    }
}
