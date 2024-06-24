package frc.robot.HardwareIO.Abstractions;

import com.ctre.phoenix6.StatusSignal;
import frc.robot.HardwareIO.Helpers.ThreadedEncoder;

public interface CTRETimeSynchronizedEncoder extends RawEncoder {
    ThreadedEncoder toThreadedEncoder();
    StatusSignal<Double> getPositionSignal();
}
