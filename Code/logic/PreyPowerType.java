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
public enum PreyPowerType {
	
	SpeedUpPrey,
	Reverse,
	Teleport,
	SlowDownPredator,
	Freeze,
	Random;
	
    public String value() {
        return name();
    }

    public static PreyPowerType fromValue(String v) {
        return valueOf(v);
    }
}
