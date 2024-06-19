package frc.robot.HardwareIOs.VendorImplements.REV;

import com.revrobotics.CANSparkBase;
import com.revrobotics.CANSparkMax;
import frc.robot.HardwareIOs.Abstractions.Motor;

/**
 * Motor implementation with CanSparkMax
 * <a href="https://www.revrobotics.com/rev-11-2158/">Rev Spark Max</a>
 * */
public class CanSparkMaxMotorImpl implements Motor {
    private final CANSparkMax canSparkMaxInstance;
    private final double factor;

    public CanSparkMaxMotorImpl(CANSparkMax canSparkMaxInstance, boolean reversed) {
        this.canSparkMaxInstance = canSparkMaxInstance;
        this.factor = reversed ? -1:1;
    }

    @Override
    public void setMotorZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        canSparkMaxInstance.setIdleMode(switch (zeroPowerBehavior){
            case COAST -> CANSparkBase.IdleMode.kCoast;
            case BREAK -> CANSparkBase.IdleMode.kBrake;
        });
    }

    @Override
    public void setOutPut(double power) {
        canSparkMaxInstance.set(power * factor);
    }
}
