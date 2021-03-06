package org.usfirst.frc.team5160.robot.subsystems;

import java.io.DataInput;

import org.usfirst.frc.team5160.robot.RobotMap;
import org.usfirst.frc.team5160.robot.commands.CMDClimbStop;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Climber extends Subsystem {
    
    private CANTalon motor1;
    private CANTalon motor2;
    
    private CANTalon tiltMotor;
    
    
    
    private DigitalInput limitSwitch;
    
    public Climber(){
    	
    	motor1 = new CANTalon(RobotMap.CLIMBER_775_1);
    	motor2 = new CANTalon(RobotMap.CLIMBER_775_2);
    	
    	tiltMotor = new CANTalon(RobotMap.CLIMBER_CIM);
    	
    	limitSwitch = new DigitalInput(RobotMap.CLIMBER_SWITCH);
    }
    
    public void climb(double speed){
    	motor1.set(-speed);
    	motor2.set(speed);
    }
    public void stopMotors(){
    	motor1.set(0);
    	motor2.set(0);
    	tiltMotor.set(0);
    }
    public void tilt(double speed){
    	tiltMotor.set(speed);
    }
    
    public boolean getSwitch(){
    	return limitSwitch.get();
    }
    public void initDefaultCommand() {
        
    	setDefaultCommand(new CMDClimbStop());

    }
}

