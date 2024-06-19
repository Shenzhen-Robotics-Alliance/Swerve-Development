package frc.robot.HardwareIOs.VendorImplements.REV;

import com.revrobotics.SparkRelativeEncoder;
import frc.robot.HardwareIOs.HelperImplements.GenericRelativeEncoder;

/**
 * AbsolutePositionEncoder implementation with SparkMax Absolute Encoder
 * <a href="https://www.revrobotics.com/rev-11-2158/">Rev Spark Max</a>
 * */
public class SparkMaxRelativePositionEncoderImpl extends GenericRelativeEncoder {
    private final SparkRelativeEncoder sparkRelativeEncoder;
    private final double factor;
    public SparkMaxRelativePositionEncoderImpl(SparkRelativeEncoder sparkRelativeEncoder, boolean inverted) {
        super(
                sparkRelativeEncoder::getPosition,
                sparkRelativeEncoder::getVelocity,
                inverted
        );
        this.sparkRelativeEncoder = sparkRelativeEncoder;
        this.factor = inverted ? -1:1;
    }

    @Override
    public void setCurrentPositionAs(double desiredReading) {
        sparkRelativeEncoder.setPosition(desiredReading * factor);
    }

    @Override
    public double getCurrentPosition() {
        return sparkRelativeEncoder.getPosition() * factor;
    }

    @Override
    public double getCurrentVelocity() {
        return sparkRelativeEncoder.getVelocity() * factor;
    }
}
