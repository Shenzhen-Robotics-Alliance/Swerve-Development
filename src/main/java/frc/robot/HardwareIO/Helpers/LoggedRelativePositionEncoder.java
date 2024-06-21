package frc.robot.HardwareIO.Helpers;

import frc.robot.HardwareIO.Abstractions.RawEncoder;
import org.littletonrobotics.junction.Logger;

public class LoggedRelativePositionEncoder implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String sensorPath;
    /* the raw encoder (not calibrated but already inverted as needed) */
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs inputs;
    private double zeroPosition;
    public LoggedRelativePositionEncoder(String name) {
        this(name, new RawEncoder() {});
    }
    public LoggedRelativePositionEncoder(String name, RawEncoder rawEncoder) {
        this.sensorPath = "RelativePositionEncoder/" + name;
        this.rawEncoder = rawEncoder;
        this.inputs = new RawEncoder.RawEncoderInputs();
        this.zeroPosition = 0;

        PeriodicallyUpdatedInputs.register(this);
    }

    @Override
    public void update() {
        rawEncoder.updateEncoderInputs(inputs);
        Logger.processInputs("RawInputs/" + sensorPath, inputs);
        Logger.recordOutput("ProcessedInputs/" + sensorPath + "/currentPosition", getCurrentPosition());
    }

    public void setCurrentPositionAs(double desiredReading) {
        this.zeroPosition = inputs.uncalibratedEncoderPosition - desiredReading;
    }

    public double getCurrentPosition() {
        return inputs.uncalibratedEncoderPosition - zeroPosition;
    }

    public double getCurrentVelocity() {
        return inputs.encoderVelocity;
    }
}
