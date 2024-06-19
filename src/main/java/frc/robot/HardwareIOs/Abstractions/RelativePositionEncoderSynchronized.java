package frc.robot.HardwareIOs.Abstractions;

public interface RelativePositionEncoderSynchronized extends RelativePositionEncoder {
    double getCurrentPositionSynchronized();
    double getCurrentVelocitySynchronized();
}
