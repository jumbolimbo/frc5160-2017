package org.usfirst.frc.team5160.robot.commands;

import org.usfirst.frc.team5160.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;

/**
 *Pushes gear forward until button is released
 */
public class CMDPushGear extends Command {
	
	double speed;

    public CMDPushGear(double speed) {
        requires(Robot.GEAR_MECHANISM);
        
        this.speed = speed;
    }
    @Override
    protected void execute() {
    	Robot.GEAR_MECHANISM.pushGear(speed);
    }
    @Override
    protected boolean isFinished() {
        return false;
    }
    @Override
    protected void end() {
    	Robot.GEAR_MECHANISM.stopMotor();
    }

    protected void interrupted() {
    	end();
    }

	@Override
	protected void initialize() {
		
	}
}
