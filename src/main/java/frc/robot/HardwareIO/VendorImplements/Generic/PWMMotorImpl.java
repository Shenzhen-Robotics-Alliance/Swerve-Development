package frc.robot.HardwareIO.VendorImplements.Generic;

import edu.wpi.first.wpilibj.motorcontrol.PWMMotorController;
import frc.robot.HardwareIO.Abstractions.RawMotor;

public class PWMMotorImpl implements RawMotor {
    private final PWMMotorController pwmMotorControllerInstance;

    public PWMMotorImpl(PWMMotorController pwmMotorControllerInstance, boolean inverted) {
        this.pwmMotorControllerInstance = pwmMotorControllerInstance;
        this.pwmMotorControllerInstance.setInverted(inverted);
    }

    @Override
    public void setOutPut(double output) {
        this.pwmMotorControllerInstance.set(output);
    }
}
