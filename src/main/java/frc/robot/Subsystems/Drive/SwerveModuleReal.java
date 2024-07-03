package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.DriverStation;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawMotor;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;
import frc.robot.Helpers.MechanismControlHelpers.MapleSimplePIDController;
import org.littletonrobotics.junction.Logger;


public class SwerveModuleReal extends SwerveModule {
    private final double drivingEncoderRevolutionToMetersFactor;
    private final LoggedMotor drivingMotor, steeringMotor;
    private final LoggedRelativePositionEncoder driveEncoder;
    private final LoggedAbsoluteRotationEncoder steerEncoder;
    private final MapleSimplePIDController steerHeadingCloseLoop = new MapleSimplePIDController(
            Constants.SwerveModuleConfigs.steerHeadingCloseLoop, 0
    );

    public SwerveModuleReal(String swerveName, double drivingWheelGearRatio, double wheelRadiusMeters, LoggedMotor drivingMotor, LoggedMotor steeringMotor, LoggedRelativePositionEncoder driveEncoder, LoggedAbsoluteRotationEncoder steerEncoder) {
        super(swerveName);
        this.drivingEncoderRevolutionToMetersFactor = wheelRadiusMeters / drivingWheelGearRatio;
        this.drivingMotor = drivingMotor;
        this.steeringMotor = steeringMotor;
        this.driveEncoder = driveEncoder;
        this.steerEncoder = steerEncoder;
    }

    @Override
    public SwerveModuleState getActualSwerveModuleState() {
        return new SwerveModuleState(driveEncoder.getVelocity() * drivingEncoderRevolutionToMetersFactor, Rotation2d.fromRadians(steerEncoder.getLatestAbsoluteRotationRadian()));
    }

    @Override
    public SwerveModulePosition getLatestSwerveModulePosition() {
        return getSwerveModulePosition(driveEncoder.getLatestPosition(), steerEncoder.getLatestAbsoluteRotationRadian());
    }

    private SwerveModulePosition[] cachedSwerveModulePositions = new SwerveModulePosition[0];
    @Override
    public SwerveModulePosition[] getCachedSwerveModulePositions() {
        return cachedSwerveModulePositions;
    }

    private SwerveModulePosition getSwerveModulePosition(double drivingEncoderReadingRevolutions, double steeringEncoderReadingRadians) {
        return new SwerveModulePosition(drivingEncoderReadingRevolutions * drivingEncoderRevolutionToMetersFactor, Rotation2d.fromRadians(steeringEncoderReadingRadians));
    }

    @Override
    public void onReset() {
        super.onReset();
        driveEncoder.setCurrentPositionAs(0);
    }

    @Override
    public void periodic(double dt, boolean enabled) {
        super.periodic(dt, enabled);

        updateSwervePositionsFromCachedEncoderData();

        steerHeadingCloseLoop.setDesiredPosition(super.calculatedSteeringSetPoint);
        if (enabled) steeringMotor.setMotorPower(steerHeadingCloseLoop.getMotorPower(steerEncoder.getAngularVelocity(), steerEncoder.getLatestAbsoluteRotationRadian()));
        Logger.recordOutput(super.logPath + "/steer closed loop power", steerHeadingCloseLoop.getMotorPower(steerEncoder.getAngularVelocity(), steerEncoder.getLatestAbsoluteRotationRadian()));

        // TODO wheel speed feed-forward
        if (enabled) drivingMotor.setMotorPower(super.calculatedDriveSpeedSetPoint / Constants.ChassisConfigs.DEFAULT_MAX_VELOCITY_METERS_PER_SECOND);

        Logger.recordOutput(super.logPath + "/drive encoder position", driveEncoder.getLatestPosition());
        Logger.recordOutput(super.logPath + "/steer encoder rotation (deg)", Math.toDegrees(steerEncoder.getLatestAbsoluteRotationRadian()));
        Logger.recordOutput(super.logPath + "/steer encoder rotation (rad)", steerEncoder.getLatestAbsoluteRotationRadian());
    }

    private void updateSwervePositionsFromCachedEncoderData() {
        final double[] steerEncoderReadings = steerEncoder.getAbsoluteRotations(),
                driveEncoderReadings = driveEncoder.getPositions();
        if (steerEncoderReadings.length != driveEncoderReadings.length) {
            DriverStation.reportWarning("The length of the cached inputs of the steering(" + steerEncoderReadings.length + ") and driving(" + driveEncoderReadings.length + ") encoders on the same module appears to be unequal", false);
            return;
        }

        cachedSwerveModulePositions = new SwerveModulePosition[steerEncoder.getAbsoluteRotations().length];
        for (int i = 0; i < cachedSwerveModulePositions.length; i++)
            cachedSwerveModulePositions[i] = getSwerveModulePosition(driveEncoder.getPositions()[i], steerEncoder.getAbsoluteRotations()[i]);
    }

    @Override
    public void onEnable() {
        steeringMotor.rawMotor.configureZeroPowerBehavior(RawMotor.ZeroPowerBehavior.BREAK);
        drivingMotor.rawMotor.configureZeroPowerBehavior(RawMotor.ZeroPowerBehavior.BREAK);
    }

    @Override
    public void onDisable() {
        steeringMotor.relax();
        drivingMotor.relax();
    }
}