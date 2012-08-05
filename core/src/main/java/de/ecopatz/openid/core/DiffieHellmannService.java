package de.ecopatz.openid.core;

import java.math.BigInteger;

public interface DiffieHellmannService {
	/**
	 * Computes the shared secret number (called ZZ)
	 * 
	 * <pre>
	 * X9.42 defines that the shared secret ZZ is generated as follows:
     * 
     * ZZ = g ^ (xb * xa) mod p
     *
     * Note that the individual parties actually perform the computations:
     * 
     * ZZ = (yb ^ xa)  mod p  = (ya ^ xb)  mod p
     * 
     * where ^ denotes exponentiation
     *
     *   ya is party a's public key; ya = g ^ xa mod p
     *   yb is party b's public key; yb = g ^ xb mod p
     *   xa is party a's private key
     *   xb is party b's private key
     *   p is a large prime
     *   q is a large prime
     *   g = h^{(p-1)/q} mod p, where
     *   h is any integer with 1 < h < p-1 such that h{(p-1)/q} mod p > 1
     *     (g has order q mod p; i.e. g^q mod p = 1 if g!=1)
     *   j a large integer such that p=qj + 1
     *   (See Section 2.2 for criteria for keys and parameters)
     * </pre>
	 * @param privateKey is xa
	 * @param modulusPrime is p 
	 * @param otherPublicKey is yb
	 * @return shared secret number
	 * @see RFC2631 Diffie-Hellman Key Agreement Method, starting with 2.1
	 */
	BigInteger computeSharedSecretNumber(DH dh, BigInteger otherPublicKey);

	/**
	 * A new DiffieHellmann key pair, having its own modulusPrime and generator
	 * 
	 * @return new dh pair
	 */
	DH newPair();
	
	/**
	 * A new DiffieHellmann, based on modulusPrime and generator.
	 * 
	 * @param modulusPrime
	 * @param generator
	 * @return new dh pair
	 */
	DH newPair(final BigInteger modulusPrime, final BigInteger generator);

	/**
	 * ZZ = g ^ (xb * xa) mod p, both, dh and otherDH, must have the same modulusPrima and generator. 
	 * Its here just for completeness, usually you never own both pairs
	 * 
	 * @param dh provides xa
	 * @param otherDH provides xb
	 * @return shared secret number
	 */
	BigInteger computeSharedSecretNumber(DH dh, DH otherDH);
}
