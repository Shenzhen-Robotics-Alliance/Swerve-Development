package frc.robot.HardwareIOs.Abstractions;

public interface AbsoluteRotationEncoderSynchronized extends AbsoluteRotationEncoder {
    double getAbsoluteRotationRadianSynchronized();
    double getAngularVelocitySynchronized();
}
