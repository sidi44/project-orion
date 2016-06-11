package ui;

public class UIEventScreenChange extends UIEvent {

	private ScreenName from;
	private ScreenName to;
	
	protected UIEventScreenChange(String name, ScreenName from, ScreenName to) {
		super(name);
		this.from = from;
		this.to = to;
	}

	@Override
	public void accept(UIEventVisitor visitor) {
		visitor.visit(this);
	}

	public ScreenName getFrom() {
		return from;
	}

	public ScreenName getTo() {
		return to;
	}

}
