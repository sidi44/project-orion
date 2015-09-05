package logic;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * Represents different types of powerups for preys.
 * 
 * @author Martin Wong
 * @version 2015-06-11
 */

@XmlType(name = "PreyPowerUpEnum")
@XmlEnum
public enum PreyPowerUpType {
	
	SpeedUpPrey, // Increases the speed of prey
	Reverse, // Reverses direction of predator
	Teleport, // Move to any random point in maze
	SlowDownPredator, // Decreases the speed of predator
	Freeze; // Stop predator from moving
	
    public String value() {
        return name();
    }

    public static PreyPowerUpType fromValue(String v) {
        return valueOf(v);
    }
}
