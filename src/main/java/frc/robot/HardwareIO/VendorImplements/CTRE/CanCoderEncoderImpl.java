package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;

public class CanCoderEncoderImpl extends CTRETimeSynchronizedEncoder {
    private final CANcoder canCoderInstance;
    private final StatusSignal<Double> velocitySignal;
    private boolean isThreadedEncoder = false;

    public CanCoderEncoderImpl(CANcoder canCoderInstance) {
        super(canCoderInstance.getAbsolutePosition());
        this.canCoderInstance = canCoderInstance;
        this.velocitySignal = canCoderInstance.getVelocity();
        velocitySignal.setUpdateFrequency(Constants.ChassisConfigs.MOTORS_FREQ, Constants.ChassisConfigs.MOTORS_TIMEOUT_SECONDS);
    }

    @Override
    public double getEncoderVelocity() {
        velocitySignal.refresh();
        return velocitySignal.getValue();
    }
}
