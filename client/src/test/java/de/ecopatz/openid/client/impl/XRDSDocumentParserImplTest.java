package de.ecopatz.openid.client.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ecopatz.openid.client.XRDSDiscoveryService;

public class XRDSDocumentParserImplTest {
	private final static Logger LOG = LoggerFactory.getLogger(XRDSDocumentParserImplTest.class);
	// live is no good, when behind a firewall
	private boolean live = true;
	private boolean fromFile = true;

	@Test
	public void testGoogle() throws IOException{
		final String url = "https://www.google.com/accounts/o8/id";
		final String expect = "https://www.google.com/accounts/o8/ud";
		final String file = "google_xrds.xml";
		check(url, expect, file);
	}


	@Test
	public void testYahoo() throws IOException {
		final String url = "http://open.login.yahooapis.com/openid20/www.yahoo.com/xrds";
		final String expect = "https://open.login.yahooapis.com/openid/op/auth";
		final String file = "yahoo_xrds.xml";
		check(url, expect, file);		
	}

	@Test
	public void testMyOpenId() throws IOException {
		final String url = "http://www.myopenid.com/xrds";
		final String expect = "http://www.myopenid.com/server";
		final String file = "myopenid_xrds.xml";
		check(url, expect, file);
	}

	@Ignore
	@Test
	public void testLiveJournal() throws IOException {
		// seems like, livejournal returns xrds, but without indication of any endpoint. hmmm.
		final String url = "http://www.livejournal.com/openid/server.bml";
		final String expect = "";
		final String file = "livejournal_xrds.xml";
		check(url, expect, file);
	}
	
	@Test
	public void testAOL() throws IOException {
		// http://www.livejournal.com/openid/server.bml
		final String url = "http://openid.aol.com/";
		final String expect = "https://api.screenname.aol.com/auth/openidServer";
		final String file = "aol_xrds.xml";
		check(url, expect, file);
	}
	
	@Test
	public void testVerisign() throws IOException {
		final String url = "https://pip.verisignlabs.com/";
		final String expect = "https://pip.verisignlabs.com/server";
		final String file = "verisign_xrds.xml";
		check(url, expect, file);
	}

	
	private void checkResource(final URL resource, final String expectedEndpoint) {
		final XRDSDocumentParserImpl xrdsDocumentParserImpl = new XRDSDocumentParserImpl();
		final XRDSDiscoveryService discovery = new XRDSDiscoveryServiceImpl();
		final String document = discovery.locate(resource);
		LOG.debug(document);
		final List<String> discoverEndpointURIs = xrdsDocumentParserImpl.discoverEndpointURIs(document);
		Assert.assertNotNull(discoverEndpointURIs);
		Assert.assertTrue(!discoverEndpointURIs.isEmpty());
		Assert.assertTrue("expected url not contained: "+expectedEndpoint,discoverEndpointURIs.contains(expectedEndpoint));

	}

	private void check(final String discoveryURL, final String expectedEndpoint, final String file) {
		if (fromFile) {
			final URL resource = asFileResource(file);
			checkResource(resource, expectedEndpoint);			
		}

		// now live
		if (live) {
			try {
				final URL resource = new URL(discoveryURL);
				checkResource(resource, expectedEndpoint);
			} catch (MalformedURLException e) {
				Assert.fail("malformed discoveryURL: "+discoveryURL);
			}
		}

	}

	private URL asFileResource(final String file) {
		// "de/ecopatz/openid/client/impl/$file"
		final String name = getClass().getPackage().getName().replace(".","/")+"/"+file;
		final URL resource = getClass().getClassLoader().getResource(name);
		Assert.assertNotNull("missing: "+name,resource);
		return resource;
	}
}
