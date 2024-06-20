package frc.robot.HardwareIO.Helpers;

import frc.robot.HardwareIO.Abstractions.RawDigitalSwitch;
import org.littletonrobotics.junction.Logger;

public class LoggedDigitalSwitch implements PeriodicallyUpdatedInputs.PeriodicallyUpdatedInput {
    private final String name;
    private final RawDigitalSwitch rawDigitalSwitch;
    private final RawDigitalSwitch.RawDigitalInput inputs;

    public LoggedDigitalSwitch(String name) {
        this(name, new RawDigitalSwitch() {});
    }

    public LoggedDigitalSwitch(String name, RawDigitalSwitch rawDigitalSwitch) {
        this.name = name;
        this.rawDigitalSwitch = rawDigitalSwitch;
        this.inputs = new RawDigitalSwitch.RawDigitalInput();
        PeriodicallyUpdatedInputs.register(this);
    }

    @Override
    public void update() {
        this.rawDigitalSwitch.updateDigitalInputs(inputs);
        Logger.processInputs("RawInputs/DigitalSwitch" + name, inputs);
    }

    public boolean isTriggered() {
        return inputs.triggered;
    }
}
