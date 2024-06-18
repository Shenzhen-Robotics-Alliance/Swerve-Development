package frc.robot.HardwareDrivers.Abstractions;

import edu.wpi.first.math.geometry.Rotation2d;
import frc.robot.Helpers.MathHelpers.AngleHelpers;

public interface AbsoluteRotationEncoder {
    double getAbsoluteRotationRadian();
    default Rotation2d getRotation2d() {
        return Rotation2d.fromRadians(getAbsoluteRotationRadian());
    }

    default void setCurrentPositionAsZeroPosition() {
        setCurrentPositionAs(0);
    }
    void setCurrentPositionAs(double reading);
    /**
     * tells the hardware that when the encoder has this value as its raw reading, it is actually in zero position
     * @param zeroPosition the raw reading of the encoder, when it is actually pointing at zero position
     * */
    void setZeroPosition(double zeroPosition);


    double getAngularVelocity();

}