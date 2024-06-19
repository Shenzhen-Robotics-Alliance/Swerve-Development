package frc.robot.HardwareIOs.Abstractions;

import org.littletonrobotics.junction.LogTable;

import java.util.function.Supplier;
public class LoggedDigitalSwitch implements LoggedSensor {
    private final String name;
    private final Supplier<Boolean> rawDigitalSwitch;
    private final boolean inverted;
    private boolean triggered;

    public LoggedDigitalSwitch(String name) {
        this(name, null, false);
    }

    public LoggedDigitalSwitch(String name, Supplier<Boolean> rawDigitalSwitch, boolean inverted) {
        this.name = name;
        this.rawDigitalSwitch = rawDigitalSwitch;
        this.inverted  = inverted;
        triggered = false;
        LoggedSensor.register(this);
    }

    @Override
    public void update() {
        /* if it's just simulation, skip */
        if (this.rawDigitalSwitch == null)
            return;
        this.triggered = inverted == (this.rawDigitalSwitch.get());
    }

    @Override
    public void toLog(LogTable table) {
        table.put("triggered", triggered);
    }

    @Override
    public void fromLog(LogTable table) {
        triggered = table.get("triggered", false);
    }

    @Override
    public String getSensorPath() {
        return "DigitalSwitches/" + name;
    }

    public boolean isTriggered() {
        return triggered;
    }
}
