package frc.robot.UnitTests;

import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.wpilibj.XboxController;
import org.littletonrobotics.junction.Logger;

public class SwerveStateTest implements UnitTest {
    private final XboxController xboxController = new XboxController(0);
    SwerveDriveKinematics swerveDriveKinematics;
    @Override
    public void testStart() {
        swerveDriveKinematics = new SwerveDriveKinematics(
                new Translation2d(1, 1),
                new Translation2d(1, -1),
                new Translation2d(-1, 1),
                new Translation2d(-1, -1)
        );
    }

    @Override
    public void testPeriodic() {
        Logger.recordOutput("Kinematics Output",
                swerveDriveKinematics.toSwerveModuleStates(
                        new ChassisSpeeds(xboxController.getRightX(), -xboxController.getRightY(), -xboxController.getLeftX()) // TODO: figure this out
                ));
    }
}
