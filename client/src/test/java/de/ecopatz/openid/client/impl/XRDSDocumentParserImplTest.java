package de.ecopatz.openid.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class XRDSDocumentParserImplTest {

	// live is no good, when behind a firewall
	private boolean live = false;
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

	private void checkResource(final URL resource, final String expectedEndpoint) {
		final XRDSDocumentParserImpl xrdsDocumentParserImpl = new XRDSDocumentParserImpl();

		try {
			final String document = toStringFromURL(resource);
			final List<String> discoverEndpointURIs = xrdsDocumentParserImpl.discoverEndpointURIs(document);
			Assert.assertNotNull(discoverEndpointURIs);
			Assert.assertTrue(!discoverEndpointURIs.isEmpty());
			Assert.assertTrue("expected url not contained: "+expectedEndpoint,discoverEndpointURIs.contains(expectedEndpoint));
		} catch (IOException e) {
			Assert.fail("fail while loading or parsing: "+resource);
		}

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

	private String toStringFromURL(final URL resource) throws IOException {
		final InputStream openStream = resource.openStream();
		final InputStreamReader inputStreamReader = new InputStreamReader(openStream, Charset.forName("UTF-8"));
		final StringBuilder buffer = new StringBuilder();
		Reader in = new BufferedReader(inputStreamReader);
		int ch;
		while ((ch = in.read()) > -1) {
			buffer.append((char)ch);
		}
		in.close();
		return buffer.toString();
	}

}
