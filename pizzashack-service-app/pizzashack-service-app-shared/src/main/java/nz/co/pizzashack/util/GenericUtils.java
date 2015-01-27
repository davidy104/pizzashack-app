package nz.co.pizzashack.util;

import java.lang.reflect.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenericUtils.class);

	public static Object getValueByField(final Object instance, final String fieldName) {
		try {
			Field field = instance.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			return field.get(instance);
		} catch (final Exception e) {
			LOGGER.error("getValueByField error.", e);
		}
		return null;
	}
}
