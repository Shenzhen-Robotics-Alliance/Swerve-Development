package frc.robot.HardwareIO.VendorImplements.CTRE;

import com.ctre.phoenix6.hardware.Pigeon2;

public class Pigeon2EncoderImpl extends CTRETimeSynchronizedEncoder {
    private final Pigeon2 pigeon2Instance;

    public Pigeon2EncoderImpl(Pigeon2 pigeon2Instance) {
        super(pigeon2Instance.getYaw());
        this.pigeon2Instance = pigeon2Instance;
    }

    @Override
    public double getUncalibratedEncoderPosition() {
        return super.getUncalibratedEncoderPosition() / (Math.PI * 2);
    }

    @Override
    public double getEncoderVelocity() {
        return pigeon2Instance.getRate() / (Math.PI * 2);
    }
}
