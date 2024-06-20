package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.configs.CANcoderConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;

public class CanCoderEncoderImpl implements RawEncoder {
    private final CANcoder canCoderInstance;

    public CanCoderEncoderImpl(CANcoder canCoderInstance) {
        this.canCoderInstance = canCoderInstance;
    }

    @Override
    public void updateEncoderInputs(RawEncoderInputs inputs) {
        // TODO: update values
    }
}
