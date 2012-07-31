package de.ecopatz.openid.client.impl;

import java.io.StringReader;
import java.util.List;

import javax.xml.stream.StreamFilter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import de.ecopatz.openid.client.XRDSDocumentParser;
import de.ecopatz.openid.core.OpenIDConstants;

/**
 * 
 * 
 * This class searches in XRDS-Documents for Service-URIs of Type: "http://specs.openid.net/auth/2.0/server"
 * 
 * <pre>
 *  &lt;Service priority="10"&gt;
 *     &lt;Type&gt;http://specs.openid.net/auth/2.0/server&lt;/Type&gt;
 *     &lt;URI&gt;https://www.google.com/accounts/o8/id&lt;/URI&gt;
 *  &lt;/Service&gt;
 * </pre>
 * 
 * @see http://de.wikipedia.org/wiki/XRDS
 * @author krische
 * 
 */
public class XRDSDocumentParserImpl implements XRDSDocumentParser {
	private final static Logger LOG = LoggerFactory.getLogger(XRDSDocumentParserImpl.class);
	private final static String XRDS_NAMESPACE ="xri://$xrd*($v*2.0)";
	private final static class MyStreamFilter implements StreamFilter {
		@Override
		public boolean accept(XMLStreamReader reader) {
			// only match start/end elements of xrd namespace
			if ((reader.isStartElement() || reader.isEndElement()) && Objects.equal(XRDS_NAMESPACE, reader.getNamespaceURI())) {
				return true;
			} else {
				return false;
			}
		}
	}	
	// immutable
	private final static StreamFilter XRDS_NAMESPACE_STREAMFILTER = new MyStreamFilter();
	// thread-safe
	private final XMLInputFactory f;

	public XRDSDocumentParserImpl() {
		f = XMLInputFactory.newInstance();
	}

	public List<String> discoverEndpointURIs(final String document) {
		// parse as XRDS
		final StringReader stringReader = new StringReader(document);
		final List<String> servers = Lists.newArrayListWithExpectedSize(2);
		XMLStreamReader r;
		try {
			final XMLStreamReader createXMLStreamReader = f.createXMLStreamReader(stringReader);
			r = f.createFilteredReader(createXMLStreamReader, XRDS_NAMESPACE_STREAMFILTER);
			boolean inService = false;
			boolean hasServer = false;
			while (r.hasNext()) {
				r.next();
				if (r.isStartElement()) {
					// <Service>
					if (Objects.equal("Service", r.getLocalName())) {
						inService = true;
					} else if (inService) {
						// <Service><Type>http://specs.openid.net/auth/2.0/server
						if (Objects.equal(r.getLocalName(), "Type")) {
							final String type = r.getElementText();
							if (Objects.equal(OpenIDConstants.OPENID_VALUE_SERVER,type)) {
								hasServer = true;
							}
						} else if (hasServer) {
							// use value for URI only, when there was at least one <TYPE>, that points to openid 2.0
							// e.g. <Service><URI>http://www.myopenid.com/server                            
							if (Objects.equal("URI", r.getLocalName())) {
								final String uri = r.getElementText();
								servers.add(uri);
							}
						}
					}
				} else if (r.isEndElement()) {
					if (Objects.equal("Service",r.getLocalName())) {
						// reset
						inService = false;
						hasServer = false;
					}
				}
			}
		} catch (XMLStreamException e) {
			LOG.warn("parse error, ignoring and returning found servers so far",e);
		}

		return ImmutableList.copyOf(servers);		
	}
}
