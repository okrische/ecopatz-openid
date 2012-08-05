package de.ecopatz.openid.core.impl;

import java.math.BigInteger;

import com.google.common.base.Preconditions;

import de.ecopatz.openid.core.DH;
import de.ecopatz.openid.core.DiffieHellmannService;
import de.ecopatz.openid.core.RandomSupplier;

/**
 * Impl of DiffieHellmannService.
 * 
 * <ul>
 * <li>TODO: validate public key 
 * </li>
 *
 * @author krische
 *
 */
public class DiffieHellmannServiceImpl implements DiffieHellmannService {

	private final RandomSupplier rnd;
	
	public DiffieHellmannServiceImpl(RandomSupplier rnd) {
		this.rnd = rnd;
	}

	@Override
	public BigInteger computeSharedSecretNumber(final DH dh, final DH otherDH) {		
		Preconditions.checkArgument(dh.getModulusPrime().compareTo(otherDH.getModulusPrime()) == 0, "arguments dont have the same modulusPrime number");
		Preconditions.checkArgument(dh.getGenerator().compareTo(otherDH.getGenerator()) == 0, "arguments dont have the same generator number");
		
		// ZZ = g ^ (xb * xa) mod p
		// privateKey is xa
		// otherPublicKey is yb
		final BigInteger multiplication = dh.getPrivateKey().multiply(otherDH.getPrivateKey());
		final BigInteger modulusPrime = dh.getModulusPrime();
		return dh.getGenerator().modPow(multiplication, modulusPrime);
	}

	
	@Override
	public BigInteger computeSharedSecretNumber(final DH dh, final BigInteger otherPublicKey) {
		// privateKey is xa
		// otherPublicKey is yb
		// modulusPrime is p
		// compute: ZZ = (yb ^ xa)  mod p
		final BigInteger privateKey = dh.getPrivateKey();
		final BigInteger modulusPrime = dh.getModulusPrime();
		return otherPublicKey.modPow(privateKey, modulusPrime);
	}
		
	
	@Override
	public DH newPair() {
		// 8.4.2.  Diffie-Hellman Association Sessions
		// "The Relying Party chooses a random private key x, ..., in the range [1 .. p-1]."
		//
		// 1) modulusPrime is p, private key is x, public key is y 
		// 2) choose private key x, thats in range of [1.. p-1], aka: 0 < x < p
		// 3) ya = g ^ x mod p
		final BigInteger modulusPrime = rnd.newLargePrime();
		final BigInteger generator = rnd.newGenerator(modulusPrime);
		return newPair(modulusPrime, generator);
	}
	
	@Override
	public DH newPair(final BigInteger modulusPrime, final BigInteger generator) {
		final BigInteger privateKey = rnd.newBigInteger(BigInteger.ONE, modulusPrime);
		final BigInteger publicKey = generator.modPow(privateKey,modulusPrime);
		return new DHBean(publicKey, privateKey, modulusPrime, generator);
	}
}
