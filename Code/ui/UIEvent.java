package ui;

import callback.Event;

public abstract class UIEvent extends Event {

	protected UIEvent(String name) {
		super(name);
	}

	/**
	 * Accept the event visitor.
	 * 
	 * All concrete classes should implement this method and call visit() on the
	 * provided visitor, passing this class as argument.
	 * 
	 * @param visitor - the visitor to accept.
	 */
	public abstract void accept(UIEventVisitor visitor);
	
}
