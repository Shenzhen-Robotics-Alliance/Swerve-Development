package frc.robot;

import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.HardwareIO.Helpers.*;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import frc.robot.Subsystems.Drive.OdometryThread;
import frc.robot.Subsystems.Drive.SwerveModuleReal;
import frc.robot.Subsystems.MapleSubsystem;
import org.littletonrobotics.junction.Logger;

import java.io.IOException;

public class RobotContainer {
    public static PowerDistribution powerDistribution;
    private final SwerveModuleReal testSwerveImplement;
    private final OdometryThread odometryThread;
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
        this.testSwerveImplement = createSwerveModuleCTRE("FrontLeft", chassisWheelsCalibrationFile);

        this.odometryThread = OdometryThread.getInstance();
        odometryThread.start();

        configureBindings();
    }

    private static SwerveModuleReal createSwerveModuleCTRE(String moduleName, MapleConfigFile calibrationFile) {
        final MapleConfigFile.ConfigBlock currentModuleBlock = calibrationFile.getBlock(moduleName),
                generalBlock = calibrationFile.getBlock("GeneralInformation");
        final TalonFX drivingTalonFX = new TalonFX(currentModuleBlock.getIntConfig("drivingMotorID"), Constants.ChassisConfigs.CHASSIS_CANBUS_NAME),
                steeringTalonFX = new TalonFX(currentModuleBlock.getIntConfig("steeringMotorID"), Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);
        final CANcoder steeringCANcoder = new CANcoder(currentModuleBlock.getIntConfig("steeringEncoderID"), Constants.ChassisConfigs.CHASSIS_CANBUS_NAME);

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
                drivingMotor = HardwareFactory.createMotor(moduleName + "DrivingMotor", drivingTalonFX, false, currentModuleBlock.getIntConfig("drivingMotorPortOnPDP")),
                steeringMotor = HardwareFactory.createMotor(
                        moduleName + "SteeringMotor", steeringTalonFX,
                        currentModuleBlock.getIntConfig("steeringMotorInverted") != 0,
                        currentModuleBlock.getIntConfig("steeringMotorPortOnPDP")
                );
        final LoggedRelativePositionEncoder drivingEncoder = HardwareFactory.createRelativePositionEncoderOnOdometry(moduleName + "DrivingEncoder", drivingTalonFX, false);
        final LoggedAbsoluteRotationEncoder steeringEncoder = HardwareFactory.createAbsoluteRotationEncoderOnOdometry(moduleName + "SteeringEncoder", steeringCANcoder);
        steeringEncoder.setZeroPosition(currentModuleBlock.getDoubleConfig("steeringEncoderReadingAtOrigin"));

        return new SwerveModuleReal(moduleName, generalBlock.getDoubleConfig("overallGearRatio"), generalBlock.getDoubleConfig("wheelRadiusMeters"), drivingMotor, steeringMotor, drivingEncoder, steeringEncoder);
    }

    private void configureBindings() {

    }

    public void updateRobot() {
        PeriodicallyUpdatedInputs.updateInputs();
        MapleSubsystem.checkForOnDisableAndEnable();
        CommandScheduler.getInstance().run();
    }

    @Deprecated
    public void testUnitFeatures() {
        SwerveDriveKinematics swerveDriveKinematics = new SwerveDriveKinematics(
                new Translation2d(1, 1),
                new Translation2d(1,-1)
        );
        XboxController xboxController = new XboxController(1);
        this.testSwerveImplement.requestSetPoint(swerveDriveKinematics.toWheelSpeeds(new ChassisSpeeds(xboxController.getRightX() * 3, -xboxController.getRightY() * 3, xboxController.getLeftX() * 3)).states[0]);
        Logger.recordOutput("/Odometer/TimeStamps", odometryThread.getOdometerTimeStampsSincePreviousRobotPeriod());
        Logger.recordOutput("/Odometer/Cached Swerve Positions", testSwerveImplement.getCachedSwerveModulePositions());
    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
