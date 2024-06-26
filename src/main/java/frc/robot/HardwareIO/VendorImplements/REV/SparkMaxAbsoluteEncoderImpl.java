package frc.robot.HardwareIO.VendorImplements.REV;

import com.revrobotics.SparkAbsoluteEncoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;

public class SparkMaxAbsoluteEncoderImpl implements RawEncoder {
    private final SparkAbsoluteEncoder sparkAbsoluteEncoderInstance;

    public SparkMaxAbsoluteEncoderImpl(SparkAbsoluteEncoder sparkAbsoluteEncoderInstance, boolean inverted) {
        this.sparkAbsoluteEncoderInstance = sparkAbsoluteEncoderInstance;
        this.sparkAbsoluteEncoderInstance.setInverted(inverted);
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        return sparkAbsoluteEncoderInstance.getPosition();
    }

    @Override
    public double getEncoderVelocity() {
        return sparkAbsoluteEncoderInstance.getVelocity();
    }
}
