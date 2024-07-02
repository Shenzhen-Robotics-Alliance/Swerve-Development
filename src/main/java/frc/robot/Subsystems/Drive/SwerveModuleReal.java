package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawMotor;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;
import frc.robot.Helpers.MechanismControlHelpers.MapleSimplePIDController;
import org.littletonrobotics.junction.Logger;


public class SwerveModuleReal extends SwerveModule {
    private final LoggedMotor drivingMotor, steeringMotor;
    private final LoggedRelativePositionEncoder driveEncoder;
    private final LoggedAbsoluteRotationEncoder steerEncoder;
    private final MapleSimplePIDController steerHeadingCloseLoop = new MapleSimplePIDController(
            Constants.SwerveModuleConfigs.steerHeadingCloseLoop, 0
    );

    public SwerveModuleReal(String swerveName, LoggedMotor drivingMotor, LoggedMotor steeringMotor, LoggedRelativePositionEncoder driveEncoder, LoggedAbsoluteRotationEncoder steerEncoder) {
        super(swerveName);
        this.drivingMotor = drivingMotor;
        this.steeringMotor = steeringMotor;
        this.driveEncoder = driveEncoder;
        this.steerEncoder = steerEncoder;
    }

    @Override
    public SwerveModuleState getActualSwerveModuleState() {
        return new SwerveModuleState(driveEncoder.getVelocity(), Rotation2d.fromRadians(steerEncoder.getLatestAbsoluteRotationRadian()));
    }

    @Override
    SwerveModulePosition getLatestSwerveModulePosition() {
        return null;
    }

    @Override
    SwerveModulePosition[] getCachedSwerveModulePositions() {
        return new SwerveModulePosition[0];
    }

    @Override
    public void onReset() {
        super.onReset();
    }

    @Override
    public void periodic(double dt, boolean enabled) {
        super.periodic(dt, enabled);

        steerHeadingCloseLoop.setDesiredPosition(super.calculatedSteeringSetPoint);
        if (enabled) steeringMotor.setMotorPower(steerHeadingCloseLoop.getMotorPower(steerEncoder.getAngularVelocity(), steerEncoder.getLatestAbsoluteRotationRadian()));
        Logger.recordOutput(super.logPath + "/steer closed loop power", steerHeadingCloseLoop.getMotorPower(steerEncoder.getAngularVelocity(), steerEncoder.getLatestAbsoluteRotationRadian()));

        // TODO wheel speed feed-forward
        if (enabled) drivingMotor.setMotorPower(super.calculatedDriveSpeedSetPoint / Constants.ChassisConfigs.DEFAULT_MAX_VELOCITY_METERS_PER_SECOND);

        Logger.recordOutput(super.logPath + "/drive encoder position", driveEncoder.getLatestPosition());
        Logger.recordOutput(super.logPath + "/steer encoder rotation (deg)", Math.toDegrees(steerEncoder.getLatestAbsoluteRotationRadian()));
        Logger.recordOutput(super.logPath + "/steer encoder rotation (rad)", steerEncoder.getLatestAbsoluteRotationRadian());
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