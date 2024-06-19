package frc.robot.HardwareIOs.HelperImplements;

import frc.robot.HardwareIOs.Abstractions.AbsoluteRotationEncoder;
import frc.robot.Helpers.MathHelpers.AngleHelpers;

import java.util.function.Supplier;

public class GenericAbsoluteEncoder implements AbsoluteRotationEncoder {
    /* the position and velocity feeder (not calibrated but already inverted as needed) */
    private final Supplier<Double> uncalibratedPositionFeeder, velocityFeeder;
    private double zeroPosition;

    /**
     * the
     * */
    public GenericAbsoluteEncoder(Supplier<Double> rawPositionFeeder, Supplier<Double> rawVelocityFeeder, boolean inverted) {
        final double factor = inverted ? -1:1;
        this.uncalibratedPositionFeeder = () -> rawPositionFeeder.get() * factor;
        this.velocityFeeder = () -> rawVelocityFeeder.get() * factor;
        this.zeroPosition = 0;
    }

    @Override
    public double getAbsoluteRotationRadian() {
        return AngleHelpers.simplifyAngle(AngleHelpers.getActualDifference(zeroPosition, uncalibratedPositionFeeder.get()));
    }

    @Override
    public double getAngularVelocity() {
        return velocityFeeder.get();
    }

    @Override
    public void setCurrentPositionAs(double actualRotation) {
        // when the actual rotation is actualRotation, the sensor reads uncalibratedPositionFeeder.getAsDouble()
        // when the actual rotation is zero, the sensor reads
        //  uncalibratedPositionFeeder.getAsDouble() - getActualDifference(0, actualRotation)
        setZeroPosition(
                AngleHelpers.simplifyAngle(
                        uncalibratedPositionFeeder.get() - AngleHelpers.getActualDifference(0, actualRotation)
                )
        );
    }

    @Override
    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
    }
}
