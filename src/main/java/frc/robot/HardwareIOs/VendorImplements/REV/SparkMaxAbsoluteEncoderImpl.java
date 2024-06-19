package frc.robot.HardwareIOs.VendorImplements.REV;

import com.revrobotics.SparkAbsoluteEncoder;
import frc.robot.HardwareIOs.HelperImplements.GenericAbsoluteEncoder;

/**
 * AbsolutePositionEncoder implementation with SparkMax Absolute Encoder
 * <a href="https://www.revrobotics.com/rev-11-2158/">Rev Spark Max</a>
 * */
public class SparkMaxAbsoluteEncoderImpl extends GenericAbsoluteEncoder {
    public SparkMaxAbsoluteEncoderImpl(SparkAbsoluteEncoder sparkAbsoluteEncoder, boolean inverted) {
        super(
                sparkAbsoluteEncoder::getPosition,
                sparkAbsoluteEncoder::getVelocity,
                inverted
        );
    }
}
