package frc.robot.Subsystems.Drive;

import edu.wpi.first.math.estimator.SwerveDrivePoseEstimator;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.*;
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

    public SwerveDriveChassis(MapleConfigFile.ConfigBlock generalConfigBlock, SwerveModuleReal frontLeft, SwerveModuleReal frontRight, SwerveModuleReal backLeft, SwerveModuleReal backRight, LoggedGyro gyro) {
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
        for (int measurementID = 0; measurementID < odometryThread.getOdometryMeasurementTimeStamps().length; measurementID++)
            poseEstimator.updateWithTime(
                    odometryThread.getOdometryMeasurementTimeStamps()[measurementID],
                    // gyro.getRobotFacings()[measurementID],
                    gyro.getLatestRobotRotation2d(),
                    getModulesPositionsAtGivenTimeStamp(measurementID)
            );
        Logger.recordOutput("/Odometry/GyroReadings", gyro.getRobotFacings());
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

    /**
     * gets the modules' positions at a given measurement timestamp
     * @param measurementID the id of the measurement, meaning the i th measurement since last robot period
     * */
    private SwerveModulePosition[] getModulesPositionsAtGivenTimeStamp(int measurementID) {
        final SwerveModulePosition[] swerveModulePositions = new SwerveModulePosition[modules.length];
        for (int moduleID = 0; moduleID < swerveModulePositions.length; moduleID++)
            swerveModulePositions[moduleID] = modules[moduleID].getCachedSwerveModulePositions()[measurementID];
        return swerveModulePositions;
    }

    /**
     * gets the latest swerve states measured by the modules
     * @return the swerve states, in the order FL, FR, BL, BR
     * */
    @AutoLogOutput(key = "/Odometry/MeasuredSwerveStates")
    private SwerveModuleState[] getMeasuredSwerveModuleStates() {
        final SwerveModuleState[] swerveModuleStates = new SwerveModuleState[modules.length];
        for (int i = 0; i < modules.length; i++)
            swerveModuleStates[i] = modules[i].getActualSwerveModuleState();
        return swerveModuleStates;
    }
}
