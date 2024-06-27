package frc.robot.Helpers;

public class WheelsCalibrationFileReader {
    public WheelsCalibrationFileReader(String calibrationFileName) {
        // TODO: in the constructor, read the xml file and store it in hashmap
    }

    public double getDoubleConfig(String wheelName, String configName) {
        // TODO get a double variable in the config of a given wheel, throws exception if config not found
    }

    public double getIntConfig(String wheelName, String configName) {
        // TODO get a int variable
    }

    public static void main(String[] args) {
        WheelsCalibrationFileReader fileReader = new WheelsCalibrationFileReader("myRobot"); // reads the file deploy/WheelCalibrations/myRobot.xml

        fileReader.getIntConfig("frontLeft", "drivingMotorPort"); // returns 2
        fileReader.getDoubleConfig("frontRight", "steeringEncoderZeroPosition"); // returns 0.6
        fileReader.getDoubleConfig("frontRight", "Dummy"); // throws exception
    }
}
