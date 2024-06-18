package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.UnitTests.UnitTest;
import frc.robot.UnitTests.WheelsCalibration;

public class Robot extends TimedRobot {
  private Command m_autonomousCommand;
  private RobotContainer m_robotContainer;
  @Override
  public void robotInit() {
      m_robotContainer = new RobotContainer();
    }
  @Override
  public void robotPeriodic() {
    CommandScheduler.getInstance().run();
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  @Override
  public void autonomousPeriodic() {}

  @Override
  public void autonomousExit() {}

  @Override
  public void teleopInit() {
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  @Override
  public void teleopPeriodic() {}

  @Override
  public void teleopExit() {}

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
