package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import frc.robot.Helpers.TimeHelpers;
import frc.robot.Subsystems.MapleSubsystem;


public abstract class SwerveModuleLogic extends MapleSubsystem {
    private final String logPath;
    private SwerveModuleState swerveStateSetPoint;
    private double driveSpeed, swerveHeadingSetPoint;


    public SwerveModuleLogic(String swerveName) {
        super("Swerve Wheel-" + swerveName);
        this.logPath = "Chassis/ModuleStates/" + swerveName;
    }

    @Override
    public void onReset() {
        this.swerveStateSetPoint = new SwerveModuleState();
        this.driveSpeed = 0;
        this.swerveHeadingSetPoint = 0;
    }

    double previousMovementTime = 0;
    @Override
    public void periodic(double dt) {
        final SwerveModuleState currentState = getActualSwerveModuleState();
        final double rawSwerveSpeedHeading = currentState.angle.getRadians(),
                desiredDriveSpeed = currentState.speedMetersPerSecond,
                currentSwerveHeading = getActualSwerveModuleState().angle.getRadians();

        if (desiredDriveSpeed > Constants.SwerveModuleConfigs.MINIMUM_USAGE_SPEED)
            previousMovementTime = TimeHelpers.getTime();
        final double rawSwerveHeadingSetPoint =
                TimeHelpers.getTime() - previousMovementTime > Constants.SwerveModuleConfigs.NON_USAGE_TIME_RESET_SWERVE ?
                        0:rawSwerveSpeedHeading,
                differenceToRawSetPoint = AngleHelpers.getActualDifference(currentSwerveHeading, rawSwerveHeadingSetPoint),
                differenceToReversedSetPoint = AngleHelpers.getActualDifference(currentSwerveHeading, rawSwerveHeadingSetPoint + Math.PI);

        if (Math.abs(differenceToReversedSetPoint) - Math.abs(differenceToRawSetPoint) > Math.toRadians(10))
            this.swerveHeadingSetPoint = AngleHelpers.simplifyAngle(rawSwerveHeadingSetPoint + Math.PI);
        else
            this.swerveHeadingSetPoint = rawSwerveHeadingSetPoint;
    }

    public void requestSetPoint(SwerveModuleState setPoint) {
        this.swerveStateSetPoint = setPoint;
    }

    public SwerveModuleState getSwerveStateSetPoint() {
        return swerveStateSetPoint;
    }

    public abstract SwerveModuleState getActualSwerveModuleState();
    abstract SwerveModulePosition getLatestSwerveModulePosition();

    abstract SwerveModulePosition[] getCachedSwerveModulePositions();
}
