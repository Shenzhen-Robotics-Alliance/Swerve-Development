package frc.robot.Subsystems.Drive;

import com.ctre.phoenix6.BaseStatusSignal;
import frc.robot.Constants;
import frc.robot.HardwareIO.Helpers.PeriodicallyUpdatedInputs;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;
import frc.robot.Helpers.ArrayHelpers;
import frc.robot.Helpers.TimeHelpers;
import frc.robot.Robot;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class OdometryThread extends Thread {
    private static final List<TimeStampedEncoderReal> registeredEncoders = new ArrayList<>();
    public static void registerOdometryEncoder(TimeStampedEncoderReal encoder) {
        registeredEncoders.add(encoder);
    }

    private final BaseStatusSignal[] odometrySignals;
    private final List<TimeStampedEncoderReal> odometryEncoders;
    public static final Lock odometerLock = new ReentrantLock();
    private final Queue<Double> timeStampsQueue = new ConcurrentLinkedDeque<>();

    private static OdometryThread instance = null;
    public static OdometryThread getInstance() {
        if (instance == null)
            instance = new OdometryThread(registeredEncoders);
        return instance;
    }
    public OdometryThread(List<TimeStampedEncoderReal> odometryEncoders) {
        this.odometryEncoders = odometryEncoders;

        final List<BaseStatusSignal> baseStatusSignals = new ArrayList<>();
        for (TimeStampedEncoderReal threadedEncoder: odometryEncoders)
            if (threadedEncoder.statusSignal != null)
                baseStatusSignals.add(threadedEncoder.statusSignal);
        this.odometrySignals = baseStatusSignals.toArray(new BaseStatusSignal[0]);

        for (BaseStatusSignal statusSignal:odometrySignals)
            statusSignal.setUpdateFrequency(Constants.ChassisConfigs.ODOMETRY_FREQ);

        setDaemon(true);
        PeriodicallyUpdatedInputs.register(this::processCachedOdometerMeasurementTimeStamps);
    }

    @Override
    public synchronized void start() {
        /* start the thread if there is at least one odometry signal */
        if (!odometryEncoders.isEmpty() && Robot.mode == Robot.Mode.REAL)
            super.start();
    }

    @Override
    public void run() {
        while (true) {
            boolean successful;
            if (Constants.ChassisConfigs.ODOMETRY_WAIT_FOR_TIME_SYNC)
                successful = BaseStatusSignal.waitForAll(Constants.ChassisConfigs.ODOMETER_TIMEOUT_SECONDS, odometrySignals).isOK();
            else {
                BaseStatusSignal.refreshAll(odometrySignals);
                try {
                    Thread.sleep((long) (1000 / Constants.ChassisConfigs.ODOMETRY_FREQ));
                } catch (InterruptedException ignored) {}
                successful = true;
            }

            odometerLock.lock();
            for (TimeStampedEncoderReal encoder: odometryEncoders)
                encoder.pollPositionReadingToCache();
            TimeStampedEncoderReal.offerWithLengthLimit(estimateAverageTimeStamps(new BaseStatusSignal[]{}), timeStampsQueue);
            odometerLock.unlock();
        }
    }

    private double estimateAverageTimeStamps(BaseStatusSignal[] signals) {
        double totalLatency = 0;
        for (BaseStatusSignal signal:signals)
            totalLatency += signal.getTimestamp().getLatency();
        final double averageLatency = signals.length > 0 ? totalLatency/signals.length:0;
        return TimeHelpers.getRealTime() - averageLatency;
    }

    private double[] odometerTimeStamps = new double[]{};
    private void processCachedOdometerMeasurementTimeStamps() {
        odometerTimeStamps = ArrayHelpers.toDoubleArray(timeStampsQueue.toArray(new Double[0]));
        timeStampsQueue.clear();
    }

    public double[] getOdometerTimeStampsSincePreviousRobotPeriod() {
        return odometerTimeStamps;
    }
}