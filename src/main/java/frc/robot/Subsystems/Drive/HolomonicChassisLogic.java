package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.HardwareIO.Helpers.TimeStampedEncoderReal;
import frc.robot.Subsystems.MapleSubsystem;

import java.util.List;

public abstract class HolomonicChassisLogic extends MapleSubsystem {
    private final OdometryThread odometryThread;
    protected HolomonicChassisLogic(List<TimeStampedEncoderReal> odometryEncoders) {
        super("Chassis");
        this.odometryThread = new OdometryThread(odometryEncoders);
        this.odometryThread.start();
    }

    @Override
    public void onReset() {

    }

    @Override
    public void periodic(double dt) {

    }

    protected abstract void driveChassis(ChassisSpeeds chassisSpeeds);
    protected abstract Pose2d getRobotPose2d();
    protected abstract void setCurrentPose2d(Pose2d currentPose2d);
}
