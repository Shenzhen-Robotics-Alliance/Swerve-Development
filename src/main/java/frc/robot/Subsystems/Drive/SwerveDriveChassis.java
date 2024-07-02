package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModulePosition;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import frc.robot.HardwareIO.Helpers.LoggedGyro;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import org.littletonrobotics.junction.AutoLogOutput;
import org.littletonrobotics.junction.Logger;

public class SwerveDriveChassis extends HolomonicChassisLogic {
    private final double horizontalWheelsMarginMeters, verticalWheelsMarginMeters;

    private final SwerveModuleReal[] modules;
    private final LoggedGyro gyro;
    private final SwerveDriveKinematics swerveDriveKinematics;
    private final SwerveDrivePoseEstimator poseEstimator;
    private final OdometryThread odometryThread;

    protected SwerveDriveChassis(MapleConfigFile.ConfigBlock generalConfigBlock, SwerveModuleReal frontLeft, SwerveModuleReal frontRight, SwerveModuleReal backLeft, SwerveModuleReal backRight, LoggedGyro gyro) {
        super(generalConfigBlock);

        this.horizontalWheelsMarginMeters = generalConfigBlock.getDoubleConfig("leftRightWheelsDistanceMeters");
        this.verticalWheelsMarginMeters = generalConfigBlock.getDoubleConfig("frontBackWheelsDistanceMeters");

        modules = new SwerveModuleReal[] {frontLeft, frontRight, backLeft, backRight};
        for (SwerveModuleReal module:modules)
            module.onReset();
        this.gyro = gyro;
        this.gyro.setCurrentRotation2d(new Rotation2d());

        final Translation2d[] MODULE_TRANSLATIONS = new Translation2d[] {
                new Translation2d(horizontalWheelsMarginMeters / 2, verticalWheelsMarginMeters / 2), // FL
                new Translation2d(horizontalWheelsMarginMeters / 2, -verticalWheelsMarginMeters / 2), // FR
                new Translation2d(-horizontalWheelsMarginMeters / 2, verticalWheelsMarginMeters / 2), // BL
                new Translation2d(-horizontalWheelsMarginMeters / 2, -verticalWheelsMarginMeters / 2)  // BR
        };
        swerveDriveKinematics = new SwerveDriveKinematics(MODULE_TRANSLATIONS);
        poseEstimator = new SwerveDrivePoseEstimator(swerveDriveKinematics, gyro.getLatestRobotRotation2d(), getModulesLatestPositions(), new Pose2d());
        this.odometryThread = OdometryThread.getInstance();
    }

    @Override
    public void setCurrentPose2d(Pose2d currentPose2d) {
        this.poseEstimator.resetPosition(gyro.getLatestRobotRotation2d(), getModulesLatestPositions(), currentPose2d);
    }

    @Override
    public void periodic(double dt, boolean enabled) {
        for (int measurementCount = 0; measurementCount < odometryThread.getOdometerTimeStampsSincePreviousRobotPeriod().length; measurementCount++)
            poseEstimator.updateWithTime(
                    odometryThread.getOdometerTimeStampsSincePreviousRobotPeriod()[measurementCount],
                    gyro.getRobotFacings()[measurementCount],
                    getModulesCachedPositionArrays()[measurementCount]
            );
        super.periodic(dt, enabled);
    }

    @Override
    protected void runChassisSpeeds(ChassisSpeeds chassisSpeeds) {
        // TODO run chassis with kinematics
    }

    @Override
    protected void lockChassis() {
        // TODO lock chassis to x formation
    }

    @Override
    public Pose2d getCurrentRobotPose2d() {
        return poseEstimator.getEstimatedPosition();
    }

    @Override
    public void onReset() {
        super.onReset();
    }

    @AutoLogOutput(key = "/Odometry/LatestSwervePositions")
    private SwerveModulePosition[] getModulesLatestPositions() {
        final SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[modules.length];
        for (int i = 0; i < swerveModulePositions.length; i++)
            swerveModulePositions[i] = modules[i].getLatestSwerveModulePosition();
        return swerveModulePositions;
    }

    private double[] getOdometryMeasurementTimeStamps() {
        return odometryThread.getOdometerTimeStampsSincePreviousRobotPeriod();
    }

    private SwerveModulePosition[][] getModulesCachedPositionArrays() {
        final SwerveModulePosition[][] swerveModulePositions = new SwerveModulePosition[modules.length][];
        for (int i = 0; i < swerveModulePositions.length; i++)
            swerveModulePositions[i] = modules[i].getCachedSwerveModulePositions();
        return swerveModulePositions;
    }

    @AutoLogOutput(key = "/Odometry/MeasuredSwerveStates")
    private SwerveModuleState[] swerveModuleStates() {
        final SwerveModuleState[] swerveModuleStates = new SwerveModuleState[modules.length];
        for (int i = 0; i < modules.length; i++)
            swerveModuleStates[i] = modules[i].getActualSwerveModuleState();
        return swerveModuleStates;
    }
}
