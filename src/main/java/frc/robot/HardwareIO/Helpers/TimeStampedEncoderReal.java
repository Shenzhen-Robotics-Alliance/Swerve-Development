package frc.robot.HardwareIO.Helpers;

import com.ctre.phoenix6.BaseStatusSignal;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.HardwareIO.Abstractions.TimeStampedEncoder;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TimeStampedEncoderReal implements TimeStampedEncoder {
    public final BaseStatusSignal statusSignal;
    private final RawEncoder rawEncoder;
    private final Queue<Double>
            positionCache = new ConcurrentLinkedDeque<>(),
            timeStampsCache = new ConcurrentLinkedDeque<>();

    public TimeStampedEncoderReal(BaseStatusSignal statusSignal, RawEncoder rawEncoder) {
        this.statusSignal = statusSignal;
        this.rawEncoder = rawEncoder;
    }

    @Override
    public void pollPositionReadingToCache() {
        offerWithLengthLimit(rawEncoder.getUncalibratedEncoderPosition(), positionCache);
        offerWithLengthLimit(Logger.getRealTimestamp() * 1.0e-6, timeStampsCache);
    }

    private void offerWithLengthLimit(double d, Queue<Double> targetQueue) {
        targetQueue.offer(d);
        if (targetQueue.size() < Constants.ChassisConfigs.ODOMETRY_QUEUE_LENGTH_LIMIT) return;

        DriverStation.reportWarning("Warning: Odometry Queue length limit reached", false);
        targetQueue.poll();
    }

    @Override
    public void processInputsUsingCachedReadings(TimeStampedEncoder.TimeStampedEncoderInputs inputs) {
        inputs.uncalibratedEncoderPosition = new ArrayList<>(positionCache);
        positionCache.clear();
        inputs.timeStamps = new ArrayList<>(timeStampsCache);
        timeStampsCache.clear();

        inputs.encoderVelocity = rawEncoder.getEncoderVelocity();
        if (!inputs.uncalibratedEncoderPosition.isEmpty())
            inputs.latestUncalibratedPosition = inputs.uncalibratedEncoderPosition.get(inputs.uncalibratedEncoderPosition.size()-1);
    }
}
