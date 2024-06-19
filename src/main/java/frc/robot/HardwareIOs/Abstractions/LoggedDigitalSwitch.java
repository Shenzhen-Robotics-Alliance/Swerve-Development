package frc.robot.HardwareIOs.Abstractions;

import org.littletonrobotics.junction.LogTable;

import java.util.function.Supplier;
public class LoggedDigitalSwitch implements LoggedSensor {
   private final String name;
    private final Supplier<Boolean> booleanSupplier;
    private final boolean inverted;
    private boolean triggered;

    public LoggedDigitalSwitch(String name, Supplier<Boolean> booleanSupplier, boolean inverted) {
        this.name = name;
        this.booleanSupplier = booleanSupplier;
        this.inverted  = inverted;
        triggered = false;
        LoggedSensor.register(this);
    }

    @Override
    public void update() {
        this.triggered = inverted == (this.booleanSupplier.get());
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
    public String getName() {
        return "DigitalSwitches/" + name;
    }

    public boolean isTriggered() {
        return triggered;
    }
}
