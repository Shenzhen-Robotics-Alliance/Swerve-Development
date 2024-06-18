package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import frc.robot.UnitTests.UnitTest;
import frc.robot.UnitTests.WheelsCalibration;

public class Robot extends TimedRobot {
  private Command autonomousCommand;
  private RobotContainer robotContainer;
  @Override
  public void robotInit() {
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
  public void autonomousPeriodic() {}

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
