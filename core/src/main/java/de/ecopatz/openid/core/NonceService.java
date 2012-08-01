package de.ecopatz.openid.core;

import java.util.Date;

/**
 * Thread-safe NonceService to create and parse nonces, according to the OpenID spec
 * 
 * @author krische
 *
 */
public interface NonceService {
	/** 
	 * Creates a new nonce, that encodes the date, as specified by the openId specification 
	 * 
	 * @param date
	 * @return nonce
	 */
	String createNonce(Date date);

	/**
	 * Parses and returns the date from the nonce, as created by the NonceService. If nonce is invalid, null is returned
	 * @param nonce
	 * @return parsed date or null, if nonce is invalid
	 */
	Date parseDate(String nonce);
}
