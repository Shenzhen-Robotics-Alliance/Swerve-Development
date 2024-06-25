package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.HardwareIO.Helpers.LoggedAbsoluteRotationEncoder;
import frc.robot.HardwareIO.Helpers.LoggedMotor;
import frc.robot.HardwareIO.Helpers.LoggedRelativePositionEncoder;

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
        return null;
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
    }
}
