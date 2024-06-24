package frc.robot.Subsystems.Drive;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class SwerveModule extends SubsystemBase {
    @Override
    public void periodic() {
        if (DriverStation.isDisabled())
            return;
        System.out.println("swerve module periodic coming through...");
    }
}
