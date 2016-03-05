package physics;

/**
 * A Visitor interface for physics events. 
 * 
 * Classes which need to perform different actions depending on the type of a 
 * physics event should implement this interface.
 * 
 * All concrete implementations of PhysicsEvent will implement the accept() 
 * method, which takes a PhysicsEventVisitor as a parameter. The concrete 
 * physics events will then call visit() on the visitor, providing themselves as
 * argument, hence calling the correct PhysicsEventVisitor method. 
 * 
 * This avoids the need to dynamic cast physics events and have huge if..else 
 * blocks for each different type. Additionally, there will be an error if a new 
 * physics event is created which doesn't implement the accept() method 
 * (avoiding the chance of forgetting to add a new case to the if..else block).
 * 
 * (See sound.EventSoundProcessor for an example).
 * 
 * @author Simon Dicken
 * @version 2016-01-10
 */
public interface PhysicsEventVisitor {

	void visit(PhysicsEventContact event);
	
}
