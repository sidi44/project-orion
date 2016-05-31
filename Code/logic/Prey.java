package logic;

import geometry.PointXY;

/**
 * Represents a prey agent.
 * 
 * @author Martin Wong
 * @version 2015-08-09
 */
public class Prey extends Agent {
	
//	private List<PreyPowerUpContainer> storedPowerUps;
//	private List<PreyPowerUp> activatedPowerUps;
//	private int selectedPowerUp;
	
	/**
	 * Creates an instance of Prey, using id, position, isPlayer
	 * and stacking.
	 * 
	 * @param id (int)
	 * @param pos (pointXY)
	 * @param isPlayer (boolean)
	 * @param stacking (boolean)
	 */
	public Prey(int id, boolean isPlayer, PointXY position, 
			int baseSpeedIndex, int maxPowerUp) {
		super(id, isPlayer, position, baseSpeedIndex, maxPowerUp);
		
//		this.storedPowerUps = new ArrayList<PreyPowerUpContainer>();
//		this.activatedPowerUps = new ArrayList<PreyPowerUp>();
//		this.selectedPowerUp = -1;
	}
	
//	/**
//	 * Gets the storedPowerUps of the prey.
//	 * 
//	 * @return storedPowerUps (List<PreyPowerUpContainer>)
//	 */
//	public List<PreyPowerUpContainer> getStoredPowerUps() {
//		return storedPowerUps;
//	}
//	
//	/**
//	 * Adds a powerup to the prey's storedPowerUps.
//	 * 
//	 * @param storedPowerUps (PreyPowerUp)
//	 */
//	public void addStoredPowerUp(PreyPowerUp preyPowerUp) {
//		boolean found = false;
//		
//		for (PreyPowerUpContainer pContainer : storedPowerUps) {
//			if (pContainer.getPowerUp().equals(preyPowerUp)) {
//				pContainer.setAmount(pContainer.getAmount() + 1);
//				found = true;
//				break;
//			}
//		}
//		
//		if (!found && storedPowerUps.size() < getMaxPowerUp()) {
//			storedPowerUps.add(new PreyPowerUpContainer(preyPowerUp, 1));
//		}
//		
//		if (storedPowerUps.size() == 1) {
//			selectedPowerUp = getSelectedPowerUpTop();
//		}
//	}
//	
//	/**
//	 * Removes a powerup from the prey's storedPowers.
//	 * 
//	 * @param storedPowerUps (PreyPowerUp)
//	 */
//	public void removeStoredPowerUp(PreyPowerUp preyPowerUp) {
//		int numStoredPowerUps = storedPowerUps.size();
//		
//		for (PreyPowerUpContainer pContainer : storedPowerUps) {
//			if (pContainer.getPowerUp().equals(preyPowerUp)) {
//				if (pContainer.getAmount() <= 1) {
//					storedPowerUps.remove(pContainer);
//				} else {
//					pContainer.setAmount(pContainer.getAmount() - 1);
//				}
//				break;
//			}
//		}
//		
//		if (numStoredPowerUps != storedPowerUps.size()) {
//			selectedPowerUpLeft();
//		}
//		
//	}
//	
//	/**
//	 * Gets the activatedPowerUps of the prey.
//	 * 
//	 * @return activatedPowerUps (List<PreyPowerUp>)
//	 */
//	public List<PreyPowerUp> getActivatedPowerUps() {
//		return activatedPowerUps;
//	}
//	
//	/**
//	 * Adds a powerup to the prey's activatedPowerUps.
//	 * 
//	 * @param activatedPowerUps (PreyPowerUp)
//	 */
//	public void addActivatedPowerUp(PreyPowerUp preyPowerUp) {
//		activatedPowerUps.add(preyPowerUp);
//		preyPowerUp.activate();
//	}
//	
//	/**
//	 * Removes a powerup from the prey's activatedPowerUps.
//	 * 
//	 * @param activatedPowerUps (PreyPowerUp)
//	 */
//	public void removeActivatedPowerUp(PreyPowerUp preyPowerUp) {
//		activatedPowerUps.remove(preyPowerUp);
//	}
//	
//	@Override
//	public boolean activatePowerUp() {
//		boolean success = false;
//		
//		if (isSelectedValid()) {
//			PreyPowerUp powerUp = storedPowerUps.get(selectedPowerUp).getPowerUp();
//			
//			success = (getStacking() && !isActivated(powerUp))
//					|| !hasActivatedPowerUp();
//
//			if (success) {
//				addActivatedPowerUp(powerUp);
//				removeStoredPowerUp(powerUp);
//			}
//		}
//		
//		return success;
//	}
//	
//	@Override
//	public boolean activatePowerUp(int selected) {
//		setSelectedPowerUp(selected);
//		return activatePowerUp();
//	}
//	
//	/**
//	 * Updates activatedPowerUps of the prey (i.e. remove expired ones).
//	 */
//	public void updateActivatedPowerUps() {
//		
//		for (int i = 0; i < activatedPowerUps.size(); ++i) {
//			PreyPowerUp powerUp = activatedPowerUps.get(i);
//			powerUp.decrementTimeRemaining();
//			double timeRemaining = powerUp.getTimeRemaining();
//			if (timeRemaining <= 0) {
//				removeActivatedPowerUp(powerUp);
//				--i;
//			}
//		}
//	}
//	
//	/**
//	 * Sets selectedPowerUp to the right.
//	 */
//	public void selectedPowerUpRight() {
//		int top = getSelectedPowerUpTop();
//		
//		if (top <= 0) {
//			selectedPowerUp = top;
//		} else {
//			if (selectedPowerUp >= top) {
//				selectedPowerUp = 0;
//			} else {
//				++selectedPowerUp;
//			}
//		}
//	}
//	
//	/**
//	 * Sets selectedPowerUp to the left.
//	 */
//	public void selectedPowerUpLeft() {
//		int top = getSelectedPowerUpTop();
//		
//		if (top <= 0) {
//			selectedPowerUp = top;
//		} else {
//			if (selectedPowerUp <= 0) {
//				selectedPowerUp = top;
//			} else {
//				--selectedPowerUp;
//			}
//		}
//	}
//	
//	/**
//	 * Checks whether the same powerup type has already been activated.
//	 * 
//	 * @param preyPowerUp (PreyPowerUp)
//	 * @return isActivated (boolean)
//	 */
//	public boolean isActivated(PreyPowerUp preyPowerUp) {
//		boolean isActivated = false;
//		PreyPowerUpType pType = preyPowerUp.getPType();
//		
//		for (PreyPowerUp powerUp : activatedPowerUps) {
//			if (powerUp.getPType() == pType) {
//				isActivated = true;
//				break;
//			}
//		}
//		return isActivated;
//	}
//	
//	/**
//	 * Checks whether the prey has an activated power.
//	 * 
//	 * @return hasActivatedPowerUp (boolean)
//	 */
//	public boolean hasActivatedPowerUp() {
//		return (activatedPowerUps.size() > 0 );
//	}
//	
//	/**
//	 * Gets the selected index of storedPowerUps.
//	 * 
//	 * @return selectedPowerUp (int)
//	 */
//	public int getSelectedPowerUp() {
//		return selectedPowerUp;
//	}
//	
//	/**
//	 * Sets the selected index of storedPowerUps.
//	 * 
//	 * @param selectedPowerUp (int)
//	 */
//	public void setSelectedPowerUp(int selectedPowerUp) {
//		this.selectedPowerUp = selectedPowerUp;
//	}
//	
//	/**
//	 * Sets the selected index of storedPowerUps to the top.
//	 * 
//	 * @param selectedPowerUp (int)
//	 */
//	public int getSelectedPowerUpTop() {
//		return storedPowerUps.size() - 1;
//	}
//
//	@Override
//	public PowerUp getSelectedStoredPowerUp() {
//		PowerUp powerUp = null;
//		
//		if (isSelectedValid()) {
//			powerUp = storedPowerUps.get(selectedPowerUp).getPowerUp();
//		}
//		
//		return powerUp;
//	}
//	
//	/**
//	 * Checks whether the selectedPowerUp is valid.
//	 * 
//	 * @return isValid (boolean)
//	 */
//	public boolean isSelectedValid() {
//		boolean inRangeMax = selectedPowerUp <= getMaxPowerUp();
//		boolean inRange = selectedPowerUp >= 0 &&
//				selectedPowerUp <= getSelectedPowerUpTop();
//		
//		return inRangeMax && inRange;
//	}
	
}
