package de.ecopatz.openid.core;

import java.util.Random;


/**
 * Thread-safe way of supplying randon numbers
 * 
 * @author krische
 *
 */
public interface RandomSupplier {
	/**
	 * Returns a random long
	 * 
	 * @return random long
	 */
	long nextLong();
	
	/**
	 * Fills up the array with random values, that range between 0 (inclusive) and n (exclusive)
	 *  
	 * @see Random#nextInt(int) for the description of on
	 * @param n
	 * @param integers
	 */
	void nextInt(int n, int[] integers);
}
