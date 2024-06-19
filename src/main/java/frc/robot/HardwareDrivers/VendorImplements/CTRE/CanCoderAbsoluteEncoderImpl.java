package frc.robot.HardwareDrivers.VendorImplements.CTRE;

import com.ctre.phoenix6.StatusSignal;
import com.ctre.phoenix6.hardware.CANcoder;
import frc.robot.HardwareDrivers.Abstractions.AbsoluteRotationEncoderSynchronized;
import frc.robot.HardwareDrivers.HelperImplements.GenericAbsoluteEncoder;

public class CanCoderAbsoluteEncoderImpl extends GenericAbsoluteEncoder implements AbsoluteRotationEncoderSynchronized {
    private final CANcoder phoenix6CANCoderInstance;
    private final StatusSignal<Double> positionSignal, velocitySignal;
    public CanCoderAbsoluteEncoderImpl(CANcoder phoenix6CANCoderInstance, boolean inverted) {
        super(
                phoenix6CANCoderInstance.getAbsolutePosition().asSupplier(),
                phoenix6CANCoderInstance.getVelocity().asSupplier(),
                inverted);
        this.phoenix6CANCoderInstance = phoenix6CANCoderInstance;
        this.positionSignal = phoenix6CANCoderInstance.getAbsolutePosition();
        this.velocitySignal = phoenix6CANCoderInstance.getVelocity();

        positionSignal.setUpdateFrequency(250);
    }

    @Override
    public double getAbsoluteRotationRadianSynchronized() {
        positionSignal.waitForUpdate(0.05);
        return super.getAbsoluteRotationRadian();
    }

    @Override
    public double getAngularVelocitySynchronized() {
        velocitySignal.waitForUpdate(0.05);
        return super.getAngularVelocity();
    }
}
