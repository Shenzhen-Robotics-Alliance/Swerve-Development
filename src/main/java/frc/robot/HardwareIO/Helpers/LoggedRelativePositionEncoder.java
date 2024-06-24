package frc.robot.HardwareIO.Helpers;

import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.Helpers.ArrayHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedRelativePositionEncoder implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String sensorPath;
    private boolean isEncoderThreaded;
    private final ThreadedEncoder threadedEncoder;
    private final ThreadedEncoder.ThreadedEncoderInputs inputs;

    private double[] relativePositions = new double[] {};
    private double zeroPosition;
    public LoggedRelativePositionEncoder(String name) {
        this(name, new ThreadedEncoder(new RawEncoder() {}));
    }

    public LoggedRelativePositionEncoder(String name, RawEncoder rawEncoder) {
        this(name, new ThreadedEncoder(rawEncoder));
        this.isEncoderThreaded = false;
    }

    public LoggedRelativePositionEncoder(String name, ThreadedEncoder threadedEncoder) {
        this.sensorPath = "RelativePositionEncoder/" + name;
        this.threadedEncoder = threadedEncoder;
        this.inputs = new ThreadedEncoder.ThreadedEncoderInputs();
        this.zeroPosition = 0;
        this.isEncoderThreaded = true;

        PeriodicallyUpdatedInputs.register(this);
    }

    @Override
    public void update() {
        if (!isEncoderThreaded)
            threadedEncoder.pollReadingsFromEncoder();
        threadedEncoder.processCachedInputs(inputs);
        processCachedInputs();

        Logger.processInputs(Constants.LogConfigs.sensorInputsPath + sensorPath, inputs);
        Logger.recordOutput(Constants.LogConfigs.sensorProcessedInputsPath + sensorPath + "/zeroPosition", zeroPosition);
        Logger.recordOutput(Constants.LogConfigs.sensorProcessedInputsPath + sensorPath + "/velocity", getVelocity());
        Logger.recordOutput(Constants.LogConfigs.sensorProcessedInputsPath + sensorPath + "/latestPosition", getLatestPosition());
        Logger.recordOutput(Constants.LogConfigs.sensorProcessedInputsPath + sensorPath + "/positions", getPositions());
        Logger.recordOutput(Constants.LogConfigs.sensorProcessedInputsPath + sensorPath + "/timeStamps", getTimeStamps());
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
