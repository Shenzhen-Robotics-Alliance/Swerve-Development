package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public abstract class SwerveModuleLogic extends SubsystemBase {
    protected  SwerveModuleState setPoint;
    public void requestSetPoint(SwerveModuleState setPoint) {
        this.setPoint = setPoint;
    }

    public SwerveModuleState getSetPoint() {
        return setPoint;
    }
    
    public abstract SwerveModuleState getActualSwerveModuleState();
    abstract SwerveModulePosition getSwerveModulePosition();
}
