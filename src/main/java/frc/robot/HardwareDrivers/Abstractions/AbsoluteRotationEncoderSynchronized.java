package frc.robot.HardwareDrivers.Abstractions;

public interface AbsoluteRotationEncoderSynchronized extends AbsoluteRotationEncoder {
    double getAbsoluteRotationRadianSynchronized();
    double getAngularVelocitySynchronized();
}
