package nz.co.pizzashack.util;

import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sun.jersey.api.client.ClientResponse;

public class JerseyClientUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JerseyClientUtil.class);

	public static String getResponsePayload(final ClientResponse response) {
		String result = null;
		try (InputStream in = response.getEntityInputStream()) {
			result = IOUtils.toString(in, "UTF-8");
		} catch (final Exception e) {
			LOGGER.error("getResponsePayload error.", e);
			throw new RuntimeException(e);
		}
		return result;
	}
}
