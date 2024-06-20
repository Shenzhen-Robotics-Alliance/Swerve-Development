package frc.robot.HardwareIO.Abstractions;


import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface RawEncoder {
    class RawEncoderInputs implements LoggableInputs {
        public double uncalibratedEncoderPosition = 0, encoderVelocity = 0;

        @Override
        public void toLog(LogTable table) {
            table.put("uncalibratedEncoderPosition", uncalibratedEncoderPosition);
            table.put("encoderVelocity", encoderVelocity);
        }

        @Override
        public void fromLog(LogTable table) {
            uncalibratedEncoderPosition = table.get("uncalibratedEncoderPosition", 0);
            encoderVelocity = table.get("encoderVelocity", 0);
        }
    }
    default void updateEncoderInputs(RawEncoderInputs inputs) {}
}
