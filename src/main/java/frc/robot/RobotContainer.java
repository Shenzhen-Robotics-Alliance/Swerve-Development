package frc.robot;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.HardwareIO.Helpers.HardwareFactory;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import frc.robot.Subsystems.Drive.GenericSwerveModule;

import java.io.IOException;

public class RobotContainer {
    public static PowerDistribution powerDistribution = new PowerDistribution();
    private final GenericSwerveModule testSwerveImplement;
    public RobotContainer(String chassisName) {
        powerDistribution = new PowerDistribution(0, PowerDistribution.ModuleType.kCTRE);
        // powerDistribution = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
        SmartDashboard.putData("PDP", powerDistribution);

        final MapleConfigFile chassisWheelsCalibrationFile;
        try {
            chassisWheelsCalibrationFile = MapleConfigFile.fromDeployedConfig("ChassisWheelsCalibration", chassisName);
        } catch (IOException e) {
            throw new RuntimeException("Cannot Find Wheels Calibration File For Chassis: " + chassisName + ", because:" + e.getMessage());
        }
        this.testSwerveImplement = createSwerveModuleCTRE("FrontLeft", chassisWheelsCalibrationFile);

        configureBindings();
    }

    private static GenericSwerveModule createSwerveModuleCTRE(String moduleName, MapleConfigFile calibrationFile) {
        final TalonFX drivingTalonFX = new TalonFX(calibrationFile.getIntConfig(moduleName, "drivingMotorID"), Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME),
                steeringTalonFX = new TalonFX(calibrationFile.getIntConfig(moduleName, "steeringMotorID"), Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME);
        final CANcoder steeringCANcoder = new CANcoder(calibrationFile.getIntConfig(moduleName, "steeringEncoderID"), Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME);

        final LoggedMotor
                drivingMotor = HardwareFactory.createMotor(moduleName + "DrivingMotor", drivingTalonFX, false, calibrationFile.getIntConfig(moduleName, "drivingMotorPortOnPDP")),
                steeringMotor = HardwareFactory.createMotor(
                        moduleName + "SteeringMotor", steeringTalonFX,
                        calibrationFile.getIntConfig(moduleName, "steeringMotorTurningDirection") == 0,
                        calibrationFile.getIntConfig(moduleName, "steeringMotorPortOnPDP")
                );
        final LoggedRelativePositionEncoder drivingEncoder = HardwareFactory.createRelativePositionEncoder(moduleName + "DrivingEncoder", drivingTalonFX, false);
        final LoggedAbsoluteRotationEncoder steeringEncoder = HardwareFactory.createAbsoluteRotationEncoder("FrontLeftSteeringEncoder", steeringCANcoder);
        steeringEncoder.setZeroPosition(calibrationFile.getDoubleConfig(moduleName, "steeringEncoderReadingAtOrigin"));
        return new GenericSwerveModule(moduleName, drivingMotor, steeringMotor, drivingEncoder, steeringEncoder);
    }

    private void configureBindings() {

    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
