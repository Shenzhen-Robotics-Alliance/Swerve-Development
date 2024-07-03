package frc.robot.UnitTests;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.NeutralModeValue;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Constants;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import frc.robot.Helpers.MathHelpers.AngleHelpers;

public class WheelsCalibration implements UnitTest {
    public static final class WheelToBeCalibrated {
        public final String name;
        public final int drivingMotorID, steeringMotorID, encoderID, drivingMotorPortOnPDP, steeringMotorPortOnPDP;
        public boolean steeringMotorInverted;

        public WheelToBeCalibrated(String name, int drivingMotorID, int steeringMotorID, int encoderID) {
            this(name, drivingMotorID, steeringMotorID, encoderID, -1, -1);
        }

        private WheelToBeCalibrated(String name, int drivingMotorID, int steeringMotorID, int encoderID, int drivingMotorPortOnPDP, int steeringMotorPortOnPDP) {
            this.name = name;
            this.drivingMotorID = drivingMotorID;
            this.steeringMotorID = steeringMotorID;
            this.encoderID = encoderID;
            this.drivingMotorPortOnPDP = drivingMotorPortOnPDP;
            this.steeringMotorPortOnPDP = steeringMotorPortOnPDP;
            this.steeringMotorInverted = false;
        }
    }

    private enum SteerWheelTurningDirection {
        NOT_INVERTED,
        INVERTED
    }
    private final SendableChooser<SteerWheelTurningDirection> wheelTurningDirectionSendableChooser = new SendableChooser<>();
    private final SendableChooser<WheelToBeCalibrated> wheelSendableChooser = new SendableChooser<>();
    private final String configName;
    private final MapleConfigFile calibrationFile;
    public WheelsCalibration() {
        configName = "5516-2024-OnSeason";
        for (WheelToBeCalibrated wheelToBeCalibrated:Constants.WheelCalibrationConfigs.wheelsToBeCalibrated)
            wheelSendableChooser.addOption(wheelToBeCalibrated.name, wheelToBeCalibrated);
        wheelSendableChooser.setDefaultOption(Constants.WheelCalibrationConfigs.wheelsToBeCalibrated[0].name, Constants.WheelCalibrationConfigs.wheelsToBeCalibrated[0]);
        wheelSendableChooser.onChange(this::initHardware);

        SmartDashboard.putData("Select Wheel to Calibrate", wheelSendableChooser);

        this.calibrationFile = new MapleConfigFile("ChassisWheelsCalibration", configName);

        wheelTurningDirectionSendableChooser.setDefaultOption(SteerWheelTurningDirection.NOT_INVERTED.name(), SteerWheelTurningDirection.NOT_INVERTED);
        wheelTurningDirectionSendableChooser.addOption(SteerWheelTurningDirection.INVERTED.name(), SteerWheelTurningDirection.INVERTED);
    }

    @Override
    public void testStart() {
        initHardware(wheelSendableChooser.getSelected());
        SmartDashboard.putData("Steer Motor Turning Direction (Should be Spinning Counter-Clockwise)", wheelTurningDirectionSendableChooser);
        SmartDashboard.putData("Calibration/moveDrivingWheel", new InstantCommand(
                () -> drivingMotor.set(0.3)).finallyDo(() -> drivingMotor.set(0))
        );
        SmartDashboard.putData("Calibration/moveSteeringWheel", new InstantCommand(
                () -> steeringMotor.set(wheelSendableChooser.getSelected().steeringMotorInverted ? -0.2:0.2)).finallyDo(() -> steeringMotor.set(0))
        );

        SmartDashboard.putData("Calibration/save", new InstantCommand(this::writeConfigurationFile));
        finished = false;
    }


    private TalonFX drivingMotor, steeringMotor;
    private CANcoder canCoder;
    private boolean finished;
    @Override
    public void testPeriodic() {
        if (finished) return;
        wheelSendableChooser.getSelected().steeringMotorInverted = switch (wheelTurningDirectionSendableChooser.getSelected()){
            case NOT_INVERTED -> false;
            case INVERTED -> true;
        };

        SmartDashboard.putNumber("Can Coder Reading (Rad)", getCanCoderReadingRadian(canCoder));
    }

    private double getCanCoderReadingRadian(CANcoder canCoder) {
        return AngleHelpers.simplifyAngle(canCoder.getAbsolutePosition().getValue() * Math.PI * 2);
    }

    private void writeConfigurationFile() {
        if (finished) return;
        final MapleConfigFile.ConfigBlock configBlock = calibrationFile.getBlock("GeneralInformation");
        configBlock.putIntConfig("gyroPort", Constants.ChassisConfigs.DEFAULT_GYRO_PORT);
        configBlock.putDoubleConfig("overallGearRatio", Constants.ChassisConfigs.DEFAULT_GEAR_RATIO);
        configBlock.putDoubleConfig("wheelRadiusMeters", Constants.ChassisConfigs.DEFAULT_WHEEL_RADIUS_METERS);
        configBlock.putDoubleConfig("bumperWidthMeters", Constants.ChassisConfigs.DEFAULT_BUMPER_WIDTH_METERS);
        configBlock.putDoubleConfig("bumperLengthMeters", Constants.ChassisConfigs.DEFAULT_BUMPER_LENGTH_METERS);
        configBlock.putDoubleConfig("leftRightWheelsDistanceMeters", Constants.ChassisConfigs.DEFAULT_LEFT_RIGHT_WHEELS_DISTANCE_METERS);
        configBlock.putDoubleConfig("frontBackWheelsDistanceMeters", Constants.ChassisConfigs.DEFAULT_FRONT_BACK_WHEELS_DISTANCE_METERS);
        configBlock.putDoubleConfig("maxVelocityMetersPerSecond", Constants.ChassisConfigs.DEFAULT_MAX_VELOCITY_METERS_PER_SECOND);
        configBlock.putDoubleConfig("maxAccelerationMetersPerSecondSquared", Constants.ChassisConfigs.DEFAULT_MAX_ACCELERATION_METERS_PER_SQUARED_SECOND);
        configBlock.putDoubleConfig("maxAngularVelocityRadiansPerSecond", Math.toRadians(Constants.ChassisConfigs.DEFAULT_MAX_ANGULAR_VELOCITY_DEGREES_PER_SECOND));

        for (WheelToBeCalibrated wheelToBeCalibrated:Constants.WheelCalibrationConfigs.wheelsToBeCalibrated)
            saveConfigurationForCurrentWheel(wheelToBeCalibrated);

        calibrationFile.saveConfigToUSBSafe();
        finished = true;
    }

    private void saveConfigurationForCurrentWheel(WheelToBeCalibrated wheelToBeCalibrated) {
        final MapleConfigFile.ConfigBlock configBlock = calibrationFile.getBlock(wheelToBeCalibrated.name);
        configBlock.putIntConfig("drivingMotorID", wheelToBeCalibrated.drivingMotorID);
        configBlock.putIntConfig("drivingMotorPortOnPDP", wheelToBeCalibrated.drivingMotorPortOnPDP);
        configBlock.putIntConfig("steeringMotorID", wheelToBeCalibrated.steeringMotorID);
        configBlock.putIntConfig("steeringMotorPortOnPDP", wheelToBeCalibrated.steeringMotorPortOnPDP);
        configBlock.putIntConfig("steeringEncoderID", wheelToBeCalibrated.encoderID);

        configBlock.putIntConfig("steeringMotorInverted", wheelToBeCalibrated.steeringMotorInverted ? 1 : 0);

        final CANcoder canCoder = new CANcoder(wheelToBeCalibrated.encoderID, Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);
        configBlock.putDoubleConfig("steeringEncoderReadingAtOrigin", getCanCoderReadingRadian(canCoder));
    }

    private void initHardware(WheelToBeCalibrated wheelToBeCalibrated) {
        drivingMotor = new TalonFX(wheelToBeCalibrated.drivingMotorID, Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);
        drivingMotor.setNeutralMode(NeutralModeValue.Coast);
        steeringMotor = new TalonFX(wheelToBeCalibrated.steeringMotorID, Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);
        steeringMotor.setNeutralMode(NeutralModeValue.Coast);
        steeringMotor.setInverted(false);
        canCoder = new CANcoder(wheelToBeCalibrated.encoderID, Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);
    }
}
