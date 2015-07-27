package logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import geometry.PointXY;

/**
 * Represents a predator agent.
 * 
 * @author Martin Wong
 * @version 2015-07-19
 */
public class Predator extends Agent {
	
	private List<PredatorPowerUpContainer> storedPowerUps;
	private List<PredatorPowerUp> activatedPowerUps;
	private int selectedPowerUp;
	
	/**
	 * Creates an instance of Predator, using id, position, isPlayer
	 * and stacking.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param stacking (boolean)
	 */
	public Predator(int id, PointXY pos, boolean isPlayer, boolean stacking) {
		super(id, pos, isPlayer, stacking);
		
		this.storedPowerUps = new ArrayList<PredatorPowerUpContainer>();
		this.activatedPowerUps = new ArrayList<PredatorPowerUp>();
		this.selectedPowerUp = -1;
	}
	
	/**
	 * Gets the storedPowerUps of the predator.
	 * 
	 * @return storedPowerUps (List<PredatorPowerUpContainer>)
	 */
	public List<PredatorPowerUpContainer> getStoredPowerUps() {
		return storedPowerUps;
	}
	
	/**
	 * Adds a powerup to the predator's storedPowerUps.
	 * 
	 * @param storedPowerUps (PredatorPowerUp)
	 */
	public void addStoredPowerUp(PredatorPowerUp predatorPowerUp) {
		
		boolean found = false;
		
		for (PredatorPowerUpContainer pContainer : storedPowerUps) {
			if (pContainer.getPowerUp().equals(predatorPowerUp)) {
				pContainer.setAmount(pContainer.getAmount() + 1);
				found = true;
				break;
			}
		}
		
		if (!found) {
			storedPowerUps.add(new PredatorPowerUpContainer(predatorPowerUp, 1));
		}
		
		if (storedPowerUps.size() == 1) {
			selectedPowerUp = getSelectedPowerUpTop();
		}
	}
	
	/**
	 * Removes a powerup from the predator's storeedPowers.
	 * 
	 * @param storedPowerUps (PredatorPowerUp)
	 */
	public void removeStoredPowerUp(PredatorPowerUp predatorPowerUp) {
		
		for (PredatorPowerUpContainer pContainer : storedPowerUps) {
			if (pContainer.getPowerUp().equals(predatorPowerUp)) {
				if (pContainer.getAmount() <= 1) {
					storedPowerUps.remove(pContainer);
				} else {
					pContainer.setAmount(pContainer.getAmount() - 1);
				}
				break;
			}
		}
		
		if (storedPowerUps.size() == 0) {
			selectedPowerUp = getSelectedPowerUpTop();
		}
		
	}
	
	/**
	 * Gets the activatedPowerUps of the predator.
	 * 
	 * @return activatedPowerUps (List<PredatorPowerUp>)
	 */
	public List<PredatorPowerUp> getActivatedPowerUps() {
		return activatedPowerUps;
	}
	
	/**
	 * Adds a powerup to the predator's activatedPowerUps.
	 * 
	 * @param activatedPowerUps (PredatorPowerUp)
	 */
	public void addActivatedPowerUp(PredatorPowerUp predatorPowerUp) {
		activatedPowerUps.add(predatorPowerUp);
	}
	
	/**
	 * Removes a powerup from the predator's activatedPowerUps.
	 * 
	 * @param activatedPowerUps (PredatorPowerUp)
	 */
	public void removeActivatedPowerUp(PredatorPowerUp predatorPowerUp) {
		activatedPowerUps.remove(predatorPowerUp);
	}
	
	@Override
	public boolean activatePowerUp() {
		boolean success = false;
		
		if (selectedPowerUp >= 0) {
			PredatorPowerUp powerUp = storedPowerUps.get(selectedPowerUp).getPowerUp();
			
			success = (getStacking() && !isActivated(powerUp))
					|| !hasActivatedPowerUp();

			if (success) {
				addActivatedPowerUp(powerUp);
				removeStoredPowerUp(powerUp);
			}
		}
		
		return success;
	}
	
	/**
	 * Updates activatedPowerUps of the predator (i.e. remove expired ones).
	 */
	public void updateActivatedPowerUps() {
		
		for (int i = 0; i < activatedPowerUps.size(); i++) {
			if (activatedPowerUps.get(i).getTimeRemaining() <= 1) {
				activatedPowerUps.remove(activatedPowerUps.get(i));
				i--;
			} else {
				activatedPowerUps.get(i).decrementTimeRemaining();
			}
		}
	}
	
	/**
	 * Sets selectedPowerUp to the right.
	 */
	public void selectedPowerUpRight() {
		if (selectedPowerUp >= 0) {
			int top = getSelectedPowerUpTop();
			
			if (selectedPowerUp >= top) {
				selectedPowerUp = 0;
			} else {
				selectedPowerUp++;
			}
		}
	}
	
	/**
	 * Sets selectedPowerUp to the left.
	 */
	public void selectedPowerUpLeft() {
		if (selectedPowerUp >= 0) {
			int top = getSelectedPowerUpTop();
			
			if (selectedPowerUp <= 0) {
				selectedPowerUp = top;
			} else {
				selectedPowerUp--;
			}
		}
	}
	
	/**
	 * Checks whether the same powerup type has already been activated.
	 * 
	 * @param predatorPowerUp (PredatorPowerUp)
	 * @return isActivated (boolean)
	 */
	public boolean isActivated(PredatorPowerUp predatorPowerUp) {
		boolean isActivated = false;
		PredatorPowerUpType pType = predatorPowerUp.getPType();
		
		for (PredatorPowerUp powerUp : activatedPowerUps) {
			if (powerUp.getPType() == pType) {
				isActivated = true;
				break;
			}
		}
		return isActivated;
	}
	
	/**
	 * Checks whether the predator has an activated power.
	 * 
	 * @return hasActivatedPowerUp (boolean)
	 */
	public boolean hasActivatedPowerUp() {
		return (activatedPowerUps.size() > 0 );
	}
	
	/**
	 * Gets the selected index of storedPowerUps.
	 * 
	 * @return selectedPowerUp (int)
	 */
	public int getSelectedPowerUp() {
		return selectedPowerUp;
	}
	
	/**
	 * Sets the selected index of storedPowerUps.
	 * 
	 * @param selectedPowerUp (int)
	 */
	public void setSelectedPowerUp(int selectedPowerUp) {
		this.selectedPowerUp = selectedPowerUp;
	}
	
	/**
	 * Sets the selected index of storedPowerUps to the top.
	 * 
	 * @param selectedPowerUp (int)
	 */
	public int getSelectedPowerUpTop() {
		return storedPowerUps.size() - 1;
	}

	@Override
	public PowerUp getSelectedStoredPowerUp() {
		PowerUp powerUp = null;
		
		if (selectedPowerUp != 0) {
			powerUp = storedPowerUps.get(selectedPowerUp).getPowerUp();
		}
		
		return powerUp;
	}
	
}
