package frc.robot.HardwareIO.Helpers;

import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.HardwareIO.Abstractions.TimeStampedEncoder;
import frc.robot.Helpers.ArrayHelpers;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedAbsoluteRotationEncoder implements PrePeriodicUpdatedInputs.PrePeriodicUpdateInput {
    private final String sensorPath;
    private boolean updateEncoderInMainThread;
    private final TimeStampedEncoder timeStampedEncoder;
    private final TimeStampedEncoder.TimeStampedEncoderInputs inputs;

    private double[] absoluteRotations = new double[] {};
    private double zeroPosition;

    public LoggedAbsoluteRotationEncoder(String name) {
        this(name, new TimeStampedEncoderReplay());
        updateEncoderInMainThread = false;
    }

    public LoggedAbsoluteRotationEncoder(String name, RawEncoder rawEncoder) {
        this(name, new TimeStampedEncoderReal(null, rawEncoder));
        updateEncoderInMainThread = true;
    }

    public LoggedAbsoluteRotationEncoder(String name, TimeStampedEncoder timeStampedEncoder) {
        this.sensorPath = "AbsoluteRotationEncoders/" + name;
        this.timeStampedEncoder = timeStampedEncoder;
        this.inputs = new TimeStampedEncoder.TimeStampedEncoderInputs();
        this.zeroPosition = 0;
        updateEncoderInMainThread = false;

        PrePeriodicUpdatedInputs.register(name, this);
    }

    @Override
    public void update() {
        if (updateEncoderInMainThread) this.timeStampedEncoder.pollPositionReadingToCache();

        this.timeStampedEncoder.processInputsUsingCachedReadings(inputs);
        Logger.processInputs(Constants.LogConfigs.SENSORS_INPUTS_PATH + sensorPath, inputs);

        processAbsoluteRotations();
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/latestUncalibratedPosition (Rad)", AngleHelpers.simplifyAngle(inputs.latestUncalibratedPosition * Math.PI * 2));
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/zeroPosition", zeroPosition);
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/latestAbsoluteRotationRadian", getLatestAbsoluteRotationRadian());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/absoluteRotationsRadian", getAbsoluteRotations());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/timeStamps", getTimeStamps());
        Logger.recordOutput(Constants.LogConfigs.SENSORS_PROCESSED_INPUTS_PATH + sensorPath + "/angularVelocity", getAngularVelocity());
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
        return AngleHelpers.simplifyAngle(
                AngleHelpers.getActualDifference(
                        zeroPosition,
                        uncalibratedEncoderPosition * Math.PI * 2)
        );
    }

    public double getAngularVelocity() {
        return inputs.encoderVelocity;
    }

    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
    }
}
