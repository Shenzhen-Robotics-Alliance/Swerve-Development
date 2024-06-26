package frc.robot.Subsystems.Drive;

import com.ctre.phoenix6.BaseStatusSignal;
import frc.robot.Constants;
import frc.robot.HardwareIO.Helpers.ThreadedEncoder;

import java.util.ArrayList;
import java.util.List;

public class OdometryThread extends Thread {
    private final boolean waitForTimeSync;
    private final BaseStatusSignal[] odometrySignals;
    private final List<ThreadedEncoder> odometryEncoders;
    public OdometryThread(List<ThreadedEncoder> odometryEncoders, boolean waitForTimeSync) {
        this.odometryEncoders = odometryEncoders;

        final List<BaseStatusSignal> baseStatusSignals = new ArrayList<>();
        for (ThreadedEncoder threadedEncoder: odometryEncoders)
            if (threadedEncoder.statusSignal != null)
                baseStatusSignals.add(threadedEncoder.statusSignal);
        this.odometrySignals = baseStatusSignals.toArray(new BaseStatusSignal[0]);

        for (BaseStatusSignal statusSignal:odometrySignals)
            statusSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETRY_FREQ);

        this.waitForTimeSync = waitForTimeSync;

        setDaemon(true);
    }

    @Override
    public void run() {
        while (true) {
            if (waitForTimeSync)
                BaseStatusSignal.waitForAll(5.0 / Constants.ChassisConfigs.ODOMETRY_FREQ, odometrySignals);
            else {
                BaseStatusSignal.refreshAll(odometrySignals);
                try {
                    Thread.sleep((long) (1000 / Constants.ChassisConfigs.ODOMETRY_FREQ));
                } catch (InterruptedException ignored) {}
            }
            for (ThreadedEncoder encoder: odometryEncoders)
                encoder.pollReadingsFromEncoder();
        }
    }
}