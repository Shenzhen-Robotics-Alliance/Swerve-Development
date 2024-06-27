package frc.robot.UnitTests;

import frc.robot.Helpers.WheelsCalibrationFileReader;

public class WheelsCalibrationFileReaderTest implements UnitTest {
    private WheelsCalibrationFileReader wheelsCalibrationFileReader = new WheelsCalibrationFileReader("5516");
    @Override
    public void testStart() {
        System.out.println(wheelsCalibrationFileReader.getIntConfig("frontLeft", "drivingMotorPort"));
        System.out.println(wheelsCalibrationFileReader.getDoubleConfig("frontRight", "steeringEncoderZeroPosition"));
    }

    @Override
    public void testPeriodic() {

    }
}
