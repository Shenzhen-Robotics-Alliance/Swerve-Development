package frc.robot.HardwareIOs.Abstractions;


import org.littletonrobotics.junction.LogTable;
import org.littletonrobotics.junction.inputs.LoggableInputs;

public interface RawEncoder {
    class RawEncoderInputs implements LoggableInputs {
        private double uncalibratedEncoderPosition = 0, encoderVelocity = 0;

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

        public double getUncalibratedEncoderPosition() {
            return uncalibratedEncoderPosition;
        }

        public double getEncoderVelocity() {
            return encoderVelocity;
        }
    }
    default void updateEncoderInputs(RawEncoderInputs inputs) {}
}
