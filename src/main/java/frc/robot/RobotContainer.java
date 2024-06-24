package frc.robot;

import edu.wpi.first.wpilibj.PowerDistribution;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import frc.robot.Subsystems.Drive.SwerveModule;

public class RobotContainer {
    public static PowerDistribution powerDistribution = new PowerDistribution();
    public RobotContainer() {
        powerDistribution = new PowerDistribution(0, PowerDistribution.ModuleType.kCTRE);
        // powerDistribution = new PowerDistribution(1, PowerDistribution.ModuleType.kRev);
        configureBindings();

        new SwerveModule();
    }

    private void configureBindings() {

    }

    public Command getAutonomousCommand() {
        return Commands.print("No autonomous command configured");
    }
}
