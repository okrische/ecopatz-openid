package de.ecopatz.openid.core;

import java.math.BigInteger;
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
	
	/**
	 * Returns a large prime number 
	 * 
	 * @return a large prima number
	 */
	BigInteger newLargePrime();

	/**
	 * Returns a generator for a large prime
	 * 
	 * @param prime
	 * @return generator
	 */
	BigInteger newGenerator(BigInteger prime);

	/**
	 * Returns a random non-negative number between min and max, exclusive min and max
	 * @param min
	 * @param max
	 * @return
	 */
	BigInteger newBigInteger(BigInteger min, BigInteger max);
}
