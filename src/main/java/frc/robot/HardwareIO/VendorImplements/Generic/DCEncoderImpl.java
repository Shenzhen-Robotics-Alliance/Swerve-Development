package frc.robot.HardwareIO.VendorImplements.Generic;

import edu.wpi.first.wpilibj.DutyCycleEncoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;

public class DCEncoderImpl implements RawEncoder {
    private final DutyCycleEncoder dutyCycleEncoder;

    public DCEncoderImpl(DutyCycleEncoder dutyCycleEncoder) {
        this.dutyCycleEncoder = dutyCycleEncoder;
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        return dutyCycleEncoder.getAbsolutePosition();
    }

    @Override
    public double getEncoderVelocity() {
        return 0; // TODO velocity filtering
    }
}
