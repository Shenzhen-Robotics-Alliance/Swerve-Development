package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawMotor;

public class TalonFXMotorAndEncoderImpl extends CTRETimeSynchronizedEncoder implements RawMotor {
    private final TalonFX talonFXInstance;
    private final StatusSignal<Double> velocitySignal;
    private final double factor;
    public TalonFXMotorAndEncoderImpl(TalonFX talonFXInstance, boolean inverted) {
        super(talonFXInstance.getPosition());
        this.talonFXInstance = talonFXInstance;
        this.velocitySignal = talonFXInstance.getVelocity();
        this.factor = inverted ? -1:1;
        velocitySignal.setUpdateFrequency(Constants.ChassisConfigs.MOTORS_FREQ, Constants.ChassisConfigs.MOTORS_TIMEOUT_SECONDS);
    }

    @Override
    public void setOutPut(double output) {
        talonFXInstance.set(output * factor);
    }

    @Override
    public void configureZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        talonFXInstance.setNeutralMode(switch (zeroPowerBehavior){
            case BREAK -> NeutralModeValue.Brake;
            case RELAX -> NeutralModeValue.Coast;
        });
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        return super.getUncalibratedEncoderPosition() * factor;
    }

    @Override
    public double getEncoderVelocity() {
        velocitySignal.refresh();
        return velocitySignal.getValue() * factor;
    }
}
