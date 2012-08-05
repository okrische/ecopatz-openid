package de.ecopatz.openid.core.impl;

import java.math.BigInteger;

import de.ecopatz.openid.core.DH;

final class DHBean implements DH {
	private final BigInteger publicKey;
	private final BigInteger privateKey;
	private final BigInteger modulusPrime;
	private final BigInteger generator;

	DHBean(
			final BigInteger publicKey,
			final BigInteger privateKey, 
			final BigInteger modulusPrime, 
			final BigInteger generator) {
		this.publicKey = publicKey;
		this.privateKey = privateKey;
		this.modulusPrime = modulusPrime;
		this.generator = generator;
	}
	
	public BigInteger getPublicKey() {
		return publicKey;
	}
	
	public BigInteger getPrivateKey() {
		return privateKey;
	}
	
	public BigInteger getModulusPrime() {
		return modulusPrime;
	}
	
	public BigInteger getGenerator() {
		return generator;
	}
}
