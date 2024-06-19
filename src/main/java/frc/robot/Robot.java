package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.UnitTests.UnitTest;
import frc.robot.UnitTests.WheelsCalibration;
import org.littletonrobotics.junction.LogFileUtil;
import org.littletonrobotics.junction.LoggedRobot;
import org.littletonrobotics.junction.Logger;
import org.littletonrobotics.junction.networktables.NT4Publisher;
import org.littletonrobotics.junction.wpilog.WPILOGReader;
import org.littletonrobotics.junction.wpilog.WPILOGWriter;

public class Robot extends LoggedRobot {
    private enum SimulationMode {
        REPLAY,
        SIMULATION
    }
    private static final SimulationMode simulationMode = SimulationMode.REPLAY;
    private Command autonomousCommand;
    private RobotContainer robotContainer;

    @Override
    public void robotInit() {
        // Set up data receivers & replay source
        if (isReal()) {
            // Running on a real robot, log to a USB stick ("/U/logs")
            Logger.addDataReceiver(new WPILOGWriter());
            Logger.addDataReceiver(new NT4Publisher());
        } else {
            switch (simulationMode) {
                case SIMULATION -> // Running a physics simulator, log to NT
                        Logger.addDataReceiver(new NT4Publisher());
                case REPLAY -> {
                    // Replaying a log, set up replay source
                    setUseTiming(false); // Run as fast as possible
                    String logPath = LogFileUtil.findReplayLog();
                    Logger.setReplaySource(new WPILOGReader(logPath));
                    Logger.addDataReceiver(new WPILOGWriter(LogFileUtil.addPathSuffix(logPath, "_sim")));
                }
            }
        }

        // Start AdvantageKit logger
        Logger.start();

        robotContainer = new RobotContainer();
    }

    @Override
    public void robotPeriodic() {
        CommandScheduler.getInstance().run();
    }

    @Override
    public void disabledPeriodic() {

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
    }

    @Override
    public void teleopExit() {
    }

    private final UnitTest unitTest = new WheelsCalibration();

    @Override
    public void testInit() {
        CommandScheduler.getInstance().cancelAll();
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