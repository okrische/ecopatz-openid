package de.ecopatz.openid.core;


public enum AssociationType {
	/** HMAC-SHA1 - 160 bit key length */
	HMAC_SHA1(OpenIDConstants.ASSOCIATION_TYPE_VALUE_HMAC_SHA1, 20),
	/** HMAC-SHA256 - 256 bit key length */
	HMAC_SHA256(OpenIDConstants.ASSOCIATION_TYPE_VALUE_HMAC_SHA256, 32);
	
	private AssociationType(String value, int keyLength) {
		this.value = value;
		this.keyLength = keyLength;
	}
	
	private final String value;
	private final int keyLength;
	
	public int getKeyLength() {
		return keyLength;
	}
	
	public String getValue() {
		return value;
	}
	
	public String getAssocationTypeRequestKey() {
		return OpenIDConstants.OPENID_ASSOCIATION_TYPE;
	}
	
	public String getAssociationTypeResponseKey() {
		return OpenIDConstants.ASSOCIATION_TYPE;
	}
}
