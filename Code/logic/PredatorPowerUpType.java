package logic;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents different types of powerups for predators.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */

@XmlType(name = "PredatorPowerUpEnum")
@XmlEnum
public enum PredatorPowerUpType {
	
	SpeedUpPredator, // Increases the speed of predator
	Reverse, // Reverses direction of prey
	Teleport, // Move to any random point in maze
	SlowDownPrey, // Decreases the speed of the prey
	Freeze; // Stop prey from moving
	
    public String value() {
        return name();
    }

    public static PredatorPowerUpType fromValue(String v) {
        return valueOf(v);
    }
}
