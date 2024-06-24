package frc.robot.HardwareIO.Abstractions;

import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ThreadedEncoder {
    private final RawEncoder rawEncoder;
    private final RawEncoder.RawEncoderInputs rawEncoderInputs = new RawEncoder.RawEncoderInputs();
    private final Queue<Double>
            positionQueue = new ConcurrentLinkedDeque<>(),
            timeStampsQueue = new ConcurrentLinkedDeque<>();

    public ThreadedEncoder() {
        this.rawEncoder = null;
    }

    public ThreadedEncoder(RawEncoder rawEncoder) {
        this.rawEncoder = rawEncoder;
    }

    class ThreadedEncoderInputs implements LoggableInputs {
        public List<Double>
                uncalibratedEncoderPosition = new ArrayList<>(),
                timeStamps = new ArrayList<>();
        public double encoderVelocity = 0;

        @Override
        public void toLog(LogTable table) {
            table.put("uncalibratedEncoderPosition", toArray(uncalibratedEncoderPosition));
            table.put("timeStamps", toArray(timeStamps));
            table.put("encoderVelocity", encoderVelocity);
        }

        @Override
        public void fromLog(LogTable table) {
            toList(table.get("uncalibratedEncoderPosition", new double[]{}), uncalibratedEncoderPosition);
            toList(table.get("timeStamps", new double[]{}), timeStamps);
            encoderVelocity = table.get("encoderVelocity", 0);
        }
    }

    public void pollReadingsFromEncoder() {
        if (rawEncoder == null) return;

        rawEncoder.updateEncoderInputs(rawEncoderInputs);
        positionQueue.add(rawEncoderInputs.uncalibratedEncoderPosition);
        timeStampsQueue.add(Logger.getRealTimestamp() * 1.0e-6);
    }

    void processCachedInputs(ThreadedEncoderInputs inputs) {
        if (rawEncoder == null) return;

        inputs.uncalibratedEncoderPosition = new ArrayList<>(positionQueue);
        positionQueue.clear();
        inputs.timeStamps = new ArrayList<>(timeStampsQueue);
        timeStampsQueue.clear();

        inputs.encoderVelocity = rawEncoderInputs.encoderVelocity;
    }

    public static double[] toArray(List<Double> originalList) {
        double[] array = new double[originalList.size()];
        for (int i = 0; i < originalList.size(); i++)
            array[i] = originalList.get(i);
        return array;
    }

    public static void toList(double[] data, List<Double> targetList) {
        targetList.clear();
        for (double d:data)
            targetList.add(d);
    }
}
