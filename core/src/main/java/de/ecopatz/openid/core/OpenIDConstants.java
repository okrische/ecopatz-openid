package de.ecopatz.openid.core;

import java.math.BigInteger;

public class OpenIDConstants {
	// default modulus prime number from openid v2.0 specification as HEX
	private final static String DH_MODULUS_PRIME_HEX = "DCF93A0B883972EC0E19989AC5A2CE310E1D37717E8D9571BB7623731866E61EF75A2E27898B057F9891C2E27A639C3F29B60814581CD3B2CA3986D2683705577D45C2E7E52DC81C7A171876E5CEA74B1448BFDFAF18828EFD2519F14E45E3826634AF1949E5B535CC829A483B8A76223E5D490A257F05BDFF16F2FB22C583AB";
	// default modulus prime number from openid v2.0 specification
	public final static BigInteger DH_MODULUS_P = new BigInteger(DH_MODULUS_PRIME_HEX, 16);
	// default generator key from openid v2.0 specification
	public final static BigInteger DH_GENERATOR_G = BigInteger.valueOf(2); // default = 2
	
	// association types
	public final static String ASSOCIATION_TYPE_VALUE_HMAC_SHA1 = "HMAC-SHA1";
	public final static String ASSOCIATION_TYPE_VALUE_HMAC_SHA256 = "HMAC-SHA256";
	public static final String ASSOCIATION_TYPE = "assoc_type";
	public static final String OPENID_ASSOCIATION_TYPE = "openid.assoc_type";

	// session types
	public final static String SESSION_TYPE_VALUE_NO_ENCRYPTION = "no-encryption";
	public final static String SESSION_TYPE_VALUE_DH_SHA1 = "DH-SHA1";
	public final static String SESSION_TYPE_VALUE_DH_SHA256 = "DH-SHA256";
	public static final String SESSION_TYPE = "session_type";
	public static final String OPENID_SESSION_TYPE = "openid.session_type";
	
	public static final String OPENID_VALUE_SERVER = "http://specs.openid.net/auth/2.0/server";
	public final static String OPENID_VALUE_IDENTIFIER_SELECT = "http://specs.openid.net/auth/2.0/identifier_select";
}
