package frc.robot.UnitTests;

import frc.robot.Helpers.ConfigHelpers.MapleConfigFile;

public class WheelsCalibrationFileReaderTest implements UnitTest {
    @Override
    public void testStart() {
        try {
            MapleConfigFile configFile = MapleConfigFile.fromDeployedConfig("MyConfigType", "myConfig");
            System.out.println(configFile.getIntConfig("block1", "intConfig1")); // returns 2
            System.out.println(configFile.getDoubleConfig("block2", "doubleConfig1")); // returns 0.4

            MapleConfigFile.saveConfigToUSBSafe(configFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void testPeriodic() {

    }
}
