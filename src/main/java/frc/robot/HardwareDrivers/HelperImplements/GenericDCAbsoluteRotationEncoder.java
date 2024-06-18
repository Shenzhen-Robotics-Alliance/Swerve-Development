package frc.robot.HardwareDrivers.HelperImplements;

public class GenericDCAbsoluteRotationEncoder extends GenericAbsoluteEncoder {

    public GenericDCAbsoluteRotationEncoder() {
        super(rawPositionFeeder, rawVelocityFeeder);
    }
}
