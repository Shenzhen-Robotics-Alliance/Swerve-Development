package frc.robot.HardwareIO.Abstractions;

import com.ctre.phoenix6.StatusSignal;
import frc.robot.Constants;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;

public abstract class CTRETimeSynchronizedEncoder implements RawEncoder {
    private final StatusSignal<Double> positionSignal;
    private boolean isThreaded = false;

    protected CTRETimeSynchronizedEncoder(StatusSignal<Double> positionSignal) {
        this.positionSignal = positionSignal;
        positionSignal.setUpdateFrequency(Constants.ChassisConfigs.MOTORS_FREQ, Constants.ChassisConfigs.MOTORS_TIMEOUT_SECONDS);
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        if (!isThreaded) positionSignal.refresh();
        return positionSignal.getValue();
    }

    @Override
    public abstract double getEncoderVelocity();

    public TimeStampedEncoderReal toOdometryEncoder() {
        isThreaded = true;
        positionSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETRY_FREQ, Constants.ChassisConfigs.ODOMETER_TIMEOUT_SECONDS);
        return new TimeStampedEncoderReal(this.getPositionSignal(), this);
    }
    public StatusSignal<Double> getPositionSignal() {
        return this.positionSignal;
    }
}
