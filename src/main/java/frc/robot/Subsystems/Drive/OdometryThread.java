package frc.robot.Subsystems.Drive;

import com.ctre.phoenix6.BaseStatusSignal;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.TimeStampedEncoder;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;
import frc.robot.Helpers.ArrayHelpers;
import frc.robot.Helpers.TimeHelpers;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OdometryThread extends Thread {
    private final boolean waitForTimeSync;
    private final BaseStatusSignal[] odometrySignals;
    private final List<TimeStampedEncoderReal> odometryEncoders;
    private final Lock timeStampsLock = new ReentrantLock();
    private final Queue<Double> timeStampsQueue = new ConcurrentLinkedDeque<>();
    public OdometryThread(List<TimeStampedEncoderReal> odometryEncoders, boolean waitForTimeSync) {
        this.odometryEncoders = odometryEncoders;

        final List<BaseStatusSignal> baseStatusSignals = new ArrayList<>();
        for (TimeStampedEncoderReal threadedEncoder: odometryEncoders)
            if (threadedEncoder.statusSignal != null)
                baseStatusSignals.add(threadedEncoder.statusSignal);
        this.odometrySignals = baseStatusSignals.toArray(new BaseStatusSignal[0]);
        this.waitForTimeSync = waitForTimeSync;

        for (BaseStatusSignal statusSignal:odometrySignals)
            statusSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETRY_FREQ);

        setDaemon(true);
    }

    @Override
    public synchronized void start() {
        /* start the thread if there is at least one odometry signal */
        if (odometrySignals.length != 0)
            super.start();
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

            for (TimeStampedEncoderReal encoder: odometryEncoders)
                encoder.pollPositionReadingToCache();

            timeStampsLock.lock();
            TimeStampedEncoderReal.offerWithLengthLimit(estimateAverageTimeStamps(odometrySignals), timeStampsQueue);
            timeStampsLock.unlock();
        }
    }

    private double estimateAverageTimeStamps(BaseStatusSignal[] signals) {
        double totalLatency = 0;
        for (BaseStatusSignal signal:signals)
            totalLatency += signal.getTimestamp().getLatency();
        final double averageLatency = signals.length > 0 ? totalLatency/signals.length:0;
        return TimeHelpers.getRealTime() - averageLatency;
    }

    public double[] getOdometerMeasurementTimeStamps() {
        timeStampsLock.lock();
        final double[] timeStamps = ArrayHelpers.toDoubleArray(new ArrayList<>(timeStampsQueue));
        timeStampsQueue.clear();
        timeStampsLock.unlock();
        return timeStamps;
    }
}