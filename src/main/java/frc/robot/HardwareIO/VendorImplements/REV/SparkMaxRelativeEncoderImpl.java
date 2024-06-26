package frc.robot.HardwareIO.VendorImplements.REV;

import com.revrobotics.SparkRelativeEncoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;

public class SparkMaxRelativeEncoderImpl implements RawEncoder {
    private final SparkRelativeEncoder sparkRelativeEncoderInstance;

    public SparkMaxRelativeEncoderImpl(SparkRelativeEncoder sparkRelativeEncoderInstance, boolean inverted) {
        this.sparkRelativeEncoderInstance = sparkRelativeEncoderInstance;
        this.sparkRelativeEncoderInstance.setInverted(inverted);
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        return sparkRelativeEncoderInstance.getPosition();
    }

    @Override
    public double getEncoderVelocity() {
        return sparkRelativeEncoderInstance.getVelocity();
    }
}
