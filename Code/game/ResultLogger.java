package game;

import java.util.ArrayList;
import java.util.List;

public class ResultLogger {

	private List<GameResult> results;
	
	public ResultLogger() {
		this.results = new ArrayList<GameResult>();
	}
	
	public void addResult(GameResult result) {
		results.add(result);
	}
	
	public List<GameResult> getResults() {
		return results;
	}
	
	public void reset() {
		results.clear();
	}
	
}
