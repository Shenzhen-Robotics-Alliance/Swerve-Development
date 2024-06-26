package frc.robot.HardwareIO.Abstractions;

public interface RawEncoder {
    /**
     * get the uncalibrated encoder position
     * @return the uncalibrated encoder position, in revolutions. (Every revolution of the motor around its rotter is 1 encoder position)
     * */
    double getUncalibratedEncoderPosition();

    /**
     * get the encoder velocity
     * @return the velocity of the rotter, in revolutions/second
     * */
    double getEncoderVelocity();
}
