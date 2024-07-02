package frc.robot;

import frc.robot.Helpers.MechanismControlHelpers.MapleSimplePIDController;
import frc.robot.UnitTests.WheelsCalibration;

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
        public static final double MOTORS_FREQ = 100;
        public static final double ODOMETER_TIMEOUT_SECONDS = 0.05;
        public static final double MOTORS_TIMEOUT_SECONDS = 0.25;
        public static final int ODOMETRY_QUEUE_LENGTH_LIMIT = 100;
        public static final String CHASSIS_CANBUS_NAME = "ChassisCanivore";

        public static final double DEFAULT_GEAR_RATIO = 6.12;
        public static final double DEFAULT_WIDTH_METERS = 0.876; // 34.5 inch
        public static final double DEFAULT_LENGTH_METERS = 0.876; // 34.5 inch
        public static final double DEFAULT_MAX_VELOCITY_METERS_PER_SECOND = 4.172; // calculated from Choreo (Kraken x60 motor, 6.12 gear ratio, 55kg robot mass)
        public static final double DEFAULT_MAX_ACCELERATION_METERS_PER_SQUARED_SECOND = 10.184; // calculated from Choreo (Kraken x60 motor, 6.12 gear ratio, 55kg robot mass)
        public static final double DEFAULT_MAX_ANGULAR_VELOCITY_DEGREES_PER_SECOND = 540;
    }

    public static final class SwerveModuleConfigs {
        public static final double MINIMUM_USAGE_SPEED_METERS_PER_SECOND = ChassisConfigs.DEFAULT_MAX_VELOCITY_METERS_PER_SECOND * 0.03;
        public static final double NON_USAGE_TIME_RESET_SWERVE = 0.5;

        public static final MapleSimplePIDController.SimplePIDProfile steerHeadingCloseLoop = new MapleSimplePIDController.SimplePIDProfile(
                0.9,
                Math.toRadians(65),
                0.01,
                Math.toRadians(1.5),
                0.05,
                true
        );
        public static final double STEERING_CURRENT_LIMIT = 20;
        public static final double DRIVING_CURRENT_LIMIT = 60;
    }

    public static final class WheelCalibrationConfigs {
        public static final WheelsCalibration.WheelToBeCalibrated[] wheelsToBeCalibrated = new WheelsCalibration.WheelToBeCalibrated[] {
                new WheelsCalibration.WheelToBeCalibrated("FrontLeft", 3, 4, 10),
                new WheelsCalibration.WheelToBeCalibrated("FrontRight", 6, 5, 11),
                new WheelsCalibration.WheelToBeCalibrated("BackLeft", 1, 2, 9),
                new WheelsCalibration.WheelToBeCalibrated("BackRight", 8, 7, 12)
        };
    }
}
