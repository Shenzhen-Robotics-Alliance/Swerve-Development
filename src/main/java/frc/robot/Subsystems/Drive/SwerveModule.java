package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import frc.robot.Helpers.TimeHelpers;
import frc.robot.Subsystems.MapleSubsystem;


public abstract class SwerveModule extends MapleSubsystem {
    private final String logPath;
    private SwerveModuleState swerveStateSetPoint;
    protected double calculatedDriveSpeedSetPoint, calculatedWheelHeadingSetPoint;


    public SwerveModule(String swerveName) {
        super("Swerve Wheel-" + swerveName);
        this.logPath = "Chassis/ModuleStates/" + swerveName;
    }

    @Override
    public void onReset() {
        this.swerveStateSetPoint = new SwerveModuleState();
        this.calculatedDriveSpeedSetPoint = 0;
        this.calculatedWheelHeadingSetPoint = 0;
    }

    double previousMovementTime = 0;
    @Override
    public void periodic(double dt) {
        final SwerveModuleState currentState = getActualSwerveModuleState();
        final double rawSwerveSpeedHeading = currentState.angle.getRadians(),
                desiredDriveSpeed = currentState.speedMetersPerSecond,
                currentSwerveHeading = getActualSwerveModuleState().angle.getRadians();

        if (desiredDriveSpeed > Constants.SwerveModuleConfigs.MINIMUM_USAGE_SPEED_METERS_PER_SECOND) {
            final double swerveSpeedProjectionFactorToCurrentSwerveHeading = Math.cos(AngleHelpers.getActualDifference(currentSwerveHeading, rawSwerveSpeedHeading));
            this.calculatedDriveSpeedSetPoint = desiredDriveSpeed * swerveSpeedProjectionFactorToCurrentSwerveHeading;
            previousMovementTime = TimeHelpers.getTime();
        } else
            this.calculatedDriveSpeedSetPoint = 0;
        final double rawSwerveHeadingSetPoint =
                TimeHelpers.getTime() - previousMovementTime > Constants.SwerveModuleConfigs.NON_USAGE_TIME_RESET_SWERVE ?
                        0:rawSwerveSpeedHeading,
                differenceToRawSetPoint = AngleHelpers.getActualDifference(currentSwerveHeading, rawSwerveHeadingSetPoint),
                differenceToReversedSetPoint = AngleHelpers.getActualDifference(currentSwerveHeading, rawSwerveHeadingSetPoint + Math.PI);

        if (Math.abs(differenceToReversedSetPoint) - Math.abs(differenceToRawSetPoint) > Math.toRadians(10))
            this.calculatedWheelHeadingSetPoint = AngleHelpers.simplifyAngle(rawSwerveHeadingSetPoint + Math.PI);
        else
            this.calculatedWheelHeadingSetPoint = rawSwerveHeadingSetPoint;
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
