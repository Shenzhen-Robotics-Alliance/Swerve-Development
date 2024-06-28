package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;

public class CanCoderEncoderImpl implements CTRETimeSynchronizedEncoder {
    private final CANcoder canCoderInstance;
    private final StatusSignal<Double> positionSignal, velocitySignal;
    private boolean isThreadedEncoder = false;

    public CanCoderEncoderImpl(CANcoder canCoderInstance) {
        this.canCoderInstance = canCoderInstance;
        this.positionSignal = canCoderInstance.getAbsolutePosition();
        positionSignal.setUpdateFrequency(100, 0.25);
        this.velocitySignal = canCoderInstance.getVelocity();
        velocitySignal.setUpdateFrequency(100, 0.25);
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        if (!isThreadedEncoder)
            positionSignal.refresh();
        return positionSignal.getValue();
    }

    @Override
    public double getEncoderVelocity() {
        velocitySignal.refresh();
        return velocitySignal.getValue();
    }

    @Override
    public TimeStampedEncoderReal toThreadedEncoder() {
        this.positionSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETRY_FREQ, 5.0/ Constants.ChassisConfigs.ODOMETRY_FREQ);
        this.isThreadedEncoder = true;
        return new TimeStampedEncoderReal(getPositionSignal(), this);
    }

    @Override
    public StatusSignal<Double> getPositionSignal() {
        return positionSignal;
    }
}
