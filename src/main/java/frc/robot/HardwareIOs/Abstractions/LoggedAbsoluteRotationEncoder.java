package frc.robot.HardwareIOs.Abstractions;

import frc.robot.Helpers.MathHelpers.AngleHelpers;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.function.Supplier;

public class LoggedAbsoluteRotationEncoder implements LoggedSensor {
    private final String name;
    /* the position and velocity feeder (not calibrated but already inverted as needed) */
    private final RawAnalogSensor rawAnalogSensor;
    private final double factor;
    private double zeroPosition;
    private double absoluteRotationRadian, angularVelocity;

    public LoggedAbsoluteRotationEncoder(String name) {
        this(name, null, false);
    }

    public LoggedAbsoluteRotationEncoder(String name, RawAnalogSensor rawSensor, boolean inverted) {
        this.name = name;
        this.rawAnalogSensor = rawSensor;
        this.factor = inverted ? -1:1;
        this.zeroPosition = 0;
        this.absoluteRotationRadian = 0;
        this.angularVelocity = 0;

        LoggedSensor.register(this);
    }
    @Override
    public void update() {
        if (rawAnalogSensor == null)
            return;

        this.absoluteRotationRadian = AngleHelpers.simplifyAngle(AngleHelpers.getActualDifference(zeroPosition, rawAnalogSensor.getValue() * factor));
        this.angularVelocity = getUncalibratedPositionInvertedAsNeeded();
    }

    @Override
    public String getSensorPath() {
        return "AbsoluteRotationEncoder/" + name;
    }

    @Override
    public void toLog(LogTable table) {
        table.put("absoluteRotationRadian", absoluteRotationRadian);
        table.put("angularVelocity", angularVelocity);
    }

    @Override
    public void fromLog(LogTable table) {
        absoluteRotationRadian = table.get("absoluteRotationRadian", 0);
        angularVelocity = table.get("angularVelocity", 0);
    }


    public double getAbsoluteRotationRadian() {
        return absoluteRotationRadian;
    }

    public double getAngularVelocity() {
        return angularVelocity;
    }

    public void setCurrentPositionAs(double actualRotation) {
        // when the actual rotation is actualRotation, the sensor reads uncalibratedPositionFeeder.getAsDouble()
        // when the actual rotation is zero, the sensor reads
        //  uncalibratedPositionFeeder.getAsDouble() - getActualDifference(0, actualRotation)
        setZeroPosition(
                AngleHelpers.simplifyAngle(
                        getUncalibratedPositionInvertedAsNeeded() - AngleHelpers.getActualDifference(0, actualRotation)
                )
        );
    }

    private double getUncalibratedPositionInvertedAsNeeded() {
        return rawAnalogSensor.getValue() * factor;
    }

    public void setZeroPosition(double zeroPosition) {
        this.zeroPosition = zeroPosition;
    }
}
