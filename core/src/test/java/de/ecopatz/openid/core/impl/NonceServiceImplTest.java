package de.ecopatz.openid.core.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;

public class NonceServiceImplTest {

	@Test
	public void test() {
		final Calendar c = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		final RandomSupplierImpl rnd = new RandomSupplierImpl();
		final NonceServiceImpl impl = new NonceServiceImpl(rnd);

		for(int i = 0; i < 1000; i++) {			
			final long now = Math.abs(rnd.nextLong());
			c.setTimeInMillis(now);
			c.set(Calendar.MILLISECOND, 0);
			final long nowWithoutMillis = c.getTimeInMillis();
			final Date date = new Date(nowWithoutMillis);

			final String nonce = impl.createNonce(date);
			Assert.assertNotNull(nonce);
			Assert.assertTrue(nonce.length() <= NonceServiceImpl.NONCE_MAXLENGTH);
			
			final Date parsedDate = impl.parseDate(nonce);
			Assert.assertNotNull(parsedDate);
			Assert.assertEquals(date, parsedDate);			
		}
	}

}
