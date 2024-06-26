package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;

public class Pigeon2EncoderImpl implements CTRETimeSynchronizedEncoder {
    private final Pigeon2 pigeon2Instance;

    private final StatusSignal<Double> yawSignal;

    public Pigeon2EncoderImpl(Pigeon2 pigeon2Instance) {
        this.pigeon2Instance = pigeon2Instance;
        this.yawSignal = pigeon2Instance.getYaw();
        yawSignal.setUpdateFrequency(100, 0.25);
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        yawSignal.refresh();
        return yawSignal.getValue() / (Math.PI * 2);
    }

    @Override
    public double getEncoderVelocity() {
        return pigeon2Instance.getRate() / (Math.PI * 2);
    }

    @Override
    public TimeStampedEncoderReal toThreadedEncoder() {
        return new TimeStampedEncoderReal(this.getPositionSignal(), this);
    }

    @Override
    public StatusSignal<Double> getPositionSignal() {
        return yawSignal;
    }
}
