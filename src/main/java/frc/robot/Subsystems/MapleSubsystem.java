package frc.robot.Subsystems;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;

public abstract class MapleSubsystem extends SubsystemBase {
    public static final List<MapleSubsystem> instances = new ArrayList<>();
    private long previousUpdateTimeStamp = 0;
    public static void register(MapleSubsystem instance) {
        instances.add(instance);
    }
    public static void cancelRegister(MapleSubsystem instance) {
        instances.remove(instance);
    }

    public static void subsystemsInit() {
        for (MapleSubsystem instance:instances)
            instance.onReset();
    }

    private static boolean wasEnabled = false;
    public static void subsystemsPeriodic() { // TODO remove this and make it in timed robot
        // periodic() is called from CommandScheduler, we only need to check for enable/disable

        if (DriverStation.isEnabled() && (!wasEnabled))
            enableSubsystems();
        else if (DriverStation.isDisabled() && (wasEnabled))
            disableSubsystems();
    }
    private static void enableSubsystems() {
        for (MapleSubsystem instance:instances)
            if (!instance.updateDuringDisabled) {
                instance.onReset();
                instance.onEnable();
            }
    }

    private static void disableSubsystems() {
        for (MapleSubsystem instance:instances)
            if (!instance.updateDuringDisabled)
                instance.onDisable();
    }

    private final boolean updateDuringDisabled;
    public MapleSubsystem(String name) {
        this(name, false);
    }
    public MapleSubsystem(String name, boolean updateDuringDisabled) {
        super(name);
        this.updateDuringDisabled = updateDuringDisabled;
        register(this);
    }

    public void onEnable() {}
    public void onDisable() {}
    public abstract void onReset();
    public abstract void periodic(double dt);

    @Override
    public void periodic() {
        final long t0 = System.nanoTime();
        periodic(getDt());
        final double cpuTimeMS = (System.nanoTime() - t0) / 1_000_000.0;
        Logger.recordOutput(Constants.LogConfigs.SYSTEM_PERFORMANCE_PATH + getName() + "-CPUTimeMS", cpuTimeMS);
    }

    private double getDt() {
        if (previousUpdateTimeStamp == 0) {
            previousUpdateTimeStamp = Logger.getTimestamp();
            return Robot.defaultPeriodSecs;
        }
        final long dt_micros = Logger.getTimestamp() - previousUpdateTimeStamp;
        previousUpdateTimeStamp = Logger.getTimestamp();
        return dt_micros / 1_000_000.0;
    }
}
