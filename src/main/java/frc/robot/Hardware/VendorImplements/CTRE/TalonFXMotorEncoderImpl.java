package frc.robot.Hardware.VendorImplements.CTRE;

import frc.robot.Hardware.Interfaces.Motor;
import frc.robot.Hardware.Interfaces.RelativePositionEncoder;

public class TalonFXMotorEncoderImpl implements Motor, RelativePositionEncoder {
    @Override
    public void setOutPut(double power) {

    }

    @Override
    public void setMotorZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        Motor.super.setMotorZeroPowerBehavior(zeroPowerBehavior);
    }

    @Override
    public void setCurrentPositionAs(double desiredReading) {

    }

    @Override
    public double getCurrentPosition() {
        return 0;
    }
}
