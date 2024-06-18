package frc.robot.UnitTests;

public interface UnitTest {
    void testStart();
    void testPeriodic();
    default void testEnd() {}
}
