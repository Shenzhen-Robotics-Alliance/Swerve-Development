package frc.robot.HardwareDrivers.VendorImplements.CTRE;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.HardwareDrivers.Abstractions.Motor;

/**
 * Motor implementation with TalonSRX
 * <a href="https://store.ctr-electronics.com/talon-srx/">CTRE Talon SRX</a>
 * */
public class TalonSRXMotorImpl implements Motor {
    private final TalonSRX talonSRXInstance;
    private final double factor;

    public TalonSRXMotorImpl(TalonSRX talonSRXInstance, boolean inverted) {
        this.talonSRXInstance = talonSRXInstance;
        this.factor = inverted ? -1:1;
    }

    @Override
    public void setMotorZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        talonSRXInstance.setNeutralMode(switch (zeroPowerBehavior) {
            case COAST -> NeutralMode.Coast;
            case BREAK -> NeutralMode.Brake;
        });
    }

    @Override
    public void setOutPut(double power) {
        talonSRXInstance.set(TalonSRXControlMode.PercentOutput, power * factor);
    }
}
