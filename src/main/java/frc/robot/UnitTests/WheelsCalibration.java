package frc.robot.UnitTests;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;

public class WheelsCalibration implements UnitTest {
    private static final class Wheel {
        public final String name;
        public final int drivingMotorID, steeringMotorID, encoderID;

        private Wheel(String name, int drivingMotorID, int steeringMotorID, int encoderID) {
            this.name = name;
            this.drivingMotorID = drivingMotorID;
            this.steeringMotorID = steeringMotorID;
            this.encoderID = encoderID;
        }
    }

    private static final Wheel
            frontLeft = new Wheel("FrontLeft", 3, 4, 10),
            frontRight = new Wheel("FrontRight", 6, 5, 11),
            backLeft = new Wheel("BackLeft", 1, 2, 9),
            backRight = new Wheel("BackRight", 8, 7, 12);

    private static final Wheel[] wheels = new Wheel[] {frontLeft, frontRight, backLeft, backRight};

    private final XboxController xboxController = new XboxController(0);

    private int i;
    private enum SteerWheelTurningDirection {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }
    private final SendableChooser<SteerWheelTurningDirection> wheelTurningDirectionSendableChooser = new SendableChooser<>();
    @Override
    public void testStart() {
        i = 0;
        wheelTurningDirectionSendableChooser.setDefaultOption(SteerWheelTurningDirection.CLOCKWISE.name(), SteerWheelTurningDirection.CLOCKWISE);
        wheelTurningDirectionSendableChooser.addOption(SteerWheelTurningDirection.COUNTER_CLOCKWISE.name(), SteerWheelTurningDirection.COUNTER_CLOCKWISE);
    }
    @Override
    public void testPeriodic() {
        final Wheel currentWheel = wheels[i];
        final TalonFX
                drivingMotor = new TalonFX(currentWheel.drivingMotorID, Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME),
                steeringMotor = new TalonFX(currentWheel.steeringMotorID, Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME);
        final CANcoder canCoder = new CANcoder(currentWheel.steeringMotorID, Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME);
        SmartDashboard.putString("Calibration/CurrentWheel", currentWheel.name);
        wheelTurningDirectionSendableChooser.getSelected(); // TODO write to config file

        if (xboxController.getAButton())
            drivingMotor.set(0.1);
        else
            drivingMotor.set(0);

        if (xboxController.getAButton())
            drivingMotor.set(0.1);
        else
            drivingMotor.set(0);
    }
}
