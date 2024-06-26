package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Subsystems.MapleSubsystem;


public abstract class SwerveModuleLogic extends MapleSubsystem {
    private final String logPath;
    private SwerveModuleState swerveStateSetPoint;
    private double desiredDrivingPower, desiredSwerveRotation;


    public SwerveModuleLogic(String swerveName) {
        super("Swerve Wheel-" + swerveName);
        this.logPath = "Chassis/ModuleStates/" + swerveName;
    }

    @Override
    public void onReset() {
        this.swerveStateSetPoint = new SwerveModuleState();
        this.desiredDrivingPower = 0;
        this.desiredSwerveRotation = 0;
    }

    @Override
    public void periodic(double dt) {
        // TODO here
    }

    public void requestSetPoint(SwerveModuleState setPoint) {
        this.swerveStateSetPoint = setPoint;
    }

    public SwerveModuleState getSwerveStateSetPoint() {
        return swerveStateSetPoint;
    }
    
    public abstract SwerveModuleState getActualSwerveModuleState();
    abstract SwerveModulePosition getSwerveModulePosition();
}
