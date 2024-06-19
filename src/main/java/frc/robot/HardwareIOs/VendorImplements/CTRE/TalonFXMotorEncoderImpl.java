package frc.robot.HardwareIOs.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import frc.robot.HardwareIOs.HelperImplements.GenericRelativeEncoder;
import frc.robot.HardwareIOs.Abstractions.Motor;
import frc.robot.HardwareIOs.Abstractions.RelativePositionEncoderSynchronized;

/**
 * Motor and RelativePositionEncoderSynchronized implementation with TalonFX
 * <a href="https://store.ctr-electronics.com/falcon-500-powered-by-talon-fx/">CTRE Talon FX</a>
 * */
public class TalonFXMotorEncoderImpl extends GenericRelativeEncoder implements Motor, RelativePositionEncoderSynchronized {
    private final TalonFX talonFXInstance;
    private final StatusSignal<Double> positionSignal, velocitySignal;

    public TalonFXMotorEncoderImpl(TalonFX talonFXInstance, boolean inverted) {
        super(
                talonFXInstance.getPosition().asSupplier(),
                talonFXInstance.getVelocity().asSupplier(),
                inverted
        );
        this.talonFXInstance = talonFXInstance;
        this.positionSignal = talonFXInstance.getPosition();
        this.velocitySignal = talonFXInstance.getVelocity();

        positionSignal.setUpdateFrequency(250);
        velocitySignal.setUpdateFrequency(250);
    }

    @Override
    public void setOutPut(double power) {
        talonFXInstance.set(power);
    }

    @Override
    public void setMotorZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        talonFXInstance.setNeutralMode(switch (zeroPowerBehavior) {
            case COAST -> NeutralModeValue.Coast;
            case BREAK -> NeutralModeValue.Brake;
        });
    }

    @Override
    public double getCurrentPositionSynchronized() {
        positionSignal.waitForUpdate(0.05);
        return super.getCurrentPosition();
    }

    @Override
    public double getCurrentVelocitySynchronized() {
        velocitySignal.waitForUpdate(0.05);
        return super.getCurrentPosition();
    }
}
