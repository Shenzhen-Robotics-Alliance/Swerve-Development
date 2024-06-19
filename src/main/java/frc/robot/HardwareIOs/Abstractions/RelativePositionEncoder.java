package frc.robot.HardwareIOs.Abstractions;

public interface RelativePositionEncoder {
    default void setCurrentPositionAsZeroReading() {
        setCurrentPositionAs(0);
    }
    void setCurrentPositionAs(double desiredReading);

    double getCurrentPosition();
    double getCurrentVelocity();
}
