package frc.robot.HardwareIO.Helpers;

import frc.robot.HardwareIO.Abstractions.TimeStampedEncoder;

public class TimeStampedEncoderReplay implements TimeStampedEncoder {
    @Override
    public void pollPositionReadingToCache() {}

    @Override
    public void processInputsUsingCachedReadings(TimeStampedEncoderInputs inputs) {}
}
