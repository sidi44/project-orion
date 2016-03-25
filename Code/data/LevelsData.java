package data;

import java.util.HashMap;
import java.util.Map;

class LevelsData {

	private Map<String, Level> levels;
	
	public LevelsData() {
		levels = new HashMap<String, Level>();
	}
	
	public Level getLevel(int number) {
		String num = Integer.toString(number);
		Level level = levels.get(num);
		return level;
	}
}
