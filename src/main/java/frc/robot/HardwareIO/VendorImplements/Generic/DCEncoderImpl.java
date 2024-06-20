package frc.robot.HardwareIO.VendorImplements.Generic;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;

public class DCEncoderImpl implements RawEncoder {
    private final DutyCycleEncoder dutyCycleEncoder;

    public DCEncoderImpl(DutyCycleEncoder dutyCycleEncoder) {
        this.dutyCycleEncoder = dutyCycleEncoder;
    }

    @Override
    public void updateEncoderInputs(RawEncoderInputs inputs) {
        inputs.uncalibratedEncoderPosition = dutyCycleEncoder.getAbsolutePosition();
        // TODO velocity input with linear velocity
    }
}
