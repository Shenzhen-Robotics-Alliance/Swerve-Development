//package frc.robot.Commands;
//
//import edu.wpi.first.math.geometry.Translation2d;
//import edu.wpi.first.wpilibj2.command.Command;
//
//public class DriveToSweetSpotAndShoot extends Command {
//    private enum State {
//        SEARCHING_FOR_APRIL_TAGS,
//        DRIVING_TO_SWEET_SPOT_PREPARING,
//        AIMING,
//        COMPLETED, FAILED
//    }
//
//    private State currentState;
//
//    private final ShooterSubsystem shooter;
//    private final ChassisSubsytem chassis;
//    private final ArmSubsystem arm;
//    public DriveToSweetSpotAndShoot(ShooterSubsystem shooter, ChassisSubsytem chassis, ArmSubsystem arm) {
//        this.shooter = shooter;
//        this.chassis = chassis;
//        this.arm = arm;
//
//        super.addRequirements(shooter, chassis, arm);
//    }
//
//    @Override
//    public void initialize() {
//        currentState = State.SEARCHING_FOR_APRIL_TAGS;
//    }
//
//    @Override
//    public void execute() {
//        switch (currentState) {
//            case SEARCHING_FOR_APRIL_TAGS -> {
//                // prepare shooters
//                shooter.requestRPM(...);
//                arm.requestArmAngle(...);
//                chassis.getDefaultCommand().execute(); // keep driving
//
//                if (vision.aprilTagsReliable())
//                    this.currentState = State.DRIVING_TO_SWEET_SPOT_PREPARING;
//            }
//            case DRIVING_TO_SWEET_SPOT_PREPARING -> {
//                shooter.requestRPM(...);
//                arm.requestArmAngle(...);
//                chassis.requestDirveToPositionVisual(shootingSweetSpot);
//            }
//            case AIMING -> {
//                shooter.requestRPM(...);
//                arm.requestArmAngle(...);
//                chassis.requestDriveVelocity2D(new Translation2d(0, 0));
//            }
//        }
//        ...
//    }
//
//    @Override
//    public void end(boolean interrupted) {
//        this.shooter.requestStop();
//        this.arm.requestRelax();
//        chassis.requestDriveVelocity2D(new Translation2d(0, 0));
//    }
//
//    @Override
//    public String getName() {
//        return "Drive To Sweet Spot And Shoot";
//    }
//
//    @Override
//    public boolean isFinished() {
//        return this.currentState == State.COMPLETED || this.currentState == State.FAILED;
//    }
//}
