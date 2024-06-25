package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.Pigeon2;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;
import frc.robot.HardwareIO.Helpers.ThreadedEncoder;

public class Pigeon2EncoderImpl implements CTRETimeSynchronizedEncoder {
    private final Pigeon2 pigeon2Instance;

    private final StatusSignal<Double> yawSignal;

    public Pigeon2EncoderImpl(Pigeon2 pigeon2Instance) {
        this.pigeon2Instance = pigeon2Instance;
        this.yawSignal = pigeon2Instance.getYaw();
        yawSignal.setUpdateFrequency(100, 0.25);
    }

    @Override
    public void updateEncoderInputs(RawEncoderInputs inputs) {
        yawSignal.refresh();
        inputs.uncalibratedEncoderPosition = yawSignal.getValue();
        inputs.encoderVelocity = pigeon2Instance.getRate();
    }

    @Override
    public ThreadedEncoder toThreadedEncoder() {
        this.yawSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETER_FREQ, 5.0/ Constants.ChassisConfigs.ODOMETER_FREQ);
        return new ThreadedEncoder(this);
    }

    @Override
    public StatusSignal<Double> getPositionSignal() {
        return yawSignal;
    }
}
