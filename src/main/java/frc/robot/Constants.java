package frc.robot;

public final class Constants {
    public static final class LogConfigs {
        // avoid typos
        public static final String
                SENSORS_INPUTS_PATH = "RawInputs/",
                SENSORS_PROCESSED_INPUTS_PATH = "ProcessedInputs/",
                SYSTEM_PERFORMANCE_PATH = "SystemPerformance/";

    }

    public static final class ChassisConfigs {
        public static final double ODOMETRY_FREQ = 250;
        public static final int ODOMETRY_QUEUE_LENGTH_LIMIT = 100;
        public static final String CHASSIS_CANIVORE_NAME = "ChassisCanivore";
    }

    public static final class SwerveModuleConfigs {
        public static final double MINIMUM_USAGE_SPEED = 0.12; // meter/second
        public static final double NON_USAGE_TIME_RESET_SWERVE = 0.5;
    }
}
