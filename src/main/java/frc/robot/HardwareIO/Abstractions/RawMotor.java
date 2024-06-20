package frc.robot.HardwareIO.Abstractions;

/**
 * a raw motor with adjustable output
 * */
public interface RawMotor {
    /**
     * sets the output of the motor
     * the raw motor should decide whether to invert its output according to the installation direction
     * @param output the requested output, -1~1
     * */
    void setOutPut(double output);
    enum ZeroPowerBehavior {
        /** the motor generates a force that stops the rotter */
        BREAK,
        /** the motor allows the rotter to spin freely */
        COAST
    }
    /**
     * sets the zero power behavior of the motion (if adjustable)
     * @param zeroPowerBehavior the behavior of the motor when the power is 0
     * */
    void configureZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior);
}
