package logic.powerup;

/**
 * A Visitor interface for power ups. 
 * 
 * Classes which need to perform different actions depending on the type of a 
 * power up should implement this interface.
 * 
 * All concrete implementations of PowerUp will implement the accept() method, 
 * which takes a PowerUpVisitor as a parameter. The concrete power ups will then
 * call visit() on the visitor, providing themselves as argument, hence calling 
 * the correct PowerUpVisitor method. 
 * 
 * This avoids the need to dynamic cast power ups and have huge if..else blocks 
 * for each different type. Additionally, there will be an error if a new power
 * up is created which doesn't implement the accept() method (avoiding the 
 * chance of forgetting to add a new case to the if..else block).
 * 
 * @author Simon Dicken
 * @version 2015-10-18
 */
public interface PowerUpVisitor {

	void visit(PowerUpSpeedUp powerUp);	
	
	void visit(PowerUpSlowDown powerUp);
	
	void visit(PowerUpFreeze powerUp);
	
	void visit(PowerUpMagnet powerUp);
	
	void visit(PowerUpTeleport powerUp);
	
}
