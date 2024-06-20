package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.HardwareIO.Abstractions.RawMotor;

public class TalonSRXMotorImpl implements RawMotor {
    private final TalonSRX talonSRX;

    public TalonSRXMotorImpl(TalonSRX talonSRX, boolean inverted) {
        this.talonSRX = talonSRX;
        this.talonSRX.setInverted(inverted);
    }

    @Override
    public void setOutPut(double output) {
        talonSRX.set(TalonSRXControlMode.PercentOutput, output);
    }

    @Override
    public void configureZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        this.talonSRX.setNeutralMode(switch (zeroPowerBehavior){
            case BREAK -> NeutralMode.Brake;
            case COAST -> NeutralMode.Coast;
        });
    }
}
