package frc.robot.UnitTests;

import edu.wpi.first.wpilibj.XboxController;
import frc.robot.HardwareIOs.Abstractions.LoggedDigitalSwitch;
import frc.robot.HardwareIOs.Abstractions.LoggedSensor;
import org.littletonrobotics.junction.Logger;

public class DigitalSwitchTest implements UnitTest {
    private final XboxController xboxController = new XboxController(0);
    private final LoggedDigitalSwitch loggedDigitalSwitch = new LoggedDigitalSwitch(
            "test switch 1",
            xboxController::getAButton,
            false
    );
    @Override
    public void testStart() {

    }

    @Override
    public void testPeriodic() {
        LoggedSensor.updateSensors();
        Logger.recordOutput("test sensor", loggedDigitalSwitch.isTriggered());
        Logger.recordOutput("test sensor not triggered", !loggedDigitalSwitch.isTriggered());
    }
}
