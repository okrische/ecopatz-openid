package de.ecopatz.openid.client.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Assert;
import org.junit.Test;

public class XRDSDiscoveryServiceImplTest {
	@Test
	public void testGoogle() throws IOException{
		final String url = "https://www.google.com/accounts/o8/id";
		check(url);
	}


	@Test
	public void testYahoo() throws IOException {
		final String url = "http://open.login.yahooapis.com/openid20/www.yahoo.com/xrds";
		check(url);
	}

	@Test
	public void testMyOpenId() throws IOException {
		final String url = "http://www.myopenid.com/xrds";
		check(url);
	}
	
	@Test
	public void testLiveJournal() throws IOException {
		final String url = "http://www.livejournal.com/openid/server.bml";
		check(url);
	}

	@Test
	public void testAOL() throws IOException {
		final String url = "http://openid.aol.com/";
		check(url);
	}

	@Test
	public void testVerisign() throws IOException {
		final String url = "https://pip.verisignlabs.com/";
		check(url);
	}

	


	private void check(String identifier) throws MalformedURLException {
		XRDSDiscoveryServiceImpl impl = new XRDSDiscoveryServiceImpl();
		final URL url = new URL(identifier);
		final String document = impl.locate(url);
		Assert.assertNotNull(document);
		Assert.assertTrue("document is empty for: "+url, !document.isEmpty());		
	}

}
