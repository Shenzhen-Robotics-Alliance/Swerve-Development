package frc.robot.HardwareIO.Helpers;

import com.ctre.phoenix6.BaseStatusSignal;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.Helpers.ArrayHelpers;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ThreadedEncoder {
    public final BaseStatusSignal statusSignal;
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs rawEncoderInputs = new RawEncoder.RawEncoderInputs();
    private final Queue<Double>
            positionQueue = new ConcurrentLinkedDeque<>(),
            timeStampsQueue = new ConcurrentLinkedDeque<>();

    public ThreadedEncoder(BaseStatusSignal statusSignal, RawEncoder rawEncoder) {
        this.statusSignal = statusSignal;
        this.rawEncoder = rawEncoder;
    }

    public static final class ThreadedEncoderInputs implements LoggableInputs {
        public List<Double>
                uncalibratedEncoderPosition = new ArrayList<>(),
                timeStamps = new ArrayList<>();
        public double encoderVelocity = 0, latestUncalibratedPosition = 0;

        @Override
        public void toLog(LogTable table) {
            table.put("uncalibratedEncoderPosition", ArrayHelpers.toDoubleArray(uncalibratedEncoderPosition));
            table.put("timeStamps", ArrayHelpers.toDoubleArray(timeStamps));

            table.put("latestUncalibratedPosition", latestUncalibratedPosition);
            table.put("encoderVelocity", encoderVelocity);
        }

        @Override
        public void fromLog(LogTable table) {
            ArrayHelpers.toDoubleList(table.get("uncalibratedEncoderPosition", new double[]{}), uncalibratedEncoderPosition);
            ArrayHelpers.toDoubleList(table.get("timeStamps", new double[]{}), timeStamps);

            latestUncalibratedPosition = table.get("latestUncalibratedPosition", 0);
            encoderVelocity = table.get("encoderVelocity", 0);
        }
    }

    public void pollReadingsFromEncoder() {
        if (rawEncoder == null) return;

        rawEncoder.updateEncoderInputs(rawEncoderInputs);
        offerWithLengthLimit(rawEncoderInputs.uncalibratedEncoderPosition, positionQueue);
        offerWithLengthLimit(Logger.getRealTimestamp() * 1.0e-6, timeStampsQueue);
    }

    private void offerWithLengthLimit(double d, Queue<Double> targetQueue) {
        targetQueue.offer(d);
        if (targetQueue.size() < Constants.ChassisConfigs.ODOMETRY_QUEUE_LENGTH_LIMIT) return;

        DriverStation.reportWarning("Warning: Odometry Queue length limit reached", false);
        targetQueue.poll();
    }

    void processCachedInputs(ThreadedEncoderInputs inputs) {
        if (rawEncoder == null) return;

        inputs.uncalibratedEncoderPosition = new ArrayList<>(positionQueue);
        positionQueue.clear();
        inputs.timeStamps = new ArrayList<>(timeStampsQueue);
        timeStampsQueue.clear();

        inputs.encoderVelocity = rawEncoderInputs.encoderVelocity;
        if (!inputs.uncalibratedEncoderPosition.isEmpty())
            inputs.latestUncalibratedPosition = inputs.uncalibratedEncoderPosition.get(inputs.uncalibratedEncoderPosition.size()-1);
    }
}
