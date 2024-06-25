package frc.robot.HardwareIO.Helpers;

import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.Helpers.ArrayHelpers;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedAbsoluteRotationEncoder implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String sensorPath;
    private boolean isEncoderThreaded;
    private final ThreadedEncoder threadedEncoder;
    private final ThreadedEncoder.ThreadedEncoderInputs inputs;

    private double[] absoluteRotations = new double[] {};
    private double zeroPosition;

    public LoggedAbsoluteRotationEncoder(String name) {
        this(name, new ThreadedEncoder(new RawEncoder() {}));
    }

    public LoggedAbsoluteRotationEncoder(String name, RawEncoder rawEncoder) {
        this(name, new ThreadedEncoder(rawEncoder));
        isEncoderThreaded = false;
    }

    public LoggedAbsoluteRotationEncoder(String name, ThreadedEncoder threadedEncoder) {
        this.sensorPath = "AbsoluteRotationEncoders/" + name;
        this.threadedEncoder = threadedEncoder;
        this.inputs = new ThreadedEncoder.ThreadedEncoderInputs();
        this.zeroPosition = 0;
        isEncoderThreaded = true;

        PeriodicallyUpdatedInputs.register(this);
    }

    @Override
    public void update() {
        if (!isEncoderThreaded)
            this.threadedEncoder.pollReadingsFromEncoder();
        this.threadedEncoder.processCachedInputs(inputs);
        processAbsoluteRotations();

        Logger.processInputs(Constants.LogConfigs.SENSORS_INPUTS_PATH + sensorPath, inputs);
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/zeroPosition", zeroPosition);
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/angularVelocity", getAngularVelocity());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/latestAbsoluteRotationRadian", getLatestAbsoluteRotationRadian());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/absoluteRotationsRadian", getAbsoluteRotations());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/timeStamps", getTimeStamps());
    }

    private void processAbsoluteRotations() {
        this.absoluteRotations = new double[inputs.uncalibratedEncoderPosition.size()];

        for (int i = 0; i < inputs.uncalibratedEncoderPosition.size(); i++)
            absoluteRotations[i] = getAbsoluteRotationRadian(inputs.uncalibratedEncoderPosition.get(i));
    }

    public double[] getAbsoluteRotations() {
        return absoluteRotations;
    }

    public double[] getTimeStamps() {
        return ArrayHelpers.toDoubleArray(inputs.timeStamps);
    }

    public double getLatestAbsoluteRotationRadian() {
        return getAbsoluteRotationRadian(inputs.latestUncalibratedPosition);
    }

    private double getAbsoluteRotationRadian(double uncalibratedEncoderPosition) {
        return AngleHelpers.simplifyAngle(AngleHelpers.getActualDifference(zeroPosition, uncalibratedEncoderPosition));
    }

    public double getAngularVelocity() {
        return inputs.encoderVelocity;
    }

    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
    }
}
