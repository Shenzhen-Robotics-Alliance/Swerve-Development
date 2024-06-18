package frc.robot.Hardware.VendorImplements.REV;

import frc.robot.Hardware.Interfaces.AbsolutePositionEncoder;

public class SparkMaxAbsoluteEncoderImpl implements AbsolutePositionEncoder {
    @Override
    public double getAbsoluteRotationRadian() {
        return 0;
    }

    @Override
    public void setCurrentReading(double currentReading) {

    }
}
