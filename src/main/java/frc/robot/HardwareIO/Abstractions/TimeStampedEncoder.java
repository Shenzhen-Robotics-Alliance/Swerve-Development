package frc.robot.HardwareIO.Abstractions;

import frc.robot.Helpers.ArrayHelpers;
import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

import java.util.ArrayList;
import java.util.List;

public interface TimeStampedEncoder {
    final class TimeStampedEncoderInputs implements LoggableInputs {
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
            latestUncalibratedPosition = table.get("latestUncalibratedPosition", latestUncalibratedPosition);
            encoderVelocity = table.get("encoderVelocity", encoderVelocity); // works
        }

        @Override
        public String toString() {
            return "Time Stamped Encoder Reading with latest position: " + latestUncalibratedPosition + " and latest velocity: " + encoderVelocity;
        }
    }

    void pollPositionReadingToCache();

    void processInputsUsingCachedReadings(TimeStampedEncoderInputs inputs);
}
