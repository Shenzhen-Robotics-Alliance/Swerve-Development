package frc.robot.Hardware.Interfaces;

import edu.wpi.first.math.geometry.Rotation2d;

public interface AbsolutePositionEncoder {
    double getAbsoluteRotationRadian();
    default Rotation2d getRotation2d() {
        return Rotation2d.fromRadians(getAbsoluteRotationRadian());
    }
}
