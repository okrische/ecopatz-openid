package de.ecopatz.openid.core.impl;

import java.math.BigInteger;

import junit.framework.Assert;

import org.junit.Test;

import de.ecopatz.openid.core.DH;
import de.ecopatz.openid.core.RandomSupplier;

public class DiffieHellmannServiceImplTest {
	@Test
	public void test() {
		final RandomSupplier rnd = new RandomSupplierImpl();
		final DiffieHellmannServiceImpl i = new DiffieHellmannServiceImpl(rnd);		
		
		final DH pair1 = i.newPair();
		// 1 < x < p
		Assert.assertTrue(BigInteger.ONE.compareTo(pair1.getPrivateKey()) <= 0);
		Assert.assertTrue(pair1.getPrivateKey().compareTo(pair1.getModulusPrime()) < 0);
		
		final DH pair2 = i.newPair(pair1.getModulusPrime(), pair1.getGenerator());
		// 1 < x < p
		Assert.assertTrue(BigInteger.ONE.compareTo(pair2.getPrivateKey()) <= 0); // 1 < x
		Assert.assertTrue(pair2.getPrivateKey().compareTo(pair2.getModulusPrime()) < 0); // x < p		
		
		// p and g in both pairs the same
		Assert.assertEquals(pair1.getModulusPrime(), pair2.getModulusPrime());
		Assert.assertEquals(pair1.getGenerator(), pair2.getGenerator());
		
		// ZZ = g ^ (xb * xa) mod p
		// ZZ = (yb ^ xa)  mod p  = (ya ^ xb)  mod p
		final BigInteger computeSharedSecretNumber = i.computeSharedSecretNumber(pair1, pair2);
		final BigInteger computeSharedSecretNumber1 = i.computeSharedSecretNumber(pair1, pair2.getPublicKey());
		final BigInteger computeSharedSecretNumber2 = i.computeSharedSecretNumber(pair2, pair1.getPublicKey());
		Assert.assertEquals(computeSharedSecretNumber1, computeSharedSecretNumber2);
		Assert.assertEquals(computeSharedSecretNumber, computeSharedSecretNumber1);
		Assert.assertEquals(computeSharedSecretNumber, computeSharedSecretNumber2);
	}

}
