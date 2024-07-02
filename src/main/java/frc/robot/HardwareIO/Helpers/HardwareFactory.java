package frc.robot.HardwareIO.Helpers;

import com.ctre.phoenix6.hardware.CANcoder;
import com.ctre.phoenix6.hardware.Pigeon2;
import com.ctre.phoenix6.hardware.TalonFX;
import frc.robot.HardwareIO.Abstractions.RawMotor;
import frc.robot.HardwareIO.VendorImplements.CTRE.CanCoderEncoderImpl;
import frc.robot.HardwareIO.VendorImplements.CTRE.Pigeon2EncoderImpl;
import frc.robot.HardwareIO.VendorImplements.CTRE.TalonFXMotorAndEncoderImpl;
import frc.robot.Robot;

public class HardwareFactory {
    public static LoggedMotor createMotor(String name, TalonFX talonFXInstance, boolean inverted, int portOnPDP) {
        final RawMotor rawMotor = new TalonFXMotorAndEncoderImpl(talonFXInstance, inverted);
        return new LoggedMotor(name, rawMotor, portOnPDP);
    }

    public static LoggedRelativePositionEncoder createRelativePositionEncoderThreaded(String name, TalonFX talonFXInstance, boolean inverted) {
        if (Robot.mode != Robot.Mode.REAL)
            return new LoggedRelativePositionEncoder(name);
        final TalonFXMotorAndEncoderImpl talonFXMotorAndEncoder= new TalonFXMotorAndEncoderImpl(talonFXInstance, inverted);
        return new LoggedRelativePositionEncoder(name, talonFXMotorAndEncoder.toOdometryEncoder());
    }

    public static LoggedAbsoluteRotationEncoder createAbsoluteRotationEncoderThreaded(String name, CANcoder canCoderInstance) {
        if (Robot.mode != Robot.Mode.REAL)
            return new LoggedAbsoluteRotationEncoder(name);
        final CanCoderEncoderImpl canCoderEncoder = new CanCoderEncoderImpl(canCoderInstance);
        return new LoggedAbsoluteRotationEncoder(name, canCoderEncoder.toOdometryEncoder());
    }

    public static LoggedRelativePositionEncoder createRelativePositionEncoder(String name, TalonFX talonFXInstance, boolean inverted) {
        if (Robot.mode != Robot.Mode.REAL)
            return new LoggedRelativePositionEncoder(name);
        final TalonFXMotorAndEncoderImpl talonFXMotorAndEncoder= new TalonFXMotorAndEncoderImpl(talonFXInstance, inverted);
        return new LoggedRelativePositionEncoder(name, talonFXMotorAndEncoder);
    }

    public static LoggedAbsoluteRotationEncoder createAbsoluteRotationEncoder(String name, CANcoder canCoderInstance) {
        if (Robot.mode != Robot.Mode.REAL)
            return new LoggedAbsoluteRotationEncoder(name);
        final CanCoderEncoderImpl canCoderEncoder = new CanCoderEncoderImpl(canCoderInstance);
        return new LoggedAbsoluteRotationEncoder(name, canCoderEncoder);
    }

    public static LoggedGyro createGyro(String name, Pigeon2 pigeon2) {
        if (Robot.mode != Robot.Mode.REAL)
            return new LoggedGyro(name);
        final Pigeon2EncoderImpl pigeon2Encoder = new Pigeon2EncoderImpl(pigeon2);
        return new LoggedGyro(name, pigeon2Encoder.toOdometryEncoder());
    }
}
