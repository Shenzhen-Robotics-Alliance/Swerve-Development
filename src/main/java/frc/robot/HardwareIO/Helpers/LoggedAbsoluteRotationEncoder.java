package frc.robot.HardwareIO.Helpers;

import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import org.littletonrobotics.junction.Logger;

public class LoggedAbsoluteRotationEncoder implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String name;
    /* the position and velocity feeder (not calibrated but already inverted as needed) */
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs inputs;
    private double zeroPosition;

    public LoggedAbsoluteRotationEncoder(String name) {
        this(name, new RawEncoder() {});
    }

    public LoggedAbsoluteRotationEncoder(String name, RawEncoder rawEncoder) {
        this.name = name;
        this.rawEncoder = rawEncoder;
        this.inputs = new RawEncoder.RawEncoderInputs();
        this.zeroPosition = 0;

        PeriodicallyUpdatedInputs.register(this);
    }
    @Override
    public void update() {
        this.rawEncoder.updateEncoderInputs(inputs);
        Logger.processInputs("RawInputs/" + "AbsoluteRotationEncoders/" + name, inputs);
    }

    public double getAbsoluteRotationRadian() {
        return AngleHelpers.simplifyAngle(AngleHelpers.getActualDifference(zeroPosition, inputs.uncalibratedEncoderPosition));
    }

    public double getAngularVelocity() {
        return inputs.encoderVelocity;
    }

    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
    }
}
