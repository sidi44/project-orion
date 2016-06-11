package data;

import java.util.HashMap;
import java.util.Map;

class LevelsData {

	private Map<String, Level> levels;
	
	public LevelsData() {
		levels = new HashMap<String, Level>();
	}
	
	public Level getLevel(int number) {
		String num = intToString(number);
		Level level = levels.get(num);
		return level;
	}
	
	public boolean levelExists(int number) {
		String num = intToString(number);
		return levels.containsKey(num);
	}
	
	private String intToString(int number) {
		return Integer.toString(number);
	}
}
