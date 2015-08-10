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
	
	SpeedUpPredator,
	Reverse,
	Teleport,
	SlowDownPrey,
	Freeze,
	Random;
	
    public String value() {
        return name();
    }

    public static PredatorPowerUpType fromValue(String v) {
        return valueOf(v);
    }
}
