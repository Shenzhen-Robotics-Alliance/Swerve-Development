package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.CTRETimeSynchronizedEncoder;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.HardwareIO.Abstractions.RawMotor;
import frc.robot.HardwareIO.Abstractions.ThreadedEncoder;

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
    public void updateEncoderInputs(RawEncoderInputs inputs) {
        positionSignal.refresh();
        velocitySignal.refresh();

        inputs.uncalibratedEncoderPosition = positionSignal.getValue();
        inputs.encoderVelocity = velocitySignal.getValue();
    }

    @Override
    public ThreadedEncoder toThreadedEncoder() {
        this.positionSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETER_FREQ, 5.0/ Constants.ChassisConfigs.ODOMETER_FREQ);
        return new ThreadedEncoder(this);
    }

    @Override
    public StatusSignal<Double> getPositionSignal() {
        return positionSignal;
    }
}
