package frc.robot.HardwareIOs.Abstractions;

import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.HashSet;
import java.util.Set;

public interface LoggedSensor extends LoggableInputs {
    /**
     * updates the sensor reading
     * this method MUST NOT be thread-blocking!
     * */
    void update();

    /**
     * gets the unique name of the instance
     * */
    String getName();
    Set<LoggedSensor> instances = new HashSet<>();
    static void register(LoggedSensor instance) {
        instances.add(instance);
    }

    static void remove(LoggedSensor instance) {
        instances.remove(instance);
    }

    static void updateSensors() {
        for (LoggedSensor instance:instances) {
            instance.update();
            Logger.processInputs("Sensors/" + instance.getName(), instance);
        }
    }
}
