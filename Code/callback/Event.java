package callback;

public abstract class Event {

	private String name;
	
	protected Event(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

}
