package de.ecopatz.openid.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import de.ecopatz.openid.client.XRDSDiscoveryService;

public class XRDSDiscoveryServiceImpl implements XRDSDiscoveryService {
	private final static Logger LOG = LoggerFactory.getLogger(XRDSDiscoveryServiceImpl.class);
	// can appear in http response or in the received document
	private final static String XRDS_LOCATION_HEADER = "X-XRDS-Location";
	private final static String CONTENT_TYPE_HEADER = "Content-Type";
	private final static String CONTENT_TYPE_XRDS = "application/xrds+xml";

	@Override
	public String locate(URL url) {				
		// hm. should be an identifier, not an url
		Result finalResult = null;
		try {			
			final Result result = toStringFromURL(url);
			if (result.isHavingDocument()) {
				// yuhu
				finalResult = result;
			} else if (result.isHavingLocations()) {				
				for(final String otherIdentifier : result.getLocations()) {
					final URL otherURL = new URL(otherIdentifier);
					final Result otherResult = toStringFromURL(otherURL);
					if (otherResult.isHavingDocument()) {
						finalResult = otherResult;
						break;
					}
				}				
			}
		} catch (IOException ex) {
			LOG.warn("{}",url, ex);
		}
		
		return finalResult == null ? "" : finalResult.getDocument();

	}

	private Result toStringFromURL(final URL resource) throws IOException {
		final Result result;
		// connect
		LOG.debug("trying {}",resource);

		if (resource.toString().startsWith("file:")) {
			final String documentFromStream = documentFromStream(resource.openStream());
			result = Result.of(documentFromStream);
		} else {
			final URLConnection openConnection = resource.openConnection();
			openConnection.setRequestProperty("Accept", "application/xrds+xml");		
			final Map<String, List<String>> headerFields = openConnection.getHeaderFields();		
			final String contentType = openConnection.getContentType();
			

			if (contentType != null && contentType.contains(CONTENT_TYPE_XRDS)) {
				LOG.debug("return xrds document for resource: {}",resource);
				result = documentFromOpenConnection(resource, openConnection);
			} else {
				// TODO: handler for html to parse META: http-equiv="X-XRDS-Location"
				final List<String> locations = headerFields.get(XRDS_LOCATION_HEADER);
				if (locations != null) {
					final Iterable<String> withoutCurrentResource = Iterables.filter(locations, Predicates.not(Predicates.equalTo(resource.toString())));
					final List<String> locationsWithoutCurrent = Lists.newArrayList(withoutCurrentResource);
					LOG.debug("return xrds locations resource: {} ",locationsWithoutCurrent, resource);
					result = Result.of(locations);
				} else {
					LOG.debug("unable to hande response, return nothing for: {}", resource);
					result = Result.of();
				}
			}			
		}

		return result;				
	}

	private Result documentFromOpenConnection(URL resource, URLConnection openConnection) {
		String document = null;
		InputStream openStream = null;
		try {
			openStream = openConnection.getInputStream();
			document = documentFromStream(openStream);
		} catch (IOException ex) {
			LOG.warn("{}",resource,ex);
		} finally {
			if (openStream != null) {
				try {
					openStream.close();
				} catch (IOException ex) {
					LOG.warn("{}",resource,ex);
				}
			}
		}			
		return document==null ? Result.of() : Result.of(document);
	}

	private final static class Result {
		static Result of(List<String> locations) {
			return new Result(locations, null);
		}

		static Result of() {
			return new Result(null,null);
		}

		static Result of(String document) {
			return new Result(null, document);
		}

		boolean isHavingLocations() {
			return locations != null && !locations.isEmpty();
		}
		boolean isHavingDocument() {
			return document != null;
		}

		private Result(List<String> locations, String document) {
			this.locations = locations;
			this.document = document;
		}
		final List<String> locations;
		final String document;

		public List<String> getLocations() {
			return locations;
		}

		public String getDocument() {
			return document;
		}
	}
	private String documentFromStream(InputStream is) throws IOException {
		final InputStreamReader inputStreamReader = new InputStreamReader(is, Charset.forName("UTF-8"));
		final StringBuilder buffer = new StringBuilder();
		final Reader in = new BufferedReader(inputStreamReader);
		int ch;
		while ((ch = in.read()) > -1) {
			buffer.append((char)ch);
		}
		return buffer.toString();
	}
}
