package frc.robot.HardwareDrivers.Abstractions;

public interface RelativePositionEncoder {
    default void setCurrentPositionAsZeroReading() {
        setCurrentPositionAs(0);
    }
    void setCurrentPositionAs(double desiredReading);

    double getCurrentPosition();
    double getCurrentVelocity();
}
