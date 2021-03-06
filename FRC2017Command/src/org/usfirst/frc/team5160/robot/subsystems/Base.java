package org.usfirst.frc.team5160.robot.subsystems;

import org.usfirst.frc.team5160.robot.Robot;
import org.usfirst.frc.team5160.robot.RobotMap;
import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import org.usfirst.frc.team5160.robot.commands.CMDTeleOpMecanumDrive;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class Base extends Subsystem {
    
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	
	//TODO: Install libraries for CANTalon
	
	//Declare motors
	private CANTalon frontLeft;
	private CANTalon backLeft; 
	private CANTalon frontRight;
	private CANTalon backRight;
	
	//Declare drive base
	private RobotDrive driveBase;
	
	//Declare gyro
	private ADXRS450_Gyro gyro;
	
	//Positions for auto drive
	private double targetLeftPos; 
	private double targetRightPos; 
	private double zeroLeftPos;
	private double zeroRightPos;
	
	public Base(){
		
		//Init motors
		frontLeft = new CANTalon(RobotMap.FRONT_LEFT_CIM);
		backLeft = new CANTalon(RobotMap.BACK_LEFT_CIM);
		frontRight = new CANTalon(RobotMap.FRONT_RIGHT_CIM);
		backRight = new CANTalon(RobotMap.BACK_RIGHT_CIM);
		
		frontRight.setInverted(true);
		backRight.setInverted(true);
		
		//Call init on all motors
		initMotor(backLeft);
		initMotor(backRight);
		initMotor(frontLeft);
		initMotor(frontRight);
		//Init base
		driveBase = new RobotDrive(frontLeft, backLeft, frontRight, backRight);
		
		//Init gyro
		gyro = new ADXRS450_Gyro(/*port*/);
		
	}

    public void initDefaultCommand() {
    	//set default command as TeleOp Drive
    	setDefaultCommand(new CMDTeleOpMecanumDrive());
    	
    }
    public void mecanumDrive(double x, double y, double rotation){
    	ensureMechanumTeleOp();
    	frontLeft.setInverted(false);
    	backLeft.setInverted(false);
    	
    	//Cartesian mecanum drive, with respect to the gyro angle
    	driveBase.mecanumDrive_Cartesian(x, y, rotation, 0);
    }
    public void mecanumDriveField(double x, double y, double rotation){
    	ensureMechanumTeleOp();
    	frontLeft.setInverted(false);
    	backLeft.setInverted(false);
    	
    	//Cartesian mecanum drive, with respect to the gyro angle
    	driveBase.mecanumDrive_Cartesian(x, y, rotation, gyro.getAngle());
    }
    public void tankDrive(double leftValue, double rightValue){
    	
		frontRight.setInverted(false);
		backRight.setInverted(false);
		
		frontLeft.setInverted(true);
		backLeft.setInverted(true);
    	
    	//Tank drive
    	driveBase.tankDrive(leftValue, rightValue);
    }
    public void stopMotors(){
    	frontLeft.set(0);
    	frontRight.set(0);
    	backLeft.set(0);
    	backRight.set(0);
    }
    public ADXRS450_Gyro getGyro(){
    	return gyro;
    }
    //Code for initializing the motors at init
    private void initMotor(CANTalon motor){
    	motor.setVoltageRampRate(24.0);
    	motor.configNominalOutputVoltage(-0f, 0f);
    	motor.configPeakOutputVoltage(-12, 12);
    	ensureMotorMode(motor, TalonControlMode.PercentVbus);
    	motor.setFeedbackDevice(FeedbackDevice.QuadEncoder);
    	motor.setAllowableClosedLoopErr(100);
    	motor.setProfile(0);
    	motor.setPID(Robot.driveD, Robot.driveI, Robot.driveD );
    	motor.setF(Robot.driveF);
    }
    private void ensureMotorMode(CANTalon motor, TalonControlMode neededMode){
    	if (motor.getControlMode() != neededMode) {
			motor.changeControlMode(neededMode);
		}
    }
    private void ensurePositionTank(){
    	System.out.println("Ensured position");
    	ensureMotorMode(frontLeft, TalonControlMode.Position);
    	ensureMotorMode(frontRight, TalonControlMode.Position);
    	ensureMotorMode(backLeft, TalonControlMode.Follower); 
    	ensureMotorMode(backRight, TalonControlMode.Follower);
    	backLeft.set(RobotMap.FRONT_LEFT_CIM);
    	backRight.set(RobotMap.FRONT_RIGHT_CIM);
    }
    private void ensureMechanumTeleOp(){
    	ensureMotorMode(frontLeft, TalonControlMode.PercentVbus);
    	ensureMotorMode(frontRight, TalonControlMode.PercentVbus);
    	ensureMotorMode(backLeft, TalonControlMode.PercentVbus);
    	ensureMotorMode(backRight, TalonControlMode.PercentVbus);
    }
    
    public void positionTankDriveSet(double dLeft, double dRight){
    	ensurePositionTank();
    	frontLeft.setEncPosition(0);
    	frontRight.setEncPosition(0);
    	backRight.setEncPosition(0);
    	backLeft.setEncPosition(0);
    	targetLeftPos = dLeft;
    	targetRightPos = dRight;
    }
    public void positionTankDriveExecute(){
    		frontLeft.set(targetLeftPos);
    		frontRight.set(targetRightPos);

    }
    public boolean positionTankDriveReached(){
    	if((frontLeft.getPosition() - targetLeftPos) >= 0 && (frontRight.getPosition() - targetRightPos) >= 0 ){
    		return true;
    	}
    	return false;
    }
    public static double inchToEncoderTick(double inches){
    	return 1*inches;
    }
    public static double feetToEncoderTick(double feet){
    	return inchToEncoderTick(12*feet);
    }

	public void printEncoders() {
		System.out.println(frontLeft.getEncPosition()+" , "+ frontRight.getEncPosition());
	}
	
}