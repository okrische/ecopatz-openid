package de.ecopatz.openid.core.impl;

import java.security.SecureRandom;

import de.ecopatz.openid.core.RandomSupplier;

/**
 * Thread-safe impl of RandomSupplier, that uses SecureRandom
 * 
 * @author krische
 *
 */
public class RandomSupplierImpl implements RandomSupplier{
	// dont assume, that random is thread-safe. it looks like it isnt.
	// in any way, random is guarded by this class
	private final SecureRandom secureRandom;

	public RandomSupplierImpl() {
		final int seedSize = 20;
		final SecureRandom init = new SecureRandom();
		final byte[] seed = init.generateSeed(seedSize);
		secureRandom = new SecureRandom(seed);
	}

	@Override
	public long nextLong() {
		synchronized (this) {
			return secureRandom.nextLong();
		}
	}

	@Override
	public void nextInt(final int n, final int[] integers) {		
		for(int i = 0; i < integers.length; i++) {
			synchronized (this) { 
				integers[i] = secureRandom.nextInt(n);	
			}
		}		
	}
}
