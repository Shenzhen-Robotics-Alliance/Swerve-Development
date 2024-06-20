package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.HardwareIO.Abstractions.RawMotor;

public class TalonFXMotorAndEncoderImpl implements RawMotor, RawEncoder {
    private final TalonFX talonFXInstance;
    private final StatusSignal<Double> positionSignal, velocitySignal;
    public TalonFXMotorAndEncoderImpl(TalonFX talonFXInstance, boolean inverted) {
        this.talonFXInstance = talonFXInstance;
        this.talonFXInstance.setInverted(inverted);
        this.positionSignal = talonFXInstance.getPosition();
        this.velocitySignal = talonFXInstance.getVelocity();
        positionSignal.setUpdateFrequency(100, 0.25);
        velocitySignal.setUpdateFrequency(100, 0.25);
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
    public void updateEncoderInputs(RawEncoderInputs inputs) {
        positionSignal.refresh();
        velocitySignal.refresh();

        inputs.uncalibratedEncoderPosition = positionSignal.getValue();
        inputs.encoderVelocity = velocitySignal.getValue();
    }
}