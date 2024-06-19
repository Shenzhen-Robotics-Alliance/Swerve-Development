package frc.robot.HardwareIOs.VendorImplements.CTRE;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.VictorSPXControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import frc.robot.HardwareIOs.Abstractions.Motor;

/**
 * Motor implementation with VictorSPX
 * <a href="https://store.ctr-electronics.com/victor-spx/">CTRE Victor SPX</a>
 * */
public class VictorSPXMotorImpl implements Motor {
    private final VictorSPX victorSPXInstance;
    private final double factor;

    public VictorSPXMotorImpl(VictorSPX victorSPXInstance, boolean inverted) {
        this.victorSPXInstance = victorSPXInstance;
        this.factor = inverted ? -1:1;
    }

    @Override
    public void setMotorZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        this.victorSPXInstance.setNeutralMode(switch (zeroPowerBehavior){
            case COAST -> NeutralMode.Coast;
            case BREAK -> NeutralMode.Brake;
        });
    }

    @Override
    public void setOutPut(double power) {
        this.victorSPXInstance.set(VictorSPXControlMode.PercentOutput, power * factor);
    }
}
