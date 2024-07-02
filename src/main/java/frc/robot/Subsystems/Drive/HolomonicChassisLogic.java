package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;
import frc.robot.Subsystems.MapleSubsystem;

import java.util.List;

public abstract class HolomonicChassisLogic extends MapleSubsystem {
    protected HolomonicChassisLogic() {
        super("Chassis");
    }

    @Override
    public void onReset() {

    }

    @Override
    public void periodic(double dt, boolean enabled) {

    }

    protected abstract void driveChassis(ChassisSpeeds chassisSpeeds);
    protected abstract Pose2d getRobotPose2d();
    protected abstract void setCurrentPose2d(Pose2d currentPose2d);
}
