package callback;

import java.util.ArrayList;
import java.util.List;

public abstract class Sender {

	private List<Receiver> receivers;
	
	protected Sender() {
		receivers = new ArrayList<Receiver>();
	}
	
	public void addReceiver(Receiver receiver) {
		receivers.add(receiver);
	}
	
	public void removeReceiver(Receiver receiver) {
		receivers.remove(receiver);
	}
	
	public void sendToAll(Event event) {
		for (Receiver receiver : receivers) {
			receiver.receive(event);
		}
	}
	
}
