package frc.robot;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.HardwareIO.Helpers.HardwareFactory;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.Subsystems.Drive.GenericSwerveModule;

public class RobotContainer {
    public static PowerDistribution powerDistribution = new PowerDistribution();
    private final GenericSwerveModule testSwerveImplement;
    public RobotContainer() {
        powerDistribution = new PowerDistribution(0, PowerDistribution.ModuleType.kCTRE);
        // powerDistribution = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);

        SmartDashboard.putData("PDP", powerDistribution);

        final TalonFX frontLeftDriving = new TalonFX(3, "ChassisCanivore"),
                frontLeftSteering = new TalonFX(4, "ChassisCanivore");
        final CANcoder frontLeftEncoder = new CANcoder(10, "ChassisCanivore");

        LoggedAbsoluteRotationEncoder absoluteRotationEncoder = HardwareFactory.createAbsoluteRotationEncoder("FrontLeftSteeringEncoder", frontLeftEncoder);
        absoluteRotationEncoder.setZeroPosition(-2.91);
        this.testSwerveImplement = new GenericSwerveModule(
                "FrontLeft",
                HardwareFactory.createMotor("FrontLeftDrivingMotor", frontLeftDriving, false, 1),
                HardwareFactory.createMotor("FrontLeftSteeringMotor", frontLeftSteering, true, 1),
                HardwareFactory.createRelativePositionEncoder("FrontLeftDrivingEncoder", frontLeftDriving, false),
                absoluteRotationEncoder
        );

        configureBindings();
    }

    private void configureBindings() {

    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
