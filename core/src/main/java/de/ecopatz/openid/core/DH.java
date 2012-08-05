package de.ecopatz.openid.core;

import java.math.BigInteger;

public interface DH {	
	/**
	 * The public key, y
	 * 
	 * @return public key
	 */
	BigInteger getPublicKey();
	
	/**
	 * The relying party chooses a random private key 'xa' the OpenID provider chooses a random private key 'xb', both in range of [1.. p-1]
	 * 
	 * The private key, x
	 * 
	 * @return private key
	 * 
	 */
	BigInteger getPrivateKey();
	
	/**
	 * The relying party specifies a modulus p
	 * 
	 * @return modulus
	 */
	BigInteger getModulusPrime();
	
	/**
	 * The relying party specifies a generator, g.
	 */
	BigInteger getGenerator();
}
