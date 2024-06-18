package frc.robot.Hardware.Interfaces;

public interface Motor {
    void setOutPut(double power);

    enum ZeroPowerBehavior {
        COAST,
        BREAK
    }
    default void setMotorZeroPowerBehavior(ZeroPowerBehavior zeroPowerBehavior) {
        // some motors do not support zero power behavior settings (like DC motors)
    }

    default void relax() {
        setMotorZeroPowerBehavior(ZeroPowerBehavior.COAST);
        setOutPut(0);
    }

    default void lock() {
        setMotorZeroPowerBehavior(ZeroPowerBehavior.BREAK);
        setOutPut(0);
    }
}
