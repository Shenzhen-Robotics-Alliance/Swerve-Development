package frc.robot.HardwareIO.Abstractions;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.function.Supplier;

public interface RawDigitalSwitch {
    class RawDigitalInput implements LoggableInputs {
        public boolean triggered = false;

        @Override
        public void toLog(LogTable table) {
            table.put("triggered", triggered);
        }

        @Override
        public void fromLog(LogTable table) {
            triggered = table.get("triggered", false);
        }
    }
    default void updateDigitalInputs(RawDigitalInput inputs) {}

    static RawDigitalSwitch simpleRawDigitalInput(Supplier<Boolean> booleanSupplier) {
        return new RawDigitalSwitch() {
            @Override
            public void updateDigitalInputs(RawDigitalInput inputs) {
                inputs.triggered = booleanSupplier.get();
            }
        };
    }
}
