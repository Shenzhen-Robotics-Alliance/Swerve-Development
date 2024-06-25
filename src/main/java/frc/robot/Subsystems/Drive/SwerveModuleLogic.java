package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.Subsystems.MapleSubsystem;
import org.littletonrobotics.junction.Logger;


public abstract class SwerveModuleLogic extends MapleSubsystem {
    protected SwerveModuleState setPoint;
    private final String logPath;

    public SwerveModuleLogic(String swerveName) {
        super("Swerve Wheel-" + swerveName);
        this.logPath = "Chassis/ModuleStates/" + swerveName;
    }

    @Override
    public void onReset() {
        this.setPoint = new SwerveModuleState();
    }

    @Override
    public void periodic(double dt) {
        Logger.recordOutput(logPath + "/setPoint", setPoint);
        Logger.recordOutput(logPath + "/actualState", getActualSwerveModuleState());
        Logger.recordOutput(logPath + "/position", getSwerveModulePosition());
    }

    public void requestSetPoint(SwerveModuleState setPoint) {
        this.setPoint = setPoint;
    }

    public SwerveModuleState getSetPoint() {
        return setPoint;
    }
    
    public abstract SwerveModuleState getActualSwerveModuleState();
    abstract SwerveModulePosition getSwerveModulePosition();
}
