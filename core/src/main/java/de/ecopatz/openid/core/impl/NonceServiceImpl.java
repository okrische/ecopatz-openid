package de.ecopatz.openid.core.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.ecopatz.openid.core.NonceService;
import de.ecopatz.openid.core.RandomSupplier;

/**
 * Impl for a thread-safe NonceService according to OpenId 2.0 Specification
 * 
 * @author krische
 *
 */
public class NonceServiceImpl implements NonceService {
	private final static Logger LOG= LoggerFactory.getLogger(NonceServiceImpl.class);
	// maximum length of a nonce, see OpenID 2.0 specification
	final static int NONCE_MAXLENGTH = 255;

	// date pattern, see OpenID 2.0 specification, always in UTC
	private final static String NONCE_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";

	// SimpleDateFormat is _NOT_ thread-safe. must be guarded (by their instances)   
	private final static SimpleDateFormat SIMPLEDATEFORMAT_FOR_CREATE = new SimpleDateFormat(NONCE_DATE_PATTERN);
	private final static SimpleDateFormat SIMPLEDATEFORMAT_FOR_PARSE  = new SimpleDateFormat(NONCE_DATE_PATTERN);	
	static {
		final TimeZone tz = TimeZone.getTimeZone("UTC");
		SIMPLEDATEFORMAT_FOR_CREATE.setTimeZone(tz);
		SIMPLEDATEFORMAT_FOR_PARSE.setTimeZone(tz);
	}

	private final RandomSupplier secureRandomSupplier;

	public NonceServiceImpl(RandomSupplier secureRandomSupplier) {
		this.secureRandomSupplier = secureRandomSupplier;
	}

	@Override
	public String createNonce(final Date date) {
		// up to 255 characters are allowed for a nonce
		final String nonceDate = newFormattedNonceDate(date);
		final int space = NONCE_MAXLENGTH - nonceDate.length();
		if (space > 0) {
			// fill up with so that we always reach 255 characters.
			// we love traffic
			final StringBuffer buff = new StringBuffer(nonceDate.length() + space);
			buff.append(nonceDate);
			final int[] integers = new int[space];
			secureRandomSupplier.nextInt(10, integers);
			for(int i = 0; i < integers.length; i++) {
				buff.append(integers[i]);
			}
			return buff.toString();
		} else {
			return nonceDate;
		}
	}

	/**
	 * Parses the date from the nonce. A nonce is invalid and we return null as the parsed date, if:
	 * 
	 * <ul>
	 * <li>if nonce is null
	 * <li>if nonce has more than 255 chars
	 * <li>if nonce has no date encoded in the specified nonce date format
	 * </ul>
	 */
	@Override
	public Date parseDate(final String nonce) {
		// when anything goes wrong, return null
		final Date date;
		if (nonce == null) {			
			LOG.warn("nonce is null. returning -1 by default");
			date = null;
		} else if (nonce.length() > NONCE_MAXLENGTH) {
			LOG.warn("nonce has more than " + NONCE_MAXLENGTH + " chars, this breaks the spec, returning -1 by default, nonce: {} ", nonce);
			date = null;
		} else {
			// because it always must be there!
			final int indexZ = nonce.indexOf('Z'); 
			if (indexZ == -1) {
				LOG.warn("nonce contains no date of format: " + NONCE_DATE_PATTERN + " (missing Z), returning -1 by default, nonce: {} ", nonce);
				date = null;
			} else {
				final String nonceDate = nonce.substring(0, indexZ+1);			
				date = parseFormattedNonceDate(nonceDate);
				if (date == null) {
					LOG.warn("nonce contains no valid date of format: " + NONCE_DATE_PATTERN + ", returning -1 by default, nonce: {} ", nonce);
				}									
			}
		}
		return date;
	}

	private Date parseFormattedNonceDate(final String nonceDate) {
		synchronized (SIMPLEDATEFORMAT_FOR_PARSE) {
			try {
				return SIMPLEDATEFORMAT_FOR_PARSE.parse(nonceDate);
			} catch (final ParseException e) {
				// could be in wrong format
				LOG.warn("unable to parse date, returning null for nonceDate: {}", nonceDate, e);
			}
		}
		return null;
	}

	private String newFormattedNonceDate(final Date date) {
		synchronized (SIMPLEDATEFORMAT_FOR_CREATE) {
			return SIMPLEDATEFORMAT_FOR_CREATE.format(date);
		}
	}
}
