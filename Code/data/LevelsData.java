package data;

import java.util.HashMap;
import java.util.Map;

class LevelsData {

	private Map<Integer, Level> levels;
	
	public LevelsData() {
		levels = new HashMap<Integer, Level>();
	}
	
	public Level getLevel(int number) {
		return levels.get(number);
	}
}
