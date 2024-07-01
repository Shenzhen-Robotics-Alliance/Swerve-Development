package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.robot.HardwareIO.Abstractions.RawMotor;

public class VictorSPXMotorImpl implements RawMotor {
    private final VictorSPX victorSPXInstance;

    public VictorSPXMotorImpl(VictorSPX victorSPXInstance, boolean inverted) {
        this.victorSPXInstance = victorSPXInstance;
        this.victorSPXInstance.setInverted(inverted);
    }

    @Override
    public void setOutPut(double output) {
        victorSPXInstance.set(VictorSPXControlMode.PercentOutput, output);
    }

    @Override
    public void configureZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        victorSPXInstance.setNeutralMode(switch (zeroPowerBehavior){
            case BREAK -> NeutralMode.Brake;
            case RELAX -> NeutralMode.Coast;
        });
    }
}
