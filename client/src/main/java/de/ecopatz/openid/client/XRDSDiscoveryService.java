package de.ecopatz.openid.client;

import java.net.URL;

/**
 * An XRDS document discovery service. An implementation must be thread-safe.
 * 
 * @author krische
 *
 */
public interface XRDSDiscoveryService {
	/**
	 * Discover the XRDS document by the given identifier. 
	 * 
	 * @see http://de.wikipedia.org/wiki/Yadis
	 * @param url
	 * @return the actual xrds document. Can be empty, if nothing has been found.
	 */
	String locate(URL url);
}
