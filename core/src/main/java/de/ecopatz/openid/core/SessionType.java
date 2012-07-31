package de.ecopatz.openid.core;


public enum SessionType {
	NO_ENCRYPTION(OpenIDConstants.SESSION_TYPE_VALUE_NO_ENCRYPTION, 0),
	/** 160 bits = 20 bytes */
	DH_SHA1(OpenIDConstants.SESSION_TYPE_VALUE_DH_SHA1, 20),
	/** 256 bits = 32 bytes */
	DH_SHA256(OpenIDConstants.SESSION_TYPE_VALUE_DH_SHA256, 32);
	
	private SessionType(String value, int keyLength) {
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
	
	public String getSessionTypeRequestKey() {
		return OpenIDConstants.OPENID_SESSION_TYPE;
	}
	
	public String getSessionTypeResponseKey() {
		return OpenIDConstants.SESSION_TYPE;
	}
}


