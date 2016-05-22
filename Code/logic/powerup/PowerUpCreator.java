package logic.powerup;

import java.util.Map;
import java.util.Map.Entry;

import utils.NumberUtils;
import logic.Maze;

public class PowerUpCreator {

	private Maze maze;
	
	public PowerUpCreator(Maze maze) {
		this.maze = maze;
	}
	
	public PowerUp createPredatorPowerUp(PowerUpType type, int strength) {
		
		PowerUp powerUp = null;
		
		switch (type) {
		
			case Freeze:
				powerUp = new PowerUpFreeze(PowerUpTarget.AllPrey, strength);
				break;
			case Magnet:
				powerUp = new PowerUpMagnet(PowerUpTarget.AllPrey, strength);
				break;
			case SlowDown:
				powerUp = new PowerUpSlowDown(PowerUpTarget.AllPrey, strength);
				break;
			case SpeedUp:
				powerUp = new PowerUpSpeedUp(PowerUpTarget.Owner, strength);
				break;
			case Teleport:
				powerUp = new PowerUpTeleport(maze.getRandomPoint());
				break;
			default:
				throw new IllegalArgumentException("Unknown power up type.");
		
		}
		
		return powerUp;
	}
	
	public PowerUp createRandomPredatorPowerUp(
			Map<PowerUpType, Integer> powerUpDefs) {
		
		int randomNum = NumberUtils.randomInt(0, powerUpDefs.size() - 1);
		int count = 0;
		for (Entry<PowerUpType, Integer> entry : powerUpDefs.entrySet()) {
			if (randomNum == count) {
				return createPredatorPowerUp(entry.getKey(), entry.getValue());
			}
			++count;
		}
		
		System.err.println("Failed to create power up.");
		return null;
	}
	
}
