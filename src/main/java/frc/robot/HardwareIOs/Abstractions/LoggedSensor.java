package frc.robot.HardwareIOs.Abstractions;

import frc.robot.Robot;
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
     * gets the unique log path of the instance
     * @return the path of the sensor, in the format "sensorType/sensorName", like "encoder/frontLeftWheelEncoder"
     * */
    String getSensorPath();
    Set<LoggedSensor> instances = new HashSet<>();
    static void register(LoggedSensor instance) {
        instances.add(instance);
    }

    static void remove(LoggedSensor instance) {
        instances.remove(instance);
    }

    static void updateSensors() {
        for (LoggedSensor instance:instances) {
            if (Robot.mode == Robot.Mode.REAL)
                instance.update();
            Logger.processInputs("Sensors/" + instance.getSensorPath(), instance);
        }
    }
}
