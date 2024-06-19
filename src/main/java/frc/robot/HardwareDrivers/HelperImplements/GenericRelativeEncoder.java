package frc.robot.HardwareDrivers.HelperImplements;

import frc.robot.HardwareDrivers.Abstractions.RelativePositionEncoder;

import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

// helper class to calibrate a relative encoder
public class GenericRelativeEncoder implements RelativePositionEncoder {
    /* feeds the current position or velocity (not calibrated but already inverted as needed) */
    private final Supplier<Double> uncalibratedPositionFeeder, velocityFeeder;
    private double zeroPosition;
    public GenericRelativeEncoder(Supplier<Double> rawPositionFeeder, Supplier<Double> rawVelocityFeeder, boolean inverted) {
        final double factor = inverted ? -1:1;
        this.uncalibratedPositionFeeder = () -> rawPositionFeeder.get() * factor;
        this.velocityFeeder = () -> rawVelocityFeeder.get() * factor;

        this.zeroPosition = rawPositionFeeder.get();
    }

    @Override
    public void setCurrentPositionAs(double desiredReading) {
        this.zeroPosition = uncalibratedPositionFeeder.get() - desiredReading;
    }

    @Override
    public double getCurrentPosition() {
        return (uncalibratedPositionFeeder.get() - zeroPosition);
    }

    @Override
    public double getCurrentVelocity() {
        return velocityFeeder.get();
    }
}
