package frc.robot.HardwareIOs.Helpers;

import frc.robot.HardwareIOs.Abstractions.RawEncoder;
import org.littletonrobotics.junction.Logger;

public class LoggedRelativePositionEncoder implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String name;
    /* the raw encoder (not calibrated but already inverted as needed) */
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs inputs;
    private double zeroPosition;
    public LoggedRelativePositionEncoder(String name) {
        this(name, new RawEncoder() {});
    }
    public LoggedRelativePositionEncoder(String name, RawEncoder rawEncoder) {
        this.name = name;
        this.rawEncoder = rawEncoder;
        this.inputs = new RawEncoder.RawEncoderInputs();
        this.zeroPosition = 0;

        PeriodicallyUpdatedInputs.register(this);
    }

    @Override
    public void update() {
        rawEncoder.updateEncoderInputs(inputs);
        Logger.processInputs("Inputs/RelativePositionEncoder/" + name, inputs);
    }

    public void setCurrentPositionAs(double desiredReading) {
        this.zeroPosition = inputs.getUncalibratedEncoderPosition() - desiredReading;
    }

    public double getCurrentPosition() {
        return inputs.getUncalibratedEncoderPosition() - zeroPosition;
    }

    public double getCurrentVelocity() {
        return inputs.getEncoderVelocity();
    }
}
