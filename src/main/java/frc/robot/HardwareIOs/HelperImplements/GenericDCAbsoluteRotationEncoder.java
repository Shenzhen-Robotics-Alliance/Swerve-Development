package frc.robot.HardwareIOs.HelperImplements;

public class GenericDCAbsoluteRotationEncoder extends GenericAbsoluteEncoder {

    public GenericDCAbsoluteRotationEncoder() {
        super(rawPositionFeeder, rawVelocityFeeder);
    }
}
