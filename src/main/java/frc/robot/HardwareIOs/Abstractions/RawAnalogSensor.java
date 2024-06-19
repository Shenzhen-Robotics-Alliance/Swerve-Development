package frc.robot.HardwareIOs.Abstractions;

public interface RawAnalogSensor {
    double getValue(); // eg. position
    double getFirstDerivative();
}
