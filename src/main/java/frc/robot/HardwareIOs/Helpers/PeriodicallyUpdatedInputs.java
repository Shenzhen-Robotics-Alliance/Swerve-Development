package frc.robot.HardwareIOs.Helpers;

import java.util.HashSet;
import java.util.Set;

public final class PeriodicallyUpdatedInputs {
    private static final Set<PeriodicallyUpdatedInput> inputs = new HashSet<>();
    public interface PeriodicallyUpdatedInput {
        void update();
    }

    static void register(PeriodicallyUpdatedInput input) {
        inputs.add(input);
    }
    static void cancelRegister(PeriodicallyUpdatedInput input) {
        inputs.remove(input);
    }

    public static void updateInputs() {
        for (PeriodicallyUpdatedInput input:inputs)
            input.update();
    }
}
