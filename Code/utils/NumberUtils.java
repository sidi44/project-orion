package utils;

import java.util.Arrays;
import java.util.Random;

/**
 * Class with static methods for random number generation and
 * number manipulation.
 * 
 * @author Martin Wong
 * @version 2015-01-20
 */
public class NumberUtils {
	
	/**
	 * Sort two numbers into ascending order.
	 * 
	 * @param num1 (double)
	 * @param num2 (double)
	 * @return sorted numbers (double[])
	 */
	public static double[] sortAscending(double num1, double num2) {
		double[] sorted = new double[]{num1, num2};
		Arrays.sort(sorted);
		return sorted;
	}
	
	/**
	 * Generates a random int in the range: num1 (inclusive) to num2 (inclusive).
	 * 
	 * @param num1 (int)
	 * @param num2 (int)
	 * @return random int in range (int)
	 */
	public static int randomInt(int num1, int num2) {
		int min = num1;
		int max = num2;
		
		if (num1 > num2) {
			min = num2;
			max = num1;
		}
		
		Random r = new Random();
		return r.nextInt((max - min) + 1) + min; // + 1 for inclusive
	}
	
	/**
	 * Generates a random double in the range: num1 (inclusive) to num2 (inclusive).
	 * 
	 * @param num1 (double)
	 * @param num2 (double)
	 * @return random double in range (double)
	 */
	public static double randomDouble(double num1, double num2) {
		double min = num1;
		double max = num2;
		
		if (num1 > num2) {
			min = num2;
			max = num1;
		}
		
		Random r = new Random();
		return r.nextDouble() * (max - min) + min;
	}
	
	
	/**
	 * Checks whether a double is within min (inclusive) and max (inclusive) provided.
	 * 
	 * @param val (double)
	 * @param min (double)
	 * @param max (double)
	 * @return inLimits (boolean)
	 */
	public static boolean withinLimits(double val, double min, double max) {
		if (min > max) {
			return false;
		} else {
			return (val >= min) && (val <= max);
		}
	}
	
}
