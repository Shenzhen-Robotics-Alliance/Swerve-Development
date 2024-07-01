package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.HardwareIO.Helpers.*;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import frc.robot.Subsystems.Drive.GenericSwerveModule;
import frc.robot.Subsystems.MapleSubsystem;

import java.io.IOException;

public class RobotContainer {
    public static PowerDistribution powerDistribution;
    // private final GenericSwerveModule testSwerveImplement;
    public RobotContainer(String chassisName) {
        if (Robot.mode == Robot.Mode.REAL)
            powerDistribution = new PowerDistribution(0, PowerDistribution.ModuleType.kCTRE);
            // powerDistribution = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
        else
            powerDistribution = new PowerDistribution();
        SmartDashboard.putData("PDP", powerDistribution);

        final MapleConfigFile chassisWheelsCalibrationFile;
        try {
            chassisWheelsCalibrationFile = MapleConfigFile.fromDeployedConfig("ChassisWheelsCalibration", chassisName);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Find Wheels Calibration File For Chassis: " + chassisName + ", because:" + e.getMessage());
        }
        // this.testSwerveImplement = createSwerveModuleCTRE("FrontLeft", chassisWheelsCalibrationFile);

        configureBindings();
    }

    private static GenericSwerveModule createSwerveModuleCTRE(String moduleName, MapleConfigFile calibrationFile) {
        final MapleConfigFile.ConfigBlock configBlock = calibrationFile.getBlock(moduleName);
        final TalonFX drivingTalonFX = new TalonFX(configBlock.getIntConfig("drivingMotorID"), Constants.ChassisConfigs.CHASSIS_CANBUS_NAME),
                steeringTalonFX = new TalonFX(configBlock.getIntConfig("steeringMotorID"), Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);
        final CANcoder steeringCANcoder = new CANcoder(configBlock.getIntConfig("steeringEncoderID"), Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);

        final TalonFXConfiguration driveMotorConfig = new TalonFXConfiguration();
        driveMotorConfig.CurrentLimits.SupplyCurrentLimit = Constants.SwerveModuleConfigs.DRIVING_CURRENT_LIMIT;
        driveMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        drivingTalonFX.getConfigurator().apply(driveMotorConfig, 500);

        final TalonFXConfiguration steerMotorConfig = new TalonFXConfiguration();
        steerMotorConfig.CurrentLimits.SupplyCurrentLimit = Constants.SwerveModuleConfigs.STEERING_CURRENT_LIMIT;
        steerMotorConfig.CurrentLimits.SupplyCurrentLimitEnable = true;
        steeringTalonFX.getConfigurator().apply(steerMotorConfig, 500);

        // TODO: network tables alarm if configuration not successful

        final LoggedMotor
                drivingMotor = HardwareFactory.createMotor(moduleName + "DrivingMotor", drivingTalonFX, false, configBlock.getIntConfig("drivingMotorPortOnPDP")),
                steeringMotor = HardwareFactory.createMotor(
                        moduleName + "SteeringMotor", steeringTalonFX,
                        configBlock.getIntConfig("steeringMotorInverted") != 0,
                        configBlock.getIntConfig("steeringMotorPortOnPDP")
                );
        final LoggedRelativePositionEncoder drivingEncoder = HardwareFactory.createRelativePositionEncoder(moduleName + "DrivingEncoder", drivingTalonFX, false);
        final LoggedAbsoluteRotationEncoder steeringEncoder = HardwareFactory.createAbsoluteRotationEncoder("FrontLeftSteeringEncoder", steeringCANcoder);
        steeringEncoder.setZeroPosition(configBlock.getDoubleConfig("steeringEncoderReadingAtOrigin"));

        return new GenericSwerveModule(moduleName, drivingMotor, steeringMotor, drivingEncoder, steeringEncoder);
    }

    private void configureBindings() {

    }

    public void updateRobot() {
        PeriodicallyUpdatedInputs.updateInputs();
        MapleSubsystem.subsystemsPeriodic();
        CommandScheduler.getInstance().run();
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
