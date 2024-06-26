package frc.robot.HardwareIO.Abstractions;

import com.ctre.phoenix6.StatusSignal;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;

public interface CTRETimeSynchronizedEncoder extends RawEncoder {
    TimeStampedEncoderReal toThreadedEncoder();
    StatusSignal<Double> getPositionSignal();
}
