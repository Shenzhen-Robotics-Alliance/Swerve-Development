package frc.robot.HardwareDrivers.HelperImplements;

import frc.robot.HardwareDrivers.Abstractions.AbsoluteRotationEncoder;
import frc.robot.Helpers.MathHelpers.AngleHelpers;

import java.util.function.DoubleSupplier;

public class GenericAbsoluteEncoder implements AbsoluteRotationEncoder {
    /* the position and velocity feeder (not calibrated but already inverted as needed) */
    private final DoubleSupplier uncalibratedPositionFeeder, velocityFeeder;
    private double zeroPosition;

    /**
     * the
     * */
    public GenericAbsoluteEncoder(DoubleSupplier rawPositionFeeder, DoubleSupplier rawVelocityFeeder, boolean inverted) {
        final double factor = inverted ? -1:1;
        this.uncalibratedPositionFeeder = () -> rawPositionFeeder.getAsDouble() * factor;
        this.velocityFeeder = () -> rawVelocityFeeder.getAsDouble() * factor;
        this.zeroPosition = 0;
    }

    @Override
    public double getAbsoluteRotationRadian() {
        return AngleHelpers.simplifyAngle(AngleHelpers.getActualDifference(zeroPosition, uncalibratedPositionFeeder.getAsDouble()));
    }

    @Override
    public double getAngularVelocity() {
        return velocityFeeder.getAsDouble();
    }

    @Override
    public void setCurrentPositionAs(double actualRotation) {
        // when the actual rotation is actualRotation, the sensor reads uncalibratedPositionFeeder.getAsDouble()
        // when the actual rotation is zero, the sensor reads
        //  uncalibratedPositionFeeder.getAsDouble() - getActualDifference(0, actualRotation)
        setZeroPosition(
                AngleHelpers.simplifyAngle(
                        uncalibratedPositionFeeder.getAsDouble() - AngleHelpers.getActualDifference(0, actualRotation)
                )
        );
    }

    @Override
    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
    }
}
