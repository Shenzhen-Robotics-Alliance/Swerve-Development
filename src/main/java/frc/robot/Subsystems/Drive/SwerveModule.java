package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.Helpers.MathHelpers.AngleHelpers;
import frc.robot.Helpers.TimeHelpers;
import frc.robot.Subsystems.MapleSubsystem;
import org.littletonrobotics.junction.Logger;


public abstract class SwerveModule extends MapleSubsystem {
    protected final String logPath;
    private SwerveModuleState swerveStateSetPoint;
    protected double calculatedDriveSpeedSetPoint, calculatedSteerSetPoint;


    public SwerveModule(String swerveName) {
        super("Swerve Wheel-" + swerveName);
        this.logPath = "Chassis/Modules/" + swerveName;
    }

    @Override
    public void onReset() {
        this.swerveStateSetPoint = new SwerveModuleState();
        this.calculatedDriveSpeedSetPoint = 0;
        this.calculatedSteerSetPoint = 0;
    }

    double previousMovementTime = 0;
    @Override
    public void periodic(double dt) {
        final double rawSwerveSpeedSetPointHeading = swerveStateSetPoint.angle.getRadians(),
                desiredDriveSpeed = swerveStateSetPoint.speedMetersPerSecond,
                currentSwerveHeading = getActualSwerveModuleState().angle.getRadians();

        if (desiredDriveSpeed > Constants.SwerveModuleConfigs.MINIMUM_USAGE_SPEED_METERS_PER_SECOND) {
            final double swerveSpeedProjectionFactorToCurrentSwerveHeading = Math.cos(AngleHelpers.getActualDifference(currentSwerveHeading, rawSwerveSpeedSetPointHeading));
            this.calculatedDriveSpeedSetPoint = desiredDriveSpeed * swerveSpeedProjectionFactorToCurrentSwerveHeading;
            previousMovementTime = TimeHelpers.getTime();
        } else
            this.calculatedDriveSpeedSetPoint = 0;
        final double swerveFacingSetPoint =
                (TimeHelpers.getTime() - previousMovementTime) < Constants.SwerveModuleConfigs.NON_USAGE_TIME_RESET_SWERVE ?
                        rawSwerveSpeedSetPointHeading :0,
                differenceToRawSetPoint = AngleHelpers.getActualDifference(currentSwerveHeading, swerveFacingSetPoint),
                differenceToReversedSetPoint = AngleHelpers.getActualDifference(currentSwerveHeading, swerveFacingSetPoint + Math.PI);

        if (Math.abs(differenceToReversedSetPoint) - Math.abs(differenceToRawSetPoint) > Math.toRadians(10))
            this.calculatedSteerSetPoint = AngleHelpers.simplifyAngle(swerveFacingSetPoint + Math.PI);
        else
            this.calculatedSteerSetPoint = swerveFacingSetPoint;

        Logger.recordOutput(logPath + "/swerveStateSetpoint", swerveStateSetPoint);
        Logger.recordOutput(logPath + "/calculatedWheelSpeed", calculatedDriveSpeedSetPoint);
        Logger.recordOutput(logPath + "/calculatedSwerveHeadingSetpoint", calculatedSteerSetPoint);
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
