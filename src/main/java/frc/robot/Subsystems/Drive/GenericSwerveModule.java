package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;
import org.littletonrobotics.junction.Logger;

import java.util.ArrayList;
import java.util.List;

public class GenericSwerveModule extends SwerveModuleLogic {
    private final LoggedMotor drivingMotor, steeringMotor;
    private final LoggedRelativePositionEncoder driveEncoder;
    private final LoggedAbsoluteRotationEncoder steerEncoder;

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
    SwerveModulePosition getSwerveModulePosition() {
        return null;
    }

    @Override
    public void onReset() {
        super.onReset();
    }

    @Override
    public void periodic(double dt) {
        super.periodic(dt);
        Logger.recordOutput("drive encoder position", driveEncoder.getLatestPosition());
        Logger.recordOutput("steer encoder rotation (deg)", Math.toDegrees(steerEncoder.getLatestAbsoluteRotationRadian()));
        Logger.recordOutput("steer encoder rotation (rad)", steerEncoder.getLatestAbsoluteRotationRadian());
    }

    public List<TimeStampedEncoderReal> getOdometryEncoders() {
        /* for a generic odometry, we do not update any encoder in the odometer thread */
        return new ArrayList<>();
    }
}