package frc.robot.Helpers.MathHelpers;

import edu.wpi.first.math.geometry.Translation2d;

public final class TranslationHelpers {
    public static Translation2d translation2dFromDirectionAndMagnitude(double direction, double magnitude) {
        return new Translation2d(
                magnitude * Math.cos(direction),
                magnitude * Math.sin(direction)
        );
    }
}
