package frc.robot;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.HardwareIO.Helpers.HardwareFactory;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;
import frc.robot.HardwareIO.VendorImplements.CTRE.CanCoderEncoderImpl;
import frc.robot.HardwareIO.VendorImplements.CTRE.TalonFXMotorAndEncoderImpl;
import frc.robot.Subsystems.Drive.GenericSwerveModule;

public class RobotContainer {
    public static PowerDistribution powerDistribution = new PowerDistribution();
    private final GenericSwerveModule testSwerveImplement;
    public RobotContainer() {
        powerDistribution = new PowerDistribution(0, PowerDistribution.ModuleType.kCTRE);
        // powerDistribution = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);

        final TalonFX frontLeftDriving = new TalonFX(3, "ChassisCanivore"),
                frontLeftSteering = new TalonFX(4, "ChassisCanivore");
        final CANcoder frontLeftEncoder = new CANcoder(10, "ChassisCanivore");

        this.testSwerveImplement = new GenericSwerveModule(
                "FrontLeft",
                HardwareFactory.createMotor("FrontLeftDrivingMotor", frontLeftDriving, false, 1),
                HardwareFactory.createMotor("FrontLeftSteeringMotor", frontLeftSteering, true, 1),
                HardwareFactory.createRelativePositionEncoderThreaded("FrontLeftDrivingEncoder", frontLeftDriving, false),
                HardwareFactory.createAbsoluteRotationEncoderThreaded("FrontLeftSteeringEncoder", frontLeftEncoder)
        );

        configureBindings();
    }

    private void configureBindings() {

    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
