package frc.robot;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Subsystems.MapleSubsystem;
import frc.robot.UnitTests.UnitTest;
import frc.robot.UnitTests.WheelsCalibration;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    public enum Mode {
        REAL,
        REPLAY,
        SIMULATION
    }
    public static final Mode mode = isReal() ? Mode.REAL : Mode.REPLAY;
    private Command autonomousCommand;
    private RobotContainer robotContainer;

    public Robot() {
        super(1/60.0);
    }

    @Override
    public void robotInit() {
        // Set up data receivers & replay source
        switch (mode) {
            case REAL, SIMULATION -> {
                // when running on a real robot, log to a USB stick ("/U/logs")
                Logger.addDataReceiver(new WPILOGWriter());
                Logger.addDataReceiver(new NT4Publisher());
            }
            case REPLAY -> {
                // Replaying a log, set up replay source
                setUseTiming(false); // Run as fast as possible
                String logPath = LogFileUtil.findReplayLog();
                Logger.setReplaySource(new WPILOGReader(logPath));
                Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
                Logger.addDataReceiver(new NT4Publisher()); // preview the replay process while running
            }
        }

        robotContainer = new RobotContainer("5516-2024-OnSeason");
        // Start AdvantageKit logger
        Logger.start();

        MapleSubsystem.subsystemsInit();
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void disabledPeriodic() {
        robotContainer.updateRobot();
    }

    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutonomousCommand();

        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }
    }

    @Override
    public void autonomousPeriodic() {
        robotContainer.updateRobot();
    }

    @Override
    public void autonomousExit() {
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {
        robotContainer.updateRobot();
        robotContainer.testUnitFeatures();
    }

    @Override
    public void teleopExit() {
    }

    private final UnitTest unitTest = new WheelsCalibration();

    @Override
    public void testInit() {
        unitTest.testStart();
    }

    @Override
    public void testPeriodic() {
        unitTest.testPeriodic();
    }

    @Override
    public void testExit() {
        unitTest.testEnd();
    }
}