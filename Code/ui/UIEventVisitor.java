package ui;

/**
 * A Visitor interface for UI events. 
 * 
 * Classes which need to perform different actions depending on the type of a 
 * UI event should implement this interface.
 * 
 * All concrete implementations of UIEvent will implement the accept() method, 
 * which takes a UIEventVisitor as a parameter. The concrete UI events will 
 * then call visit() on the visitor, providing themselves as argument, hence 
 * calling the correct UIEventVisitor method. 
 * 
 * This avoids the need to dynamic cast UI events and have huge if..else 
 * blocks for each different type. Additionally, there will be an error if a new 
 * UI event is created which doesn't implement the accept() method 
 * (avoiding the chance of forgetting to add a new case to the if..else block).
 * 
 * (See sound.EventMusicProcessor for an example).
 * 
 * @author Simon Dicken
 * @version 2016-03-26
 */
public interface UIEventVisitor {

	void visit(UIEventScreenChange event);
	
}
