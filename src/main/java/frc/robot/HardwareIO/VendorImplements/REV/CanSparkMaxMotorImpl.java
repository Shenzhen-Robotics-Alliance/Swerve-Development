package frc.robot.HardwareIO.VendorImplements.REV;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import frc.robot.HardwareIO.Abstractions.RawMotor;

public class CanSparkMaxMotorImpl implements RawMotor {
    private final CANSparkMax canSparkMax;

    public CanSparkMaxMotorImpl(CANSparkMax canSparkMax, boolean inverted) {
        this.canSparkMax = canSparkMax;
        canSparkMax.setInverted(inverted);
    }

    @Override
    public void setOutPut(double output) {
        canSparkMax.set(output);
    }

    @Override
    public void configureZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        canSparkMax.setIdleMode(switch (zeroPowerBehavior){
            case BREAK -> CANSparkBase.IdleMode.kBrake;
            case RELAX -> CANSparkBase.IdleMode.kCoast;
        });
    }
}
