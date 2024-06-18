package frc.robot.HardwareDrivers.Abstractions;

public interface RelativePositionEncoderSynchronized extends RelativePositionEncoder {
    double getCurrentPositionSynchronized();
    double getCurrentVelocitySynchronized();
}
