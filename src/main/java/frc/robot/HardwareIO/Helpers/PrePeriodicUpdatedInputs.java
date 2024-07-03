package frc.robot.HardwareIO.Helpers;

import frc.robot.Constants;
import frc.robot.Helpers.TimeHelpers;
import frc.robot.Subsystems.Drive.OdometryThread;
import org.littletonrobotics.junction.Logger;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class PrePeriodicUpdatedInputs {
    private static final Map<String, PrePeriodicUpdateInput> inputs = new HashMap<>();
    public interface PrePeriodicUpdateInput {
        void update();
    }

    public static void register(String name, PrePeriodicUpdateInput input) {
        inputs.put(name, input);
    }
    public static void cancelRegister(String name) {
        inputs.remove(name);
    }

    public static void updateInputs() {
        OdometryThread.odometerLock.lock();
        for (String name:inputs.keySet()) {
            final double realTime = TimeHelpers.getRealTime();
            inputs.get(name).update();
            Logger.recordOutput(Constants.LogConfigs.SYSTEM_PERFORMANCE_PATH + "PrePeriodicUpdatedInputsTime/" + name + " update time(ms)", (TimeHelpers.getRealTime()-realTime)*1000);
        }

        OdometryThread.odometerLock.unlock();
    }
}
