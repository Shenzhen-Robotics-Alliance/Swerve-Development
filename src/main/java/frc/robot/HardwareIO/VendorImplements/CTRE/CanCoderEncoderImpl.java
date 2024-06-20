package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;

public class CanCoderEncoderImpl implements RawEncoder {
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
}
