package de.ecopatz.openid.client;

import java.util.List;

/**
 * An XRDS document parser. An implementation must be thread-safe.
 *  
 * @author krische
 * 
 */
public interface XRDSDocumentParser {
	/**
	 * Search in XRDS-Documents for Service-URIs of Type: "http://specs.openid.net/auth/2.0/server" and returns them
	 * 
	 * <pre>
	 *  &lt;Service priority="10"&gt;
	 *     &lt;Type&gt;http://specs.openid.net/auth/2.0/server&lt;/Type&gt;
	 *     &lt;URI&gt;https://www.google.com/accounts/o8/id&lt;/URI&gt;
	 *  &lt;/Service&gt;
	 * </pre>
	 * 
	 * @see http://de.wikipedia.org/wiki/XRDS
	 * @param xrdsDocument is an xml document
	 * @return immutable list  of endpoint URIs
	 */
	List<String> discoverEndpointURIs(String xrdsDocument);
}
