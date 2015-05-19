package logic;

import geometry.PointXY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.NumberUtils;

public class GameLogic {
	
	private GameState gs;
	private GameConfig gc;
	
	public GameLogic(GameConfig gc) {
		this.gc = gc;
		
		createGs();
	}
	
	private void createGs() {
		boolean isPlayer = false;
		int randomNum = 0;
		int nLimit = 0;
		int counter = 1;
		int nPredPlayer = gc.getNumPredPlayer();
		int nPreyPlayer = gc.getNumPreyPlayer();
		PointXY point = null;
		List<PointXY> allPoints = new ArrayList<PointXY>();
		Map<Integer, Predator> pred = new HashMap<Integer, Predator>();
		Map<Integer, Prey> prey = new HashMap<Integer, Prey>();
		Map<PointXY, Powerup> powers = new HashMap<PointXY, Powerup>();
		
		// Populate allPoints
		for (int i = gc.getDimensions().getMinX(); i <= gc.getDimensions().getMaxX(); i++) {
			for(int j = gc.getDimensions().getMinY(); j <= gc.getDimensions().getMaxY(); j++) {
				point = new PointXY(i, j);
				allPoints.add(point);
			}
		}
		
		// Populate pred
		nLimit = counter + gc.getNumPred();
		for (int i = counter; i < nLimit; i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			allPoints.remove(randomNum);
			
			isPlayer = (nPredPlayer > 0);
			
			pred.put(counter, new Predator(counter, point, isPlayer));
			counter++;
			nPredPlayer--;
		}
		
		// Populate prey
		nLimit = counter + gc.getNumPrey();
		for (int i = counter; i < nLimit; i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			allPoints.remove(randomNum);
			
			isPlayer = (nPreyPlayer > 0);
			
			prey.put(counter, new Prey(counter, point, isPlayer));
			counter++;
			nPreyPlayer--;
		}
		
		// Populate powers
		for (int i = 0; i < gc.getPTypes().size(); i++) {
			randomNum = NumberUtils.randomInt(0, allPoints.size() - 1);
			point = allPoints.get(randomNum);
			allPoints.remove(randomNum);
			
			powers.put(point, new Powerup(gc.getPTypes().get(i)));
		}
		
		// Build maze
		Maze maze = (gc.getMConfig() == null) ? new Maze(gc.getDimensions()) : new Maze(gc.getDimensions(), gc.getMConfig());
		
		// Create pills
		Set<PointXY> pills = (gc.getHasPills()) ? new HashSet<PointXY>(allPoints) : new HashSet<PointXY>();
		
		// Create game state
		this.gs = new GameState(maze, pred, prey, pills, powers);
	}
	
	public GameState getGs() {
		return this.gs;
	}

	public void setGs(GameState gs) {
		this.gs = gs;
	}

	public void setPredNextMove(int id, Move move) {
		this.gs.getPred().get(id).setNextMove(move);
	}
	
	public void setPreyNextMove(int id, Move move) {
		this.gs.getPrey().get(id).setNextMove(move);
	}
	
}
