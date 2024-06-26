package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;
import frc.robot.HardwareIO.Abstractions.RawMotor;
import frc.robot.HardwareIO.Helpers.ThreadedEncoder;

public class TalonFXMotorAndEncoderImpl implements RawMotor, CTRETimeSynchronizedEncoder {
    private final TalonFX talonFXInstance;
    private final StatusSignal<Double> positionSignal, velocitySignal;
    public TalonFXMotorAndEncoderImpl(TalonFX talonFXInstance, boolean inverted) {
        this.talonFXInstance = talonFXInstance;
        this.talonFXInstance.setInverted(inverted);
        this.positionSignal = talonFXInstance.getPosition();
        this.velocitySignal = talonFXInstance.getVelocity();
        positionSignal.setUpdateFrequency(100, 0.1);
        velocitySignal.setUpdateFrequency(100, 0.1);
    }

    @Override
    public void setOutPut(double output) {
        talonFXInstance.set(output);
    }

    @Override
    public void configureZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        talonFXInstance.setNeutralMode(switch (zeroPowerBehavior){
            case BREAK -> NeutralModeValue.Brake;
            case COAST -> NeutralModeValue.Coast;
        });
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        positionSignal.refresh();
        return positionSignal.getValue();
    }

    @Override
    public double getEncoderVelocity() {
        velocitySignal.refresh();
        return velocitySignal.getValue();
    }

    @Override
    public ThreadedEncoder toThreadedEncoder() {
        return new ThreadedEncoder(this.getPositionSignal(), this);
    }

    @Override
    public StatusSignal<Double> getPositionSignal() {
        return positionSignal;
    }
}
