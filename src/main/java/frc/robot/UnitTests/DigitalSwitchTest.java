package frc.robot.UnitTests;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.HardwareIOs.Abstractions.RawDigitalSwitch;
import frc.robot.HardwareIOs.Helpers.LoggedDigitalSwitch;
import frc.robot.Robot;
import org.littletonrobotics.junction.Logger;

public class DigitalSwitchTest implements UnitTest {
    private final XboxController xboxController = new XboxController(0);
    private final LoggedDigitalSwitch loggedDigitalSwitch = Robot.mode == Robot.Mode.REAL ?
            new LoggedDigitalSwitch(
                    "test switch 1",
                    RawDigitalSwitch.simpleRawDigitalInput(xboxController::getAButton)
            )
            : new LoggedDigitalSwitch("test switch 1");
    @Override
    public void testStart() {

    }

    @Override
    public void testPeriodic() {
        Logger.recordOutput("test sensor", loggedDigitalSwitch.isTriggered());
        Logger.recordOutput("test sensor not triggered", !loggedDigitalSwitch.isTriggered());
    }
}
