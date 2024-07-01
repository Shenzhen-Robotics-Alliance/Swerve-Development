package frc.robot.UnitTests;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.TalonFX;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.Constants;
import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;
import frc.robot.Helpers.MathHelpers.AngleHelpers;

public class WheelsCalibration implements UnitTest {
    private static final class Wheel {
        public final String name;
        public final int drivingMotorID, steeringMotorID, encoderID, drivingMotorPortOnPDP, steeringMotorPortOnPDP;

        private Wheel(String name, int drivingMotorID, int steeringMotorID, int encoderID) {
            this(name, drivingMotorID, steeringMotorID, encoderID, -1, -1);
        }

        private Wheel(String name, int drivingMotorID, int steeringMotorID, int encoderID, int drivingMotorPortOnPDP, int steeringMotorPortOnPDP) {
            this.name = name;
            this.drivingMotorID = drivingMotorID;
            this.steeringMotorID = steeringMotorID;
            this.encoderID = encoderID;
            this.drivingMotorPortOnPDP = drivingMotorPortOnPDP;
            this.steeringMotorPortOnPDP = steeringMotorPortOnPDP;
        }
    }

    private final Wheel[] wheels;

    private final XboxController xboxController = new XboxController(0);

    private int i;
    private enum SteerWheelTurningDirection {
        NOT_INVERTED,
        INVERTED
    }
    private final SendableChooser<SteerWheelTurningDirection> wheelTurningDirectionSendableChooser = new SendableChooser<>();
    private final String configName;
    private final MapleConfigFile calibrationFile;
    public WheelsCalibration() {
        configName = "5516-2024-OnSeason";
        wheels = new Wheel[] {
                new Wheel("FrontLeft", 3, 4, 10),
                new Wheel("FrontRight", 6, 5, 11),
                new Wheel("BackLeft", 1, 2, 9),
                new Wheel("BackRight", 8, 7, 12)
        };

        this.calibrationFile = new MapleConfigFile("ChassisWheelsCalibration", configName);
    }

    @Override
    public void testStart() {
        i = 0;
        wheelTurningDirectionSendableChooser.setDefaultOption(SteerWheelTurningDirection.NOT_INVERTED.name(), SteerWheelTurningDirection.NOT_INVERTED);
        wheelTurningDirectionSendableChooser.addOption(SteerWheelTurningDirection.INVERTED.name(), SteerWheelTurningDirection.INVERTED);
        SmartDashboard.putData("Steer Motor Turning Direction (Should be Spinning Counter-Clockwise)", wheelTurningDirectionSendableChooser);
    }

    private boolean wasPressed = false, steeringMotorInverted = false;
    @Override
    public void testPeriodic() {
        if (i > 3) return;

        final Wheel currentWheel = wheels[i];
        final TalonFX
                drivingMotor = new TalonFX(currentWheel.drivingMotorID, Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME),
                steeringMotor = new TalonFX(currentWheel.steeringMotorID, Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME);
        final CANcoder canCoder = new CANcoder(currentWheel.steeringMotorID, Constants.ChassisConfigs.CHASSIS_CANIVORE_NAME);
        SmartDashboard.putString("Calibration/CurrentWheel", currentWheel.name);
        steeringMotorInverted = switch (wheelTurningDirectionSendableChooser.getSelected()){
            case NOT_INVERTED -> false;
            case INVERTED -> true;
        };

        if (xboxController.getAButton())
            drivingMotor.set(0.3);
        else
            drivingMotor.set(0);

        if (xboxController.getBButton())
            steeringMotor.set(steeringMotorInverted ? -0.2:0.2);
        else
            steeringMotor.set(0);

        if (xboxController.getXButton() && (!wasPressed))
            saveConfigurationForCurrentWheel(currentWheel,canCoder.getAbsolutePosition().getValue() * Math.PI * 2);
        wasPressed = xboxController.getXButton();
    }

    private void saveConfigurationForCurrentWheel(Wheel currentWheel, double canCoderAbsolutePosition) {
        final MapleConfigFile.ConfigBlock configBlock = calibrationFile.getBlock(currentWheel.name);
        configBlock.putIntConfig("drivingMotorID", currentWheel.drivingMotorID);
        configBlock.putIntConfig("drivingMotorPortOnPDP", currentWheel.drivingMotorPortOnPDP);
        configBlock.putIntConfig("steeringMotorID", currentWheel.steeringMotorID);
        configBlock.putIntConfig("steeringMotorPortOnPDP", currentWheel.steeringMotorPortOnPDP);
        configBlock.putIntConfig("steeringEncoderID", currentWheel.encoderID);

        configBlock.putIntConfig("steeringMotorInverted", steeringMotorInverted ? 1 : 0);
        configBlock.putDoubleConfig("steeringEncoderReadingAtOrigin", AngleHelpers.simplifyAngle(canCoderAbsolutePosition));
        i++;
        if (i > 3)
            writeConfigurationFile();
    }

    private void writeConfigurationFile() {
        final MapleConfigFile.ConfigBlock configBlock = calibrationFile.getBlock("GeneralInformation");
        configBlock.putDoubleConfig("overallGearRatio", Constants.ChassisConfigs.DEFAULT_GEAR_RATIO);
        configBlock.putDoubleConfig("chassisWidthMeters", Constants.ChassisConfigs.DEFAULT_WIDTH_METERS);
        configBlock.putDoubleConfig("chassisLengthMeters", Constants.ChassisConfigs.DEFAULT_LENGTH_METERS);
        configBlock.putDoubleConfig("maxVelocityMetersPerSecond", Constants.ChassisConfigs.DEFAULT_MAX_VELOCITY_METERS_PER_SECOND);
        configBlock.putDoubleConfig("maxAccelerationMetersPerSecondSquared", Constants.ChassisConfigs.DEFAULT_MAX_ACCELERATION_METERS_PER_SQUARED_SECOND);
        configBlock.putDoubleConfig("maxAngularVelocityRadiansPerSecond", Math.toRadians(Constants.ChassisConfigs.DEFAULT_MAX_ANGULAR_VELOCITY_DEGREES_PER_SECOND));

        calibrationFile.saveConfigToUSBSafe();
    }
}
