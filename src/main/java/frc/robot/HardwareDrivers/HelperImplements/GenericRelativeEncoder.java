package frc.robot.HardwareDrivers.HelperImplements;

import frc.robot.HardwareDrivers.Abstractions.RelativePositionEncoder;

import java.util.function.DoubleSupplier;

// helper class to calibrate a relative encoder
public class GenericRelativeEncoder implements RelativePositionEncoder {
    /* feeds the current position or velocity (not calibrated but already inverted as needed) */
    private final DoubleSupplier uncalibratedPositionFeeder, velocityFeeder;
    private double zeroPosition;
    public GenericRelativeEncoder(DoubleSupplier rawPositionFeeder, DoubleSupplier rawVelocityFeeder, boolean inverted) {
        final double factor = inverted ? -1:1;
        this.uncalibratedPositionFeeder = () -> rawPositionFeeder.getAsDouble() * factor;
        this.velocityFeeder = () -> rawVelocityFeeder.getAsDouble() * factor;

        this.zeroPosition = rawPositionFeeder.getAsDouble();
    }

    @Override
    public void setCurrentPositionAs(double desiredReading) {
        this.zeroPosition = uncalibratedPositionFeeder.getAsDouble() - desiredReading;
    }

    @Override
    public double getCurrentPosition() {
        return (uncalibratedPositionFeeder.getAsDouble() - zeroPosition);
    }

    @Override
    public double getCurrentVelocity() {
        return velocityFeeder.getAsDouble();
    }
}
