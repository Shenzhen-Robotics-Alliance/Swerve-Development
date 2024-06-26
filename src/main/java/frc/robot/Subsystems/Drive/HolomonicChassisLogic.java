package frc.robot.Subsystems.Drive;

import com.ctre.phoenix6.BaseStatusSignal;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Constants;
import frc.robot.HardwareIO.Helpers.ThreadedEncoder;
import frc.robot.Subsystems.MapleSubsystem;

import java.util.ArrayList;
import java.util.List;

public abstract class HolomonicChassisLogic extends MapleSubsystem {
    private final OdometryThread odometryThread;
    protected HolomonicChassisLogic(List<ThreadedEncoder> odometryEncoders, boolean isOdometryTimeSynced) {
        super("Chassis");
        this.odometryThread = new OdometryThread(odometryEncoders, isOdometryTimeSynced);
        this.odometryThread.start();
    }

    @Override
    public void onReset() {

    }

    @Override
    public void periodic(double dt) {

    }

    protected abstract void driveChassis(ChassisSpeeds chassisSpeeds);
}
