package frc.robot.Helpers;

import org.littletonrobotics.junction.Logger;

public class TimeHelpers {
    public static void delay(double seconds) {
        try {
            // Convert seconds to total milliseconds
            long totalMillis = (long) (seconds * 1000);
            // Calculate the remaining nanoseconds
            int nanoPart = (int) ((seconds * 1000 - totalMillis) * 1000000);

            // Pause the thread for the specified duration in milliseconds and nanoseconds
            Thread.sleep(totalMillis, nanoPart);
        } catch (InterruptedException e) {
            // Restore the interrupted status
            Thread.currentThread().interrupt();

            // Optionally, handle the interruption, e.g. logging or throwing a runtime exception
            System.err.println("The sleep was interrupted");
        }
    }

    public static double getTime() {
        return Logger.getTimestamp() / 1_000_000.0;
    }

    public static double getRealTime() {
        return Logger.getRealTimestamp() / 1_000_000.0;
    }
}
