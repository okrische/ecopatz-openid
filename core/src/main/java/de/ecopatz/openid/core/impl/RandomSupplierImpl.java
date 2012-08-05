package de.ecopatz.openid.core.impl;

import java.math.BigInteger;
import java.security.SecureRandom;

import de.ecopatz.openid.core.OpenIDConstants;
import de.ecopatz.openid.core.RandomSupplier;

/**
 * Thread-safe impl of RandomSupplier, that uses SecureRandom
 * 
 * @author krische
 *
 */
public class RandomSupplierImpl implements RandomSupplier{
	// dont assume, that random is thread-safe. it looks like it isnt.
	// in any way, random is guarded by its object
	private final SecureRandom secureRandom;

	public RandomSupplierImpl() {
		final int seedSize = 20;
		final SecureRandom init = new SecureRandom();
		final byte[] seed = init.generateSeed(seedSize);
		secureRandom = new SecureRandom(seed);
	}

	@Override
	public long nextLong() {
		synchronized (secureRandom) {
			return secureRandom.nextLong();
		}
	}

	@Override
	public void nextInt(final int n, final int[] integers) {		
		for(int i = 0; i < integers.length; i++) {
			synchronized (secureRandom) { 
				integers[i] = secureRandom.nextInt(n);	
			}
		}		
	}
	
	@Override
	public BigInteger newLargePrime() {
		// TODO: do it better, than using defaults
		return OpenIDConstants.DH_MODULUS_P;
	}
	
	@Override
	public BigInteger newGenerator(BigInteger prima) {
		// TODO: do it better, than using defaults
		return OpenIDConstants.DH_GENERATOR_G;
	}
	
	private BigInteger newRandomBigInteger(final int bitLength) {
		synchronized (secureRandom) {
			// non-negative
		   return new BigInteger(bitLength, secureRandom);	
		}
	}
	
	@Override
	public BigInteger newBigInteger(final BigInteger min, final BigInteger max) {		
		// 0 < x < max
		final int bitLength = max.bitLength();
		BigInteger x = newRandomBigInteger(bitLength);
		while (x.compareTo(min) < 1 || x.compareTo(max) > -1) {
			// next
			x = newRandomBigInteger(bitLength);
		}
		return x;
	}
	
	
}
