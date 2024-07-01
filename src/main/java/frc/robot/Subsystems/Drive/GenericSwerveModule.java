package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Constants;
import frc.robot.HardwareIO.Abstractions.RawMotor;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;
import frc.robot.Helpers.MechanismControlHelpers.MapleSimplePIDController;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;

public class GenericSwerveModule extends SwerveModule {
    private final LoggedMotor drivingMotor, steeringMotor;
    private final LoggedRelativePositionEncoder driveEncoder;
    private final LoggedAbsoluteRotationEncoder steerEncoder;
    private final MapleSimplePIDController steerHeadingCloseLoop = new MapleSimplePIDController(
            Constants.SwerveModuleConfigs.steerHeadingCloseLoop, 0
    );

    public GenericSwerveModule(String swerveName, LoggedMotor drivingMotor, LoggedMotor steeringMotor, LoggedRelativePositionEncoder driveEncoder, LoggedAbsoluteRotationEncoder steerEncoder) {
        super(swerveName);
        this.drivingMotor = drivingMotor;
        this.steeringMotor = steeringMotor;
        this.driveEncoder = driveEncoder;
        this.steerEncoder = steerEncoder;
    }

    @Override
    public SwerveModuleState getActualSwerveModuleState() {
        return new SwerveModuleState();
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
    public void periodic(double dt) {
        super.periodic(dt);


        steerHeadingCloseLoop.setDesiredPosition(super.calculatedSteerSetPoint);
        steeringMotor.setMotorPower(steerHeadingCloseLoop.getMotorPower(steerEncoder.getAngularVelocity(), steerEncoder.getLatestAbsoluteRotationRadian()));
        Logger.recordOutput(super.logPath + "/steer closed loop power", steerHeadingCloseLoop.getMotorPower(steerEncoder.getAngularVelocity(), steerEncoder.getLatestAbsoluteRotationRadian()));
        // TODO wheel speed feed-forward
        drivingMotor.setMotorPower(super.calculatedDriveSpeedSetPoint / Constants.ChassisConfigs.DEFAULT_MAX_VELOCITY_METERS_PER_SECOND);

        Logger.recordOutput(super.logPath + "/drive encoder position", driveEncoder.getLatestPosition()); // TODO: swerve position not right
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
        steeringMotor.rawMotor.configureZeroPowerBehavior(RawMotor.ZeroPowerBehavior.RELAX);
        drivingMotor.rawMotor.configureZeroPowerBehavior(RawMotor.ZeroPowerBehavior.RELAX);
    }

    public List<TimeStampedEncoderReal> getOdometryEncoders() {
        /* for a generic odometry, we do not update any encoder in the odometer thread */
        return new ArrayList<>();
    }
}