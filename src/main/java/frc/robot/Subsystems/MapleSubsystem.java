package frc.robot.Subsystems;

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

    public static void robotInit() {
        for (MapleSubsystem instance:instances)
            instance.onReset();
    }
    public static void robotEnable() {
        for (MapleSubsystem instance:instances)
            if (!instance.updateDuringDisabled)
                instance.onReset();
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
