package frc.robot.HardwareIO.Abstractions;

import com.ctre.phoenix6.StatusSignal;
import frc.robot.HardwareIO.Abstractions.RawEncoder;
import frc.robot.HardwareIO.Abstractions.ThreadedEncoder;

public interface CTRETimeSynchronizedEncoder extends RawEncoder {
    ThreadedEncoder toThreadedEncoder();
    StatusSignal<Double> getPositionSignal();
}
