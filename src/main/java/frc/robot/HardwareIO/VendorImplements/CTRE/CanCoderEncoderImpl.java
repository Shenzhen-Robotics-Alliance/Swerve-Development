package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;
import frc.robot.HardwareIO.Helpers.ThreadedEncoder;

public class CanCoderEncoderImpl implements CTRETimeSynchronizedEncoder {
    private final CANcoder canCoderInstance;
    private final StatusSignal<Double> positionSignal, velocitySignal;

    public CanCoderEncoderImpl(CANcoder canCoderInstance) {
        this.canCoderInstance = canCoderInstance;
        this.positionSignal = canCoderInstance.getAbsolutePosition();
        positionSignal.setUpdateFrequency(100, 0.25);
        this.velocitySignal = canCoderInstance.getVelocity();
        velocitySignal.setUpdateFrequency(100, 0.25);
    }

    @Override
    public void updateEncoderInputs(RawEncoderInputs inputs) {
        positionSignal.refresh();
        velocitySignal.refresh();
        inputs.uncalibratedEncoderPosition = positionSignal.getValue();
        inputs.encoderVelocity = velocitySignal.getValue();
    }

    @Override
    public ThreadedEncoder toThreadedEncoder() {
        this.positionSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETER_FREQ, 5.0/ Constants.ChassisConfigs.ODOMETER_FREQ);
        return new ThreadedEncoder(this);
    }

    @Override
    public StatusSignal<Double> getPositionSignal() {
        return positionSignal;
    }
}
