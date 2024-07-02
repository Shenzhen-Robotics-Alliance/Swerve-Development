package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import frc.robot.Subsystems.MapleSubsystem;

import java.util.function.Supplier;

public abstract class HolomonicChassisLogic extends MapleSubsystem {
    protected final double maxVelocityMetersPerSecond, maxAccelerationMetersPerSquaredSecond, maxAngularVelocityRadiansPerSecond;

    public HolomonicChassisLogic(MapleConfigFile.ConfigBlock generalConfigBlock) {
        super("Chassis");

        this.maxVelocityMetersPerSecond = generalConfigBlock.getDoubleConfig("maxVelocityMetersPerSecond");
        this.maxAccelerationMetersPerSquaredSecond = generalConfigBlock.getDoubleConfig("maxVelocityMetersPerSecond");
        this.maxAngularVelocityRadiansPerSecond = generalConfigBlock.getDoubleConfig("maxAngularVelocityRadiansPerSecond");
    }

    @Override
    public void onReset() {
        setCurrentPose2d(new Pose2d()); // TODO: set a default location to start
    }

    @Override
    public void periodic(double dt, boolean enabled) {

    }

    protected abstract void runChassisSpeeds(ChassisSpeeds chassisSpeeds);
    protected abstract void lockChassis();
    public abstract void setCurrentPose2d(Pose2d currentPose2d);
    public abstract Pose2d getCurrentRobotPose2d();

    /**
     * drives the chassis manually according to pilot input
     * @param pilotTranslationalInputSupplier supplier to the pilot's translational input, the x-axis is to the front (like chassis speeds)
     * @param rotationSpeedSupplier supplier to the desired rotation speed of the robot, in radians/seconds
     * */
    public Command driverControlCommand(Supplier<Translation2d> pilotTranslationalInputSupplier, Supplier<Double> rotationSpeedSupplier, boolean useFieldCentric) {
        return Commands.run(
                () -> {
                    // TODO drive by pilot
                }, this
        ).finallyDo(() -> runChassisSpeeds(new ChassisSpeeds()));
    }
    

    public Command driverControlCommandFacingDirection(Translation2d pilotTranslationalInput, Rotation2d desiredFacing, boolean useFieldCentric) {
        // TODO drive by pilot
        return new InstantCommand();
    }

    public Command driveToPoseStraightLineCommand(Pose2d setPoint) {
        return new InstantCommand();
    }

    public Command driveToPoseAStarCommander(Pose2d destination) {
        return new InstantCommand();
    }

    /**
     *
     * */
    public Command lockChassisToXFormation() {
        return new InstantCommand(this::lockChassis).finallyDo(() -> runChassisSpeeds(new ChassisSpeeds()));
    }
}
