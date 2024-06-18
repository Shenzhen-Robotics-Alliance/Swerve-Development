package frc.robot.Hardware.Interfaces;

public interface RelativePositionEncoder {
    default void setCurrentPositionAsZeroReading() {
        setCurrentPositionAs(0);
    }
    void setCurrentPositionAs(double desiredReading);

    double getCurrentPosition();
}
